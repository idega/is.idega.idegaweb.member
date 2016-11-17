package is.idega.idegaweb.member.data.group;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

import is.idega.idegaweb.member.util.IWMemberConstants;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_UNION_TRAINER)
public class MemberUnionTrainerGroup extends Group {
	private static final long serialVersionUID = 485311692674441117L;



}