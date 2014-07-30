package is.idega.idegaweb.member.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class UserStatsNoThreadBusinessHomeImpl extends IBOHomeImpl implements
		UserStatsNoThreadBusinessHome {
	public Class getBeanInterfaceClass() {
		return UserStatsNoThreadBusiness.class;
	}

	public UserStatsNoThreadBusiness create() throws CreateException {
		return (UserStatsNoThreadBusiness) super.createIBO();
	}
}