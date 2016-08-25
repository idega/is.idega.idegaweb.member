package is.idega.idegaweb.member.data.group;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

import is.idega.idegaweb.member.util.IWMemberConstants;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_INNER)
public class MemberClubPlayerInnerGroup extends Group {

	private static final long serialVersionUID = 3111371997937395757L;

}