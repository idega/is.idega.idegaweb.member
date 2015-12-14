/*
 * Created on Jan 20, 2005
 */
package is.idega.idegaweb.member.business;

import is.idega.idegaweb.member.presentation.GroupStatsNoThreadWindowPlugin;
import is.idega.idegaweb.member.presentation.UserStatsNoThreadWindowPlugin;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.core.accesscontrol.business.AccessController;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.ListUtil;
import com.idega.util.StringHandler;

/**
 * @author Sigtryggur
 *
 */
public class UserStatsNoThreadBusinessBean extends IBOSessionBean implements
		UserStatsNoThreadBusiness, UserGroupPlugInBusiness {

	private static final long serialVersionUID = -5458084870941206455L;

	private UserBusiness userBiz = null;
	private GroupBusiness groupBiz = null;
	private UserStatsBusiness userStatsBiz = null;

	private boolean hasViewPermission(User user, Group group, Collection<Group> topNodes) {
		AccessController accessController = this.getAccessController();

		boolean isCurrentUserSuperAdmin = this.isSuperAdmin();

		boolean hasViewPermissionForRealGroup = isCurrentUserSuperAdmin;
		//boolean hasEditPermissionForRealGroup = isCurrentUserSuperAdmin;
		//boolean hasDeletePermissionForRealGroup = isCurrentUserSuperAdmin;
		boolean hasOwnerPermissionForRealGroup = isCurrentUserSuperAdmin;
		boolean hasPermitPermissionForRealGroup = isCurrentUserSuperAdmin;

		try {
			if (!isCurrentUserSuperAdmin) {
				if (group.getAlias() != null) {// thats the real group
					hasOwnerPermissionForRealGroup = accessController.isOwner(
							group.getAlias(), this.getUserContext());
					if (!hasOwnerPermissionForRealGroup) {
						hasViewPermissionForRealGroup = accessController
								.hasViewPermissionFor(group.getAlias(),
										this.getUserContext());
						//hasEditPermissionForRealGroup = accessController
						//		.hasEditPermissionFor(group.getAlias(),
						//				this.getUserContext());
						//hasDeletePermissionForRealGroup = accessController
						//		.hasDeletePermissionFor(group.getAlias(),
						//				this.getUserContext());
						hasPermitPermissionForRealGroup = accessController
								.hasPermitPermissionFor(group.getAlias(),
										this.getUserContext());
					} else {
						// the user is the owner so he can do anything
						hasViewPermissionForRealGroup = true;
						//hasEditPermissionForRealGroup = true;
						//hasDeletePermissionForRealGroup = true;
						hasPermitPermissionForRealGroup = true;
					}
				} else if (group != null) {
					hasOwnerPermissionForRealGroup = accessController.isOwner(
							group, this.getUserContext());
					if (!hasOwnerPermissionForRealGroup) {
						hasViewPermissionForRealGroup = accessController
								.hasViewPermissionFor(group,
										this.getUserContext());
						//hasEditPermissionForRealGroup = accessController
						//		.hasEditPermissionFor(group,
						//				this.getUserContext());
						//hasDeletePermissionForRealGroup = accessController
						//		.hasDeletePermissionFor(group,
						//				this.getUserContext());
						hasPermitPermissionForRealGroup = accessController
								.hasPermitPermissionFor(group,
										this.getUserContext());
					} else {
						// the user is the owner so he can do anything
						hasViewPermissionForRealGroup = true;
						//hasEditPermissionForRealGroup = true;
						//hasDeletePermissionForRealGroup = true;
						hasPermitPermissionForRealGroup = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean result = hasViewPermissionForRealGroup || hasPermitPermissionForRealGroup;
		if (!result && !ListUtil.isEmpty(topNodes)) {
			result = hasGroupUnderTopNodes(group, topNodes);
		}

		return result;
	}

	private boolean hasGroupUnderTopNodes(Group group, Collection<Group> groups) {
		if (group == null || ListUtil.isEmpty(groups)) {
			return false;
		}

		if (groups.contains(group)) {
			return true;
		}

		for (Group topNode: groups) {
			try {
				Collection<Group> childGroups = getGroupBusiness().getChildGroupsRecursive(topNode, null, false);
				if (!ListUtil.isEmpty(childGroups) && childGroups.contains(group)) {
					return true;
				} else {
					getLogger().log(Level.WARNING, group + " is not among child groups (" + childGroups + ") for " + topNode);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public ReportableCollection getStatisticsForUsers(
			String groupIDFilter,
			String groupsRecursiveFilter,
			Collection groupTypesFilter,
			Collection userStatusesFilter,
			Integer yearOfBirthFromFilter,
			Integer yearOfBirthToFilter,
			String genderFilter,
			Collection postalCodeFilter,
			String dynamicLayout,
			String orderBy,
			String doOrderFilter
	) throws RemoteException {
		try {
			throw new RuntimeException("Testing stacktrace");
		} catch (Exception e) {
			getLogger().log(Level.WARNING, e.getLocalizedMessage(), e);
		}
		getLogger().info("groupIDFilter: " + groupIDFilter + ", groupsRecursiveFilter: " + groupsRecursiveFilter + ", groupTypesFilter: " +
				groupTypesFilter + ", userStatusesFilter: " + userStatusesFilter + ", yearOfBirthFromFilter: " + yearOfBirthFromFilter +
				", yearOfBirthToFilter: " + yearOfBirthToFilter + ", genderFilter: " + genderFilter + ", postalCodeFilter: " + postalCodeFilter +
				", dynamicLayout: " + dynamicLayout + ", orderBy: " + orderBy + ", doOrderFilter: " + doOrderFilter);

		Locale currentLocale = this.getUserContext().getCurrentLocale();
		boolean isSuperAdmin = isSuperAdmin();

		User user = getUserContext().getCurrentUser();
		Collection<Group> topNodes = getUserBusiness().getUsersTopGroupNodesByViewAndOwnerPermissions(user, getUserContext());
		getLogger().info("Top nodes: " + topNodes);

		Group group = null;
		Collection<Group> groups = null;

		try {
			if (groupIDFilter != null && !groupIDFilter.equals("")) {
				if (!StringHandler.isNumeric(groupIDFilter)) {
					groupIDFilter = groupIDFilter.indexOf("_") == -1 ? groupIDFilter : groupIDFilter.substring(groupIDFilter.lastIndexOf("_") + 1);
				}
				group = getGroupBusiness().getGroupByGroupID(Integer.parseInt((groupIDFilter)));
				if (group.isAlias()) {
					group = group.getAlias();
				}
			}
			getLogger().info("GROUP: " + group);
			if (group != null) {
				if (groupsRecursiveFilter != null && groupsRecursiveFilter.equals("checked")) {
					getLogger().info("Get groups by filter: " + groupTypesFilter);
					groups = getGroupBusiness().getChildGroupsRecursiveResultFiltered(group, groupTypesFilter, true, true, true);
					getLogger().info("Got groups by filter: " + groupTypesFilter + ": " + groups);
				} else {
					getLogger().info("No filter");
					groups = new ArrayList<Group>();
				}
				groups.add(group);

				Iterator<Group> it = groups.iterator();
				Collection<Group> viewGroups = new ArrayList<Group>();
				while (it.hasNext()) {
					Group g = it.next();
					if (hasViewPermission(user, g, topNodes)) {
						viewGroups.add(g);
					} else {
						getLogger().warning("No permission for " + user + " on " + g);
					}
				}

				getLogger().info("View groups: " + viewGroups);
				groups = viewGroups;
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return getUserStatsBusiness().getStatisticsForUsers(groupIDFilter,
				groupsRecursiveFilter, groupTypesFilter, userStatusesFilter,
				yearOfBirthFromFilter, yearOfBirthToFilter, genderFilter,
				postalCodeFilter, dynamicLayout, orderBy, doOrderFilter, "",
				"", "", "", "", "", "", currentLocale, Boolean.valueOf(isSuperAdmin),
				user, topNodes, groups, group);
	}

	@Override
	public ReportableCollection getStatisticsForGroups(String groupIDFilter,
			String groupsRecursiveFilter, Collection groupTypesFilter,
			String dynamicLayout, String orderBy, String doOrderFilter)
			throws RemoteException {

		Locale currentLocale = this.getUserContext().getCurrentLocale();
		boolean isSuperAdmin = isSuperAdmin();

		Group topGroup = null;
		Collection groups = null;
		try {
			if (groupIDFilter != null && !groupIDFilter.equals("")) {
				groupIDFilter = groupIDFilter.substring(groupIDFilter
						.lastIndexOf("_") + 1);
				topGroup = getGroupBusiness().getGroupByGroupID(
						Integer.parseInt((groupIDFilter)));
			}
			if (topGroup != null) {
				if (groupsRecursiveFilter != null
						&& groupsRecursiveFilter.equals("checked")) {
					groups = getGroupBusiness()
							.getChildGroupsRecursiveResultFiltered(topGroup,
									groupTypesFilter, true, true, false);
				} else {
					groups = new ArrayList();
					groups.add(topGroup);
				}

				Collection<Group> topNodes = getUserBusiness().getUsersTopGroupNodesByViewAndOwnerPermissions(this.getUserContext().getCurrentUser(), getUserContext());

				Iterator it = groups.iterator();
				Collection viewGroups = new ArrayList();
				while (it.hasNext()) {
					Group g = (Group) it.next();
					if (hasViewPermission(this.getUserContext().getCurrentUser(), g, topNodes))
						viewGroups.add(g);
				}

				groups = viewGroups;

			}
		} catch (FinderException e) {
			e.printStackTrace();
		}


		Collection topNodes = getUserBusiness()
				.getUsersTopGroupNodesByViewAndOwnerPermissions(
						getUserContext().getCurrentUser(), getUserContext());

		return getUserStatsBusiness().getStatisticsForGroups(groupIDFilter,
				groupsRecursiveFilter, groupTypesFilter, dynamicLayout,
				orderBy, doOrderFilter, "", "", "", "", "", "", "",
				currentLocale, Boolean.valueOf(isSuperAdmin),
				this.getUserContext().getCurrentUser(), topNodes, groups, topGroup);
	}

	private UserStatsBusiness getUserStatsBusiness() throws RemoteException {
		if (this.userStatsBiz == null) {
			this.userStatsBiz = IBOLookup
					.getServiceInstance(this.getIWApplicationContext(),
							UserStatsBusiness.class);
		}
		return this.userStatsBiz;
	}

	private UserBusiness getUserBusiness() throws RemoteException {
		if (this.userBiz == null) {
			this.userBiz = IBOLookup.getServiceInstance(
					this.getIWApplicationContext(), UserBusiness.class);
		}
		return this.userBiz;
	}

	private GroupBusiness getGroupBusiness() throws RemoteException {
		if (this.groupBiz == null) {
			this.groupBiz = IBOLookup.getServiceInstance(
					this.getIWApplicationContext(), GroupBusiness.class);
		}
		return this.groupBiz;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	@Override
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList(1);
		list.add(new UserStatsNoThreadWindowPlugin());
		list.add(new GroupStatsNoThreadWindowPlugin());
		return list;
	}

	@Override
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup)
			throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterUserCreateOrUpdate(User user, Group parentGroup)
			throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeGroupRemove(Group group, Group parentGroup)
			throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeUserRemove(User user, Group parentGroup)
			throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public String canCreateSubGroup(Group parentGroup,
			String groupTypeOfSubGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getGroupToolbarElements(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserPropertiesTabs(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PresentationObject instanciateEditor(Group group)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PresentationObject instanciateViewer(Group group)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isUserAssignableFromGroupToGroup(User user,
			Group sourceGroup, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isUserSuitedForGroup(User user, Group targetGroup)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSuperAdmin() {
		boolean isSuperAdmin = false;
		try {
			if (this.getCurrentUser().equals(
					this.getAccessController().getAdministratorUser())) {
				isSuperAdmin = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isSuperAdmin;
	}
}