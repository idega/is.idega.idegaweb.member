/*
 * Created on Jan 20, 2005
 */
package is.idega.idegaweb.member.business;

import is.idega.idegaweb.member.presentation.GroupStatsNoThreadWindowPlugin;
import is.idega.idegaweb.member.presentation.UserStatsNoThreadWindowPlugin;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author Sigtryggur
 * 
 */
public class UserStatsNoThreadBusinessBean extends IBOSessionBean implements
		UserStatsNoThreadBusiness, UserGroupPlugInBusiness {

	private static final long serialVersionUID = -5458084870941206455L;

	private UserBusiness userBiz = null;
	private UserStatsBusiness userStatsBiz = null;

	public ReportableCollection getStatisticsForUsers(String groupIDFilter,
			String groupsRecursiveFilter, Collection groupTypesFilter,
			Collection userStatusesFilter, Integer yearOfBirthFromFilter,
			Integer yearOfBirthToFilter, String genderFilter,
			Collection postalCodeFilter, String dynamicLayout, String orderBy,
			String doOrderFilter) throws RemoteException {

		Locale currentLocale = this.getUserContext().getCurrentLocale();
		boolean isSuperAdmin = isSuperAdmin();
		
		Collection topNodes = getUserBusiness()
				.getUsersTopGroupNodesByViewAndOwnerPermissions(
						getUserContext().getCurrentUser(), getUserContext());
		
		return getUserStatsBusiness().getStatisticsForUsers(groupIDFilter, groupsRecursiveFilter, groupTypesFilter, userStatusesFilter, yearOfBirthFromFilter, yearOfBirthToFilter, genderFilter, 
				postalCodeFilter, dynamicLayout, orderBy, doOrderFilter, "", "", "", "", "", "", "", currentLocale, isSuperAdmin, this.getUserContext().getCurrentUser(), topNodes);
	}

	public ReportableCollection getStatisticsForGroups(String groupIDFilter,
			String groupsRecursiveFilter, Collection groupTypesFilter,
			String dynamicLayout, String orderBy, String doOrderFilter) throws RemoteException {

		Locale currentLocale = this.getUserContext().getCurrentLocale();
		boolean isSuperAdmin = isSuperAdmin();
		
		Collection topNodes = getUserBusiness()
				.getUsersTopGroupNodesByViewAndOwnerPermissions(
						getUserContext().getCurrentUser(), getUserContext());
		
		return getUserStatsBusiness().getStatisticsForGroups(groupIDFilter, groupsRecursiveFilter, groupTypesFilter, dynamicLayout, orderBy, doOrderFilter, "", "", "", "", "", "", "", currentLocale, isSuperAdmin, this.getUserContext().getCurrentUser(), topNodes);
	}

	private UserStatsBusiness getUserStatsBusiness() throws RemoteException {
		if (this.userStatsBiz == null) {
			this.userStatsBiz = (UserStatsBusiness) IBOLookup
					.getServiceInstance(this.getIWApplicationContext(),
							UserStatsBusiness.class);
		}
		return this.userStatsBiz;
	}

	private UserBusiness getUserBusiness() throws RemoteException {
		if (this.userBiz == null) {
			this.userBiz = (UserBusiness) IBOLookup.getServiceInstance(this
					.getIWApplicationContext(), UserBusiness.class);
		}
		return this.userBiz;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList(1);
		list.add(new UserStatsNoThreadWindowPlugin());
		list.add(new GroupStatsNoThreadWindowPlugin());
		return list;
	}

	public void afterGroupCreateOrUpdate(Group group, Group parentGroup)
			throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	public void afterUserCreateOrUpdate(User user, Group parentGroup)
			throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	public void beforeGroupRemove(Group group, Group parentGroup)
			throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	public void beforeUserRemove(User user, Group parentGroup)
			throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	public String canCreateSubGroup(Group parentGroup,
			String groupTypeOfSubGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupToolbarElements(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserPropertiesTabs(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public PresentationObject instanciateEditor(Group group)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public PresentationObject instanciateViewer(Group group)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String isUserAssignableFromGroupToGroup(User user,
			Group sourceGroup, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String isUserSuitedForGroup(User user, Group targetGroup)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

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