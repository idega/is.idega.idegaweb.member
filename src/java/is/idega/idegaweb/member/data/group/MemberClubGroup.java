package is.idega.idegaweb.member.data.group;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.GroupTypeConstants;
import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(GroupTypeConstants.GROUP_TYPE_CLUB)
public class MemberClubGroup extends Group {

	private static final long serialVersionUID = 1242788711956990791L;

}