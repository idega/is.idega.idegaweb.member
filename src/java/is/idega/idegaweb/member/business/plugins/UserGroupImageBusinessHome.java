package is.idega.idegaweb.member.business.plugins;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface UserGroupImageBusinessHome extends IBOHome {
	public UserGroupImageBusiness create() throws CreateException,
			RemoteException;
}