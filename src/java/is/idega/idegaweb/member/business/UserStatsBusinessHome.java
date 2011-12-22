package is.idega.idegaweb.member.business;


import javax.ejb.CreateException;
import java.rmi.RemoteException;
import com.idega.business.IBOHome;

public interface UserStatsBusinessHome extends IBOHome {
	public UserStatsBusiness create() throws CreateException, RemoteException;
}