package is.idega.idegaweb.member.data.group;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.idega.user.data.bean.Group;

import is.idega.idegaweb.member.util.IWMemberConstants;

@Entity
@DiscriminatorValue(IWMemberConstants.GROUP_TYPE_ALIAS)
public class MemberAliasGroup extends Group {

	private static final long serialVersionUID = 7029590097216467852L;



}