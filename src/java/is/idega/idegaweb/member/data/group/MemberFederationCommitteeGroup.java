package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_FEDERATION_COMMITTEE)
public class MemberFederationCommitteeGroup extends Group {

	private static final long serialVersionUID = 401169380693793639L;

}
