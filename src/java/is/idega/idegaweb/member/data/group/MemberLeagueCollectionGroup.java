package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_LEAGUE_COLLECTION)
public class MemberLeagueCollectionGroup extends Group {

	private static final long serialVersionUID = -5828122999176704653L;

}
