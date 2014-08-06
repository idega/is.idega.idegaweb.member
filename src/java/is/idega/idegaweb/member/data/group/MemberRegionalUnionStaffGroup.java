package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION_STAFF)
public class MemberRegionalUnionStaffGroup extends Group {

	private static final long serialVersionUID = -84063757838500425L;

}
