package is.idega.idegaweb.member.sync.impl;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.HashMap;
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

	private com.idega.user.data.bean.Group getPlayerGroupByUniqueId(String uniqueId) {
		if (StringUtil.isEmpty(uniqueId)) {
			return null;
		}

		Group group = null;
		try {
			GroupBusiness groupBusiness = getServiceInstance(GroupBusiness.class);
			group = groupBusiness.getGroupByUniqueId(uniqueId);
		} catch (Exception e) {}
		if (group == null) {
			return null;
		}

		try {
			com.idega.user.data.bean.Group playersGroup = groupDAO.findGroup((Integer) group.getPrimaryKey());
			if (playersGroup == null) {
				return null;
			}
			if (IWMemberConstants.GROUP_TYPE_CLUB_PLAYER.equals(playersGroup.getType())) {
				return playersGroup;
			}
		} catch (Exception e) {}
		return null;
	}

	private com.idega.user.data.bean.Group getFirstPlayersGroup(Integer id, String uniqueId) throws Exception {
		com.idega.user.data.bean.Group playersGroup = getPlayerGroupByUniqueId(uniqueId);
		if (playersGroup != null) {
			return playersGroup;
		}

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
	public String registerMemberToClub(String ssn, Group club, String clubMembershipType, String uniqueId) throws RemoteException {
		if (StringUtil.isEmpty(ssn) || club == null) {
			return null;
		}

		try {
			com.idega.user.data.bean.Group playersGroup = getFirstPlayersGroup(Integer.valueOf(club.getId()), uniqueId);
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
	public String disableMemberInClub(String ssn, Group club, String groupUniqueId) throws RemoteException {
		if (StringUtil.isEmpty(ssn)) {
			getLogger().warning("Personal ID is not provided");
			return null;
		}

		try {
			Integer id = club == null ? null : Integer.valueOf(club.getId());
			UserBusiness userBusiness = getServiceInstance(UserBusiness.class);
			User user = userBusiness.getUser(ssn);
			User currentUser = getLegacyUser(getCurrentUser());
			GroupBusiness groupBusiness = getServiceInstance(GroupBusiness.class);

			//	Removing from players group(s)
			Map<Integer, List<com.idega.user.data.bean.Group>> playersGroups = null;
			if (!StringUtil.isEmpty(groupUniqueId)) {
				com.idega.user.data.bean.Group playersGroup = getPlayerGroupByUniqueId(groupUniqueId);
				if (playersGroup != null && userBusiness.isMemberOfGroup(playersGroup.getID(), user)) {
					playersGroups = new HashMap<>();
					playersGroups.put(playersGroup.getID(), Arrays.asList(playersGroup));
				}
			}
			if (MapUtil.isEmpty(playersGroups) && id != null) {
				getLogger().info("Did not find player's group by unique ID: " + groupUniqueId);
				playersGroups = groupDAO.getChildGroups(Arrays.asList(id), Arrays.asList(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER), null);
			}
			if (MapUtil.isEmpty(playersGroups)) {
				getLogger().warning("No players groups found in club " + club + " nor by group's unique ID: " + groupUniqueId + ", unable to remove member with personal ID " + ssn);
				return null;
			}

			for (List<com.idega.user.data.bean.Group> groups: playersGroups.values()) {
				if (ListUtil.isEmpty(groups)) {
					continue;
				}

				for (com.idega.user.data.bean.Group group: groups) {
					Group legacyGroup = groupBusiness.getGroupByGroupID(group.getID());
					userBusiness.removeUserFromGroup(user, legacyGroup, currentUser);
					getLogger().info("Removed " + user + " from group " + legacyGroup + " (name: " + legacyGroup.getName() + ", unique ID: " + legacyGroup.getUniqueId() + ")");
				}
			}

			if (id != null && getApplication().getSettings().getBoolean("on_membership_closed_to_general", false)) {
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
			} else {
				return UnionMemberSynchronizer.WS_MSG_SUCCESS;
			}
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "Error disabling membership for member (personal ID: " + ssn + ") in club " + club + ", group's unique ID: " + groupUniqueId, e);
		}

		return null;
	}

	@Override
	public String getSportUnion() {
		return getApplicationProperty("felix_synchronizer.motorsport_union", "MSÍ");
	}

}