package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_LEAGUE_CLUB_COLLECTION)
public class MemberLeagueClubCollectionGroup extends Group {

	private static final long serialVersionUID = 5129106641552920132L;

}
