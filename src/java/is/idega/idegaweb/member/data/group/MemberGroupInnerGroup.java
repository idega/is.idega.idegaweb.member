package is.idega.idegaweb.member.data.group;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

import is.idega.idegaweb.member.util.IWMemberConstants;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_GROUP_INNER)
public class MemberGroupInnerGroup extends Group {
	private static final long serialVersionUID = -6702068286956489354L;


}
