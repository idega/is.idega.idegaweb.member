package is.idega.idegaweb.member.integration.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.user.data.User;
import com.idega.util.EmailValidator;
import com.idega.util.ListUtil;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class MemberChangeRequest implements Serializable {

	private static final long serialVersionUID = -8765032743543058541L;

	private String image;
	private String personalId;
	private String name;
	private String membershipNumber;
	private String gender;
	private String status;
	private Address address;
	private List<String> phones;
	private String email;
	private String membershipType;
	private String loginName;
	private String password;
	private String userId;
	private String memberId;
	private String unionId;
	private String unionUniqueId;
	private List<String> relations;
	private String newImage;
	private String sportUnion;

	private Integer genderId;

	private String groupUniqueId;
	private String clubUniqueId;
	private List<String> leaguesUniqueIds;
	private List<String> leaguesNames;

	public MemberChangeRequest() {
		super();
	}

	public MemberChangeRequest(User user, String status, String groupUniqueId, String clubUniqueId, List<String> leaguesUniqueIds, List<String> leaguesNames) {
		this();

		this.status = status;
		this.groupUniqueId = groupUniqueId;
		this.clubUniqueId = clubUniqueId;
		this.leaguesUniqueIds = leaguesUniqueIds;
		this.leaguesNames = leaguesNames;

		if (user != null) {
			this.personalId = user.getPersonalID();
			this.name = user.getName();
			this.genderId = user.getGenderID();

			Collection<Email> emails = null;
			try {
				emails = user.getEmails();
			} catch (Exception e) {}
			if (!ListUtil.isEmpty(emails)) {
				boolean filled = false;
				for (Iterator<Email> emailsIter = emails.iterator(); (!filled && emailsIter.hasNext());) {
					Email email = emailsIter.next();
					if (email == null) {
						continue;
					}
					String emailAddress = email.getEmailAddress();
					if (!EmailValidator.getInstance().isValid(emailAddress)) {
						continue;
					}

					this.email = emailAddress;
					filled = true;
				}
			}

			Collection<Phone> phones = null;
			try {
				phones = user.getPhones();
			} catch (Exception e) {}
			if (!ListUtil.isEmpty(phones)) {
				this.phones = new ArrayList<>();
				for (Phone phone: phones) {
					this.phones.add(phone.getNumber());
				}
			}

			com.idega.core.location.data.Address address = null;
			try {
				address = user.getUsersMainAddress();
			} catch (Exception e) {}
			if (address != null) {
				Address addressBean = new Address();
				this.address = addressBean;

				addressBean.setStreetName(address.getStreetName());
				addressBean.setStreetNumber(address.getStreetNumber());
				addressBean.setTown(address.getCity());

				try {
					PostalCode postalCode = address.getPostalCode();
					addressBean.setPostalCode(postalCode.getPostalCode());
				} catch (Exception e) {}

				try {
					Country country = address.getCountry();
					addressBean.setCountry(country.getName());
				} catch (Exception e) {}
			}
		}
	}

	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}

	public String getNewImage() {
		return newImage;
	}
	public void setNewImage(String newImage) {
		this.newImage = newImage;
	}

	public String getPersonalId() {
		return personalId;
	}
	public void setPersonalId(String personalId) {
		this.personalId = personalId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getMembershipNumber() {
		return membershipNumber;
	}
	public void setMembershipNumber(String membershipNumber) {
		this.membershipNumber = membershipNumber;
	}

	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getPhones() {
		return phones;
	}
	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getMembershipType() {
		return membershipType;
	}
	public void setMembershipType(String membershipType) {
		this.membershipType = membershipType;
	}

	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getUnionId() {
		return unionId;
	}
	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public List<String> getRelations() {
		return relations;
	}
	public void setRelations(List<String> relations) {
		this.relations = relations;
	}

	public String getSportUnion() {
		return sportUnion;
	}
	public void setSportUnion(String sportUnion) {
		this.sportUnion = sportUnion;
	}

	public String getUnionUniqueId() {
		return unionUniqueId;
	}
	public void setUnionUniqueId(String unionUniqueId) {
		this.unionUniqueId = unionUniqueId;
	}

	public String getGroupUniqueId() {
		return groupUniqueId;
	}

	public void setGroupUniqueId(String groupUniqueId) {
		this.groupUniqueId = groupUniqueId;
	}

	public String getClubUniqueId() {
		return clubUniqueId;
	}

	public void setClubUniqueId(String clubUniqueId) {
		this.clubUniqueId = clubUniqueId;
	}

	public List<String> getLeaguesUniqueIds() {
		return leaguesUniqueIds;
	}

	public void setLeaguesUniqueIds(List<String> leaguesUniqueIds) {
		this.leaguesUniqueIds = leaguesUniqueIds;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public List<String> getLeaguesNames() {
		return leaguesNames;
	}

	public void setLeaguesNames(List<String> leaguesNames) {
		this.leaguesNames = leaguesNames;
	}

	@Override
	public String toString() {
		return getPersonalId() + ", status: " + getStatus() + ", group UID: " + getGroupUniqueId() + ", club UID: " + getClubUniqueId() + ", leagues UID: " + getLeaguesUniqueIds();
	}

}