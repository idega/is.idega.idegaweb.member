package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_CLUB_PLAYER_TEMPLATE)
public class MemberClubPlayerTemplateGroup extends Group {

	private static final long serialVersionUID = -5338782737710868872L;

}