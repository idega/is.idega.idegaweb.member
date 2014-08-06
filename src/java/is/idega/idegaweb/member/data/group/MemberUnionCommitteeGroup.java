package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_UNION_COMMITTEE)
public class MemberUnionCommitteeGroup extends Group {

	private static final long serialVersionUID = 1691728397435901950L;

}
