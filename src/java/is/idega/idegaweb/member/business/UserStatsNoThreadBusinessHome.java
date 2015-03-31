package is.idega.idegaweb.member.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface UserStatsNoThreadBusinessHome extends IBOHome {
	public UserStatsNoThreadBusiness create() throws CreateException,
			RemoteException;
}