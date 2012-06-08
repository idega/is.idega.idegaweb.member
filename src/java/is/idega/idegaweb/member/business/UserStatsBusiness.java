package is.idega.idegaweb.member.business;


import javax.ejb.RemoveException;
import java.rmi.RemoteException;
import com.idega.block.datareport.util.ReportableCollection;
import java.util.List;
import com.idega.business.IBOService;
import com.idega.user.data.Group;
import javax.ejb.CreateException;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.User;
import java.util.Locale;
import com.idega.presentation.PresentationObject;
import java.util.Collection;

public interface UserStatsBusiness extends IBOService, UserGroupPlugInBusiness {
	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getStatisticsForUsers
	 */
	public ReportableCollection getStatisticsForUsers(String groupIDFilter,
			String groupsRecursiveFilter, Collection groupTypesFilter,
			Collection userStatusesFilter, Integer yearOfBirthFromFilter,
			Integer yearOfBirthToFilter, String genderFilter,
			Collection postalCodeFilter, String dynamicLayout, String orderBy,
			String doOrderFilter, String runAsThread, String sendToEmail,
			String excel, String excelNoStylesheet, String pdf, String xml,
			String html, Locale currentLocale, Boolean isSuperAdmin,
			User currentUser, Collection sessionTopNodes, Collection groups, Group group)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getStatisticsForGroups
	 */
	public ReportableCollection getStatisticsForGroups(String groupIDFilter,
			String groupsRecursiveFilter, Collection groupTypesFilter,
			String dynamicLayout, String orderBy, String doOrderFilter,
			String runAsThread, String sendToEmail, String excel,
			String excelNoStylesheet, String pdf, String xml, String html,
			Locale currentLocale, Boolean isSuperAdmin, User currentUser,
			Collection sessionTopNodes, Collection groups, Group topGroup) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User user, Group parentGroup)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group group, Group parentGroup)
			throws RemoveException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User user, Group parentGroup)
			throws RemoveException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#canCreateSubGroup
	 */
	public String canCreateSubGroup(Group parentGroup,
			String groupTypeOfSubGroup) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group group) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group group) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User user) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group group)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group group)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User user,
			Group sourceGroup, Group targetGroup) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User user, Group targetGroup)
			throws RemoteException, RemoteException;
}