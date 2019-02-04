package is.idega.idegaweb.member.data.group;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

import is.idega.idegaweb.member.util.IWMemberConstants;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_CLUB_HONORARY_GROUP)
public class MemberClubHonoraryGroup extends Group {

	private static final long serialVersionUID = -9007426321916110561L;

}