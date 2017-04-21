package is.idega.idegaweb.member.sync.impl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

import com.idega.core.business.DefaultSpringBean;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.sync.UnionMemberSynchronizer;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;

import is.idega.idegaweb.member.business.MemberUserBusiness;
import is.idega.idegaweb.member.business.NoLeagueFoundException;
import is.idega.idegaweb.member.util.IWMemberConstants;

public abstract class DefaultUnionMemberSynchronizer extends DefaultSpringBean implements UnionMemberSynchronizer {

	public abstract String getSportUnion();

	@Override
	public Group getClub(String clubNumber) {
		try {
			GroupBusiness groupBiz = getServiceInstance(GroupBusiness.class);
			Collection<Group> clubs = groupBiz.getGroupsByMetaDataKeyAndValue(IWMemberConstants.META_DATA_CLUB_NUMBER, clubNumber);
			if (ListUtil.isEmpty(clubs)) {
				return null;
			}

			for (Group club: clubs) {
				if (IWMemberConstants.GROUP_TYPE_CLUB.equals(club.getType()) && isClubInSportUnion(club)) {
					return club;
				}
			}
		} catch (Exception e) {}
		return null;
	}

	@Override
	public abstract String registerMemberToClub(String ssn, Group club, String clubMembershipType) throws RemoteException;

	@Override
	public Group getClubByAbbreviation(String abbreviation) {
		try {
			GroupBusiness groupBiz = getServiceInstance(GroupBusiness.class);
			Collection<Group> clubs = groupBiz.getGroupsByAbbreviation(abbreviation);
			if (ListUtil.isEmpty(clubs)) {
				return null;
			}

			for (Group club: clubs) {
				if (IWMemberConstants.GROUP_TYPE_CLUB.equals(club.getType()) && isClubInSportUnion(club)) {
					return club;
				}
			}
		} catch (Exception e) {}
		return null;
	}

	@Override
	public Group getClubByUniqueId(String uniqueId) {
		try {
			GroupBusiness groupBiz = getServiceInstance(GroupBusiness.class);
			Group club = groupBiz.getGroupByUniqueId(uniqueId);
			if (club == null) {
				return null;
			}

			if (IWMemberConstants.GROUP_TYPE_CLUB.equals(club.getType()) && isClubInSportUnion(club)) {
				return club;
			}
		} catch (Exception e) {}
		return null;
	}

	@Override
	public abstract String disableMemberInClub(String ssn, Group club) throws RemoteException;

	private boolean isClubInSportUnion(Group club) {
		if (club == null) {
			return false;
		}

		String sportUnion = getSportUnion();
		if (StringUtil.isEmpty(sportUnion)) {
			return false;
		}

		try {
			MemberUserBusiness memberUserBusiness = getServiceInstance(MemberUserBusiness.class);
			List<Group> leagues = memberUserBusiness.getLeagueGroupListForClubGroup(club);
			if (ListUtil.isEmpty(leagues)) {
				getLogger().warning("No leagues were found for club: " + club + ", " + club.getName());
				return false;
			}

			for (Group league: leagues) {
				if (sportUnion.equals(league.getName())) {
					return true;
				}
			}

			getLogger().warning("No leagues were found for club: " + club + ", " + club.getName() + ", all leagues of club: " + leagues + ", expecting league: " + sportUnion);
		} catch (NoLeagueFoundException e) {
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error checking if club (ID: " + club.getId() + ", name: " + club.getName() + ") is in a league " + sportUnion, e);
		}
		return false;
	}

	@Override
	public Group getClubByUniqueId(String uniqueId, List<Group> leagues) {
		try {
			if (ListUtil.isEmpty(leagues)) {
				return null;
			}
			String sportUnion = getSportUnion();
			if (StringUtil.isEmpty(sportUnion)) {
				return null;
			}

			GroupBusiness groupBiz = getServiceInstance(GroupBusiness.class);
			Group club = groupBiz.getGroupByUniqueId(uniqueId);
			if (club == null) {
				return null;
			}

			String type = club.getType();
			if (!IWMemberConstants.GROUP_TYPE_CLUB.equals(type)) {
				getLogger().warning("Expected type '" + IWMemberConstants.GROUP_TYPE_CLUB + "', got: '" + type + "' for group's UID: " + uniqueId);
				return null;
			}

			for (Group league: leagues) {
				if (sportUnion.equals(league.getName())) {
					return club;
				}
			}
		} catch (Exception e) {}
		return null;
	}

	@Override
	public Group getClubByUniqueIdAndLeaguesName(String uniqueId, List<String> leaguesNames) {
		try {
			if (ListUtil.isEmpty(leaguesNames)) {
				return null;
			}
			String sportUnion = getSportUnion();
			if (StringUtil.isEmpty(sportUnion)) {
				return null;
			}

			GroupBusiness groupBiz = getServiceInstance(GroupBusiness.class);
			Group club = groupBiz.getGroupByUniqueId(uniqueId);
			if (club == null) {
				return null;
			}

			String type = club.getType();
			if (!IWMemberConstants.GROUP_TYPE_CLUB.equals(type)) {
				getLogger().warning("Expected type '" + IWMemberConstants.GROUP_TYPE_CLUB + "', got: '" + type + "' for group's UID: " + uniqueId);
				return null;
			}

			for (String leagueName: leaguesNames) {
				if (sportUnion.equals(leagueName)) {
					return club;
				}
			}
		} catch (Exception e) {}
		return null;
	}

}