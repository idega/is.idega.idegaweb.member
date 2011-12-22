package is.idega.idegaweb.member.business;


import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.user.data.User;
import java.util.List;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.presentation.PresentationObject;
import java.util.Collection;
import com.idega.business.IBOSession;
import com.idega.user.data.Group;

public interface UserStatsNoThreadBusiness extends IBOSession,
		UserGroupPlugInBusiness {
	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#getStatisticsForUsers
	 */
	public ReportableCollection getStatisticsForUsers(String groupIDFilter,
			String groupsRecursiveFilter, Collection groupTypesFilter,
			Collection userStatusesFilter, Integer yearOfBirthFromFilter,
			Integer yearOfBirthToFilter, String genderFilter,
			Collection postalCodeFilter, String dynamicLayout, String orderBy,
			String doOrderFilter) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#getStatisticsForGroups
	 */
	public ReportableCollection getStatisticsForGroups(String groupIDFilter,
			String groupsRecursiveFilter, Collection groupTypesFilter,
			String dynamicLayout, String orderBy, String doOrderFilter)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group, Group parentGroup)
			throws RemoveException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user, Group parentGroup)
			throws RemoveException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#canCreateSubGroup
	 */
	public String canCreateSubGroup(Group parentGroup,
			String groupTypeOfSubGroup) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user,
			Group sourceGroup, Group targetGroup) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsNoThreadBusinessBean#isSuperAdmin
	 */
	public boolean isSuperAdmin() throws RemoteException;
}