package is.idega.idegaweb.member.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class UserStatsBusinessHomeImpl extends IBOHomeImpl implements
		UserStatsBusinessHome {
	@Override
	public Class<UserStatsBusiness> getBeanInterfaceClass() {
		return UserStatsBusiness.class;
	}

	public UserStatsBusiness create() throws CreateException {
		return (UserStatsBusiness) super.createIBO();
	}
}