package is.idega.idegaweb.member.sync.impl;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.dao.GroupDAO;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.user.sync.UnionMemberSynchronizer;
import com.idega.util.ListUtil;
import com.idega.util.StringUtil;
import com.idega.util.datastructures.map.MapUtil;

import is.idega.idegaweb.member.util.IWMemberConstants;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class MotorsportUnionMemberSynchronizer extends DefaultUnionMemberSynchronizer {

	@Autowired
	private GroupDAO groupDAO;

	private com.idega.user.data.bean.Group getFirstPlayersGroup(Integer id) throws Exception {
		Map<Integer, List<com.idega.user.data.bean.Group>> playerGroups = groupDAO.getChildGroups(Arrays.asList(id), Arrays.asList(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER), null);
		if (MapUtil.isEmpty(playerGroups)) {
			return null;
		}

		for (List<com.idega.user.data.bean.Group> groups: playerGroups.values()) {
			if (ListUtil.isEmpty(groups)) {
				continue;
			}

			return groups.get(0);
		}

		return null;
	}

	@Override
	public String registerMemberToClub(String ssn, Group club, String clubMembershipType) throws RemoteException {
		if (StringUtil.isEmpty(ssn) || club == null) {
			return null;
		}

		try {
			com.idega.user.data.bean.Group playersGroup = getFirstPlayersGroup(Integer.valueOf(club.getId()));
			if (playersGroup == null) {
				getLogger().warning("Failed to find players group for " + club.getName() + ", ID: " + club.getId());
				return null;
			}

			UserBusiness userBusiness = getServiceInstance(UserBusiness.class);
			GroupBusiness groupBusiness = getServiceInstance(GroupBusiness.class);
			groupBusiness.addUser(playersGroup.getID(), userBusiness.getUser(ssn));

			return UnionMemberSynchronizer.WS_MSG_SUCCESS;
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error registering members (personal ID: " + ssn + ") into club " + club, e);
		}

		return null;
	}

	@Override
	public String disableMemberInClub(String ssn, Group club) throws RemoteException {
		if (StringUtil.isEmpty(ssn) || club == null) {
			return null;
		}

		try {
			Integer id = Integer.valueOf(club.getId());
			UserBusiness userBusiness = getServiceInstance(UserBusiness.class);
			User user = userBusiness.getUser(ssn);
			User currentUser = getLegacyUser(getCurrentUser());
			GroupBusiness groupBusiness = getServiceInstance(GroupBusiness.class);

			//	Removing from players group(s)
			Map<Integer, List<com.idega.user.data.bean.Group>> playerGroups = groupDAO.getChildGroups(Arrays.asList(id), Arrays.asList(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER), null);
			if (MapUtil.isEmpty(playerGroups)) {
				return null;
			}
			for (List<com.idega.user.data.bean.Group> groups: playerGroups.values()) {
				if (ListUtil.isEmpty(groups)) {
					continue;
				}

				for (com.idega.user.data.bean.Group group: groups) {
					Group legacyGroup = groupBusiness.getGroupByGroupID(group.getID());
					userBusiness.removeUserFromGroup(user, legacyGroup, currentUser);
				}
			}

			//	Adding to general members group
			Map<Integer, List<com.idega.user.data.bean.Group>> generalGroups = groupDAO.getChildGroups(Arrays.asList(id), Arrays.asList(IWMemberConstants.GROUP_TYPE_GENERAL), null);
			if (MapUtil.isEmpty(generalGroups)) {
				return null;
			}
			for (List<com.idega.user.data.bean.Group> groups: generalGroups.values()) {
				if (ListUtil.isEmpty(groups)) {
					continue;
				}

				com.idega.user.data.bean.Group group = groups.get(0);
				groupBusiness.addUser(group.getID(), user);
				return UnionMemberSynchronizer.WS_MSG_SUCCESS;
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error disabling membership for member (personal ID: " + ssn + ") in club " + club, e);
		}

		return null;
	}

	@Override
	public String getSportUnion() {
		return getApplicationProperty("felix_synchronizer.motorsport_union", "MSÍ");
	}

}