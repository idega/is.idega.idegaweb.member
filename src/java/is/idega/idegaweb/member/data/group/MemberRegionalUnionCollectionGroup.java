package is.idega.idegaweb.member.data.group;

import is.idega.idegaweb.member.util.IWMemberConstants;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_REGIONAL_UNION_COLLECTION)
public class MemberRegionalUnionCollectionGroup extends Group {

	private static final long serialVersionUID = 3141366099446755301L;

}
