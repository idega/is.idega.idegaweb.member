package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_FEDERATION_STAFF)
public class MemberFederationStaffGroup extends Group {

	private static final long serialVersionUID = -3337902915612437490L;

}
