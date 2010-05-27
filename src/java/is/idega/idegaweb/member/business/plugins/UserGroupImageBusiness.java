package is.idega.idegaweb.member.business.plugins;


import javax.ejb.RemoveException;
import javax.ejb.CreateException;
import java.rmi.RemoteException;
import java.util.List;
import com.idega.user.data.User;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.business.IBOService;
import com.idega.presentation.PresentationObject;
import com.idega.user.data.Group;

public interface UserGroupImageBusiness extends IBOService,
		UserGroupPlugInBusiness {
	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#afterGroupCreateOrUpdate
	 */
	public void afterGroupCreateOrUpdate(Group arg0, Group arg1)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#afterUserCreateOrUpdate
	 */
	public void afterUserCreateOrUpdate(User arg0, Group arg1)
			throws CreateException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#beforeGroupRemove
	 */
	public void beforeGroupRemove(Group arg0, Group arg1)
			throws RemoveException, RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#beforeUserRemove
	 */
	public void beforeUserRemove(User arg0, Group arg1) throws RemoveException,
			RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#canCreateSubGroup
	 */
	public String canCreateSubGroup(Group arg0, String arg1)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#getGroupPropertiesTabs
	 */
	public List getGroupPropertiesTabs(Group arg0) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#getGroupToolbarElements
	 */
	public List getGroupToolbarElements(Group arg0) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#getMainToolbarElements
	 */
	public List getMainToolbarElements() throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#getUserPropertiesTabs
	 */
	public List getUserPropertiesTabs(User arg0) throws RemoteException,
			RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#instanciateEditor
	 */
	public PresentationObject instanciateEditor(Group arg0)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#instanciateViewer
	 */
	public PresentationObject instanciateViewer(Group arg0)
			throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#isUserAssignableFromGroupToGroup
	 */
	public String isUserAssignableFromGroupToGroup(User arg0, Group arg1,
			Group arg2) throws RemoteException, RemoteException;

	/**
	 * @see is.idega.idegaweb.member.business.plugins.UserGroupImageBusinessBean#isUserSuitedForGroup
	 */
	public String isUserSuitedForGroup(User arg0, Group arg1)
			throws RemoteException, RemoteException;
}