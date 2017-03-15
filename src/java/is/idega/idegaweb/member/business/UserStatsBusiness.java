package is.idega.idegaweb.member.business;


import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.block.datareport.util.ReportableCollection;
import com.idega.business.IBOService;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

public interface UserStatsBusiness extends IBOService, UserGroupPlugInBusiness {
	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getStatisticsForUsers
	 */
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
			String doOrderFilter,
			String runAsThread,
			String sendToEmail,
			String excel,
			String excelNoStylesheet,
			String pdf,
			String xml,
			String html,
			Locale currentLocale,
			Boolean isSuperAdmin,
			User currentUser,
			Collection<Group> sessionTopNodes,
			Collection groups,
			Group group
	) throws RemoteException, RemoteException;

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
	@Override
	public List getMainToolbarElements() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#afterGroupCreateOrUpdate
	 */
	@Override
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#afterUserCreateOrUpdate
	 */
	@Override
	public void afterUserCreateOrUpdate(User user, Group parentGroup)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#beforeGroupRemove
	 */
	@Override
	public void beforeGroupRemove(Group group, Group parentGroup)
			throws RemoveException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#beforeUserRemove
	 */
	@Override
	public void beforeUserRemove(User user, Group parentGroup)
			throws RemoveException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#canCreateSubGroup
	 */
	@Override
	public String canCreateSubGroup(Group parentGroup,
			String groupTypeOfSubGroup) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getGroupPropertiesTabs
	 */
	@Override
	public List getGroupPropertiesTabs(Group group) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getGroupToolbarElements
	 */
	@Override
	public List getGroupToolbarElements(Group group) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#getUserPropertiesTabs
	 */
	@Override
	public List getUserPropertiesTabs(User user) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#instanciateEditor
	 */
	@Override
	public PresentationObject instanciateEditor(Group group)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#instanciateViewer
	 */
	@Override
	public PresentationObject instanciateViewer(Group group)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#isUserAssignableFromGroupToGroup
	 */
	@Override
	public String isUserAssignableFromGroupToGroup(User user,
			Group sourceGroup, Group targetGroup) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.UserStatsBusinessBean#isUserSuitedForGroup
	 */
	@Override
	public String isUserSuitedForGroup(User user, Group targetGroup)
			throws RemoteException, RemoteException;
}