package is.idega.idegaweb.member.business.plugins;

import is.idega.idegaweb.member.presentation.UserGroupImageTab;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;

import com.idega.business.IBOServiceBean;
import com.idega.presentation.PresentationObject;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

public class UserGroupImageBusinessBean extends IBOServiceBean implements
		UserGroupImageBusiness, UserGroupPlugInBusiness {

	public void afterGroupCreateOrUpdate(Group arg0, Group arg1)
			throws CreateException, RemoteException {
		// TODO Auto-generated method stub

	}

	public void afterUserCreateOrUpdate(User arg0, Group arg1)
			throws CreateException, RemoteException {
		// TODO Auto-generated method stub

	}

	public void beforeGroupRemove(Group arg0, Group arg1)
			throws RemoveException, RemoteException {
		// TODO Auto-generated method stub

	}

	public void beforeUserRemove(User arg0, Group arg1) throws RemoveException,
			RemoteException {
		// TODO Auto-generated method stub

	}

	public String canCreateSubGroup(Group arg0, String arg1)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupPropertiesTabs(Group arg0) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getGroupToolbarElements(Group arg0) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getMainToolbarElements() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getUserPropertiesTabs(User arg0) throws RemoteException {
		List list = new ArrayList();
		list.add(new UserGroupImageTab());

		return list;
	}

	public PresentationObject instanciateEditor(Group arg0)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public PresentationObject instanciateViewer(Group arg0)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String isUserAssignableFromGroupToGroup(User arg0, Group arg1,
			Group arg2) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public String isUserSuitedForGroup(User arg0, Group arg1)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
