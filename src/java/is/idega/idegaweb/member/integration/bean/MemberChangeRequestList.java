package is.idega.idegaweb.member.integration.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MemberChangeRequestList implements Serializable {

	private static final long serialVersionUID = 3344002455872158943L;

	private List<MemberChangeRequest> members;

	public void addMember(MemberChangeRequest member) {
		if (member == null) {
			return;
		}

		if (members == null) {
			members = new ArrayList<>();
		}

		members.add(member);
	}

	public List<MemberChangeRequest> getMembers() {
		return members;
	}

	public void setMembers(List<MemberChangeRequest> members) {
		this.members = members;
	}

}