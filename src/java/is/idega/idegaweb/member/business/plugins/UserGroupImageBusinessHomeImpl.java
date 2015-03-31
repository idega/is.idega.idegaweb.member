package is.idega.idegaweb.member.business.plugins;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class UserGroupImageBusinessHomeImpl extends IBOHomeImpl implements
		UserGroupImageBusinessHome {
	public Class getBeanInterfaceClass() {
		return UserGroupImageBusiness.class;
	}

	public UserGroupImageBusiness create() throws CreateException {
		return (UserGroupImageBusiness) super.createIBO();
	}
}