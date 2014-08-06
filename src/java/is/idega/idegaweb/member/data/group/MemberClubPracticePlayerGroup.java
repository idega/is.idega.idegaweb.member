package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_CLUB_PRACTICE_PLAYER)
public class MemberClubPracticePlayerGroup extends Group {

	private static final long serialVersionUID = 2411229922479519231L;

}