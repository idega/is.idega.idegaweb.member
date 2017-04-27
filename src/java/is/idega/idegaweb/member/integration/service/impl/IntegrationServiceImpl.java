package is.idega.idegaweb.member.integration.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.google.gson.Gson;
import com.idega.builder.bean.AdvancedProperty;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.CountryHome;
import com.idega.core.location.data.PostalCode;
import com.idega.core.location.data.PostalCodeHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWMainApplication;
import com.idega.restful.business.DefaultRestfulService;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Gender;
import com.idega.user.data.GenderHome;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelationBMPBean;
import com.idega.user.data.User;
import com.idega.user.sync.UnionMemberSynchronizer;
import com.idega.util.ArrayUtil;
import com.idega.util.CoreConstants;
import com.idega.util.EmailValidator;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;
import com.idega.util.RequestUtil;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;

import is.idega.idegaweb.member.integration.bean.Address;
import is.idega.idegaweb.member.integration.bean.MemberChangeRequest;
import is.idega.idegaweb.member.integration.bean.MemberChangeRequestList;
import is.idega.idegaweb.member.integration.bean.MemberChangeResponse;
import is.idega.idegaweb.member.integration.service.IntegrationService;

@Component
@Path(IntegrationService.PATH)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IntegrationServiceImpl extends DefaultRestfulService implements IntegrationService {

	@Override
	@POST
	@Path(IntegrationService.MEMBER_SYNC)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doSyncMember(
			@HeaderParam(RequestUtil.HEADER_AUTHORIZATION) String apiKey,
			@Context HttpServletRequest request,
			MemberChangeRequest member
	) {
		try {
			if (!isAPIKeyValid(apiKey)) {
				getLogger().warning("No access rights for API key: " + apiKey);
				return getBadRequestResponse(new MemberChangeResponse(Boolean.FALSE, member == null ? "Member's data not provided" : member.toString()));
			}

			if (member == null || StringUtil.isEmpty(member.getPersonalId())) {
				getLogger().warning("Data not provided");
				return getBadRequestResponse(new MemberChangeResponse(Boolean.FALSE, member == null ? "Member's data not provided" : member.toString()));
			}

			AdvancedProperty result = doSyncMember(request, member);
			boolean success = result == null ? false : true;
			if (result != null && result.getId() != null) {
				success = Boolean.valueOf(result.getId());
			}

			String message = result == null ? null : result.getValue();

			return success ?
					getOKResponse(new MemberChangeResponse(success, (message == null ? null : message + ": " + member.toString()))) :
					getBadRequestResponse(new MemberChangeResponse(success, (message == null ? null : message + ": " + member.toString())));
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error syncing member changes: " + member, e);
		}
		return getInternalServerErrorResponse(new MemberChangeResponse(Boolean.FALSE, member == null ? "Member's data not provided" : member.toString()));
	}

	@Override
	@POST
	@Path(IntegrationService.MEMBERS_SYNC)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response doSyncMembers(
			@HeaderParam(RequestUtil.HEADER_AUTHORIZATION) String apiKey,
			@Context HttpServletRequest request,
			MemberChangeRequestList members
	) {
		if (members == null || ListUtil.isEmpty(members.getMembers())) {
			getLogger().warning("Data not provided");
			return getBadRequestResponse(new MemberChangeResponse(Boolean.FALSE, "Members' data not provided"));
		}

		List<MemberChangeRequest> membersData = members.getMembers();
		for (MemberChangeRequest member: membersData) {
			Response response = doSyncMember(apiKey, request, member);
			if (response == null || response.getStatus() != Response.Status.OK.getStatusCode()) {
				return response == null ?
						getBadRequestResponse(new MemberChangeResponse(Boolean.FALSE, "Failed to sync " + member)) :
						response;
			}
		}

		return getOKResponse(new MemberChangeResponse(Boolean.TRUE, "Successfully synced"));
	}

	private AdvancedProperty doSyncMember(HttpServletRequest request, MemberChangeRequest member) throws Exception {
		AdvancedProperty result = new AdvancedProperty(Boolean.FALSE.toString());

		UserBusiness userBusiness = getServiceInstance(UserBusiness.class);

		User user = getCreatedOrUpdatedUser(userBusiness, member);
		if (user == null) {
			result.setValue("Unable to create/find user");
			return result;
		}

		List<Group> leaguesToSync = getLeaguesToSync(member.getLeaguesUniqueIds());
		if (ListUtil.isEmpty(leaguesToSync) && ListUtil.isEmpty(member.getLeaguesNames())) {
			result.setId(Boolean.TRUE.toString());
			result.setValue("No leagues allowed to sync");
			return result;
		}

		Boolean success = saveMember(request, userBusiness, user, member, leaguesToSync);
		if (success) {
			result.setId(success.toString());
			result.setValue("Successfully synced");
		} else {
			result.setValue("Unable to sync");
		}

		return result;
	}

	private User getCreatedOrUpdatedUser(UserBusiness userBusiness, MemberChangeRequest member) {
		try {
			Date dateOfBirth = userBusiness.getUserDateOfBirthFromPersonalId(member.getPersonalId());
			Gender gender = null;
			try {
				if (member.getGenderId() != null) {
					GenderHome genderHome = (GenderHome) IDOLookup.getHome(Gender.class);
					gender = genderHome.findByPrimaryKey(member.getGenderId());
				}
			} catch (Exception e) {}
			User user = null;
			try {
				user = userBusiness.createUserByPersonalIDIfDoesNotExist(
						member.getName(),
						member.getPersonalId(),
						gender,
						dateOfBirth == null ? null : new IWTimestamp(dateOfBirth)
				);
			} catch (Exception e) {
				getLogger().log(Level.WARNING, "Error creating/updating user with personal ID: " + member.getPersonalId(), e);
			}
			if (user == null) {
				return null;
			}

			updateEmail(userBusiness, user, member.getEmail());
			updatePhones(userBusiness, user, member.getPhones());
			updateAddress(userBusiness, user, member.getAddress());

			createLoginIfDoesNotExist(user);

			return user;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error creating/updating user: " + member.getPersonalId(), e);
		}
		return null;
	}

	private void createLoginIfDoesNotExist(User user) {
		if (user == null) {
			return;
		}

		try {
			LoginTable loginTable = LoginDBHandler.getUserLogin((Integer) user.getPrimaryKey());
			if (loginTable != null) {
				return;
			}

			String personalId = user.getPersonalID();
			if (StringUtil.isEmpty(personalId)) {
				return;
			}

			String password = personalId.length() > 6 ? personalId.substring(6) : personalId;
			LoginDBHandler.createLogin(user, personalId, password);
		} catch (Exception e) {}
	}

	private List<Group> getLeaguesToSync(List<String> leaguesUniqueIds) {
		if (ListUtil.isEmpty(leaguesUniqueIds)) {
			return null;
		}

		List<Group> leaguesToSync = null;
		try {
			String allowedLeaguesProp = getApplicationProperty("integration.allowed_leagues");
			if (StringUtil.isEmpty(allowedLeaguesProp)) {
				return null;
			}
			List<String> allowedLeagues = Arrays.asList(allowedLeaguesProp.split(CoreConstants.COMMA));
			if (ListUtil.isEmpty(allowedLeagues)) {
				return null;
			}

			leaguesToSync = new ArrayList<>();
			GroupBusiness groupBusiness = getServiceInstance(GroupBusiness.class);
			for (String leagueUniqueId: leaguesUniqueIds) {
				Group league = groupBusiness.getGroupByUniqueId(leagueUniqueId);
				if (league == null) {
					getLogger().warning("League not found by UID: " + leagueUniqueId);
					continue;
				}

				String name = league.getName();
				if (allowedLeagues.contains(name)) {
					leaguesToSync.add(league);
				} else {
					getLogger().warning("League " + name + " is not allowed to sync. UID: " + leagueUniqueId);
				}
			}
		} catch (FinderException fe) {
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error getting leagues to sync. Leagues UIDs: " + leaguesUniqueIds, e);
		}
		return leaguesToSync;
	}

	private boolean saveMember(HttpServletRequest request, UserBusiness userBusiness, User user, MemberChangeRequest member, List<Group> leagues) {
		Group club = null;
		UnionMemberSynchronizer synchronizer = null;

		try {
			if (user == null || member == null) {
				return false;
			}

			Map<String, UnionMemberSynchronizer> synchronizers = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext()).getBeansOfType(UnionMemberSynchronizer.class);
			if (MapUtil.isEmpty(synchronizers)) {
				getLogger().warning("There are no synchronizers for " + UnionMemberSynchronizer.class.getName());
				return false;
			}

			boolean anyUnionSynced = false;
			for (Iterator<UnionMemberSynchronizer> iter = synchronizers.values().iterator(); iter.hasNext();) {
				synchronizer = iter.next();

				club = null;
				try {
					club = ListUtil.isEmpty(leagues) ?
							synchronizer.getClubByUniqueIdAndLeaguesName(member.getClubUniqueId(), member.getLeaguesNames()) :
							synchronizer.getClubByUniqueId(member.getClubUniqueId(), leagues);
				} catch (Exception e) {
					club = null;
				}
				if (club == null) {
					getLogger().warning("Club with UID '" + member.getClubUniqueId() + "' was not found by synchronizer " + synchronizer.getClass().getName() + ". Leagues " +
							(ListUtil.isEmpty(leagues) ? "names: " + member.getLeaguesNames() : leagues));
					continue;
				}

				String success = null;
				if (isActive(member.getStatus())) {
					success = synchronizer.registerMemberToClub(member.getPersonalId(), club, member.getMembershipType(), member.getGroupUniqueId());
				} else {
					success = synchronizer.disableMemberInClub(member.getPersonalId(), club, member.getGroupUniqueId());
				}

				if (success == null || !UnionMemberSynchronizer.WS_MSG_SUCCESS.equals(success)) {
					return false;
				}

				anyUnionSynced = true;
				getLogger().info("Synced " + member + " with " + synchronizer.getClass().getName() + " in club " + club + " (ID: " + club.getId() + "), JSON: " + new Gson().toJson(member));
			}

			if (!anyUnionSynced) {
				return false;
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error editing member. Club: " + club + ", synchronizer: " + synchronizer.getClass().getName() + ". JSON: " + new Gson().toJson(member), e);
			return false;
		}

		return true;
	}

	private boolean isActive(String status) {
		return status != null && (GroupRelationBMPBean.STATUS_ACTIVE.equals(status) || GroupRelationBMPBean.STATUS_ACTIVE_PENDING.equals(status));
	}

	private void updateEmail(UserBusiness userBusiness, User user, String email) {
		try {
			if (EmailValidator.getInstance().isValid(email)) {
				userBusiness.updateUserMail(user, email);
			}
		} catch (Exception e) {}
	}

	private void updateAddress(UserBusiness userBusiness, User user, Address address) {
		if (address == null) {
			return;
		}

		try {
			String streetNameAndNumber = address.getStreetName();
			if (!StringUtil.isEmpty(address.getStreetNumber())) {
				streetNameAndNumber = streetNameAndNumber == null ? CoreConstants.EMPTY : streetNameAndNumber;
				streetNameAndNumber = streetNameAndNumber.concat(CoreConstants.SPACE).concat(address.getStreetNumber());
			}

			PostalCode postalCode = null;
			if (!StringUtil.isEmpty(address.getPostalCode())) {
				try {
					PostalCodeHome postalCodeHome = (PostalCodeHome) IDOLookup.getHome(PostalCode.class);
					postalCode = postalCodeHome.findByPostalCode(address.getPostalCode());
				} catch (Exception e) {}
			}
			Country country = null;
			if (!StringUtil.isEmpty(address.getCountry())) {
				try {
					CountryHome countryHome = (CountryHome) IDOLookup.getHome(Country.class);
					country = countryHome.findByCountryName(address.getCountry());
				} catch (Exception e) {}
			}

			userBusiness.updateUsersMainAddressOrCreateIfDoesNotExist(user, streetNameAndNumber, postalCode, country, address.getTown(), null, null, null);
			userBusiness.updateUsersCoAddressOrCreateIfDoesNotExist(user, streetNameAndNumber, postalCode, country, address.getTown(), null, null, null);
		} catch (Exception e) {}
	}

	private void updatePhones(UserBusiness userBusiness, User user, List<String> phones) {
		if (ListUtil.isEmpty(phones)) {
			return;
		}

		try {
			for (String phone: phones) {
				userBusiness.updateUserMobilePhone(user, phone);
			}
		} catch (Exception e) {}
	}

	private boolean isAPIKeyValid(String apiKey) {
		if (StringUtil.isEmpty(apiKey)) {
			return false;
		}

		String integrationApiKeys = getApplication().getSettings().getProperty("integration.api.keys");
		if (StringUtil.isEmpty(integrationApiKeys)) {
			return false;
		}

		String[] apiKeys = integrationApiKeys.split(CoreConstants.COMMA);
		if (ArrayUtil.isEmpty(apiKeys)) {
			return false;
		}

		AccessController accessController = IWMainApplication.getDefaultIWMainApplication().getAccessController();
		com.idega.user.data.bean.User admin = null;
		try {
			admin = accessController.getAdministratorUser();
		} catch (Exception e) {}
		String adminId = admin == null ? null : admin.getId().toString();
		adminId = adminId == null ? CoreConstants.EMPTY : adminId;
		UserBusiness userBusiness = getServiceInstance(UserBusiness.class);
		for (String key: apiKeys) {
			if (apiKey.equals(key.trim())) {
				try {
					User user = userBusiness.getUserByUniqueId(apiKey);
					return user != null && (user.getId().equals(adminId) || accessController.hasRole(user, "api_integrations"));
				} catch (Exception e) {}
			}
		}

		return false;
	}

}