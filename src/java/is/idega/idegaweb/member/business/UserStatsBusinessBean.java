/*
 * Created on Jan 20, 2005
 */
package is.idega.idegaweb.member.business;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.datareport.util.FieldsComparator;
import com.idega.block.datareport.util.ReportableCollection;
import com.idega.block.datareport.util.ReportableData;
import com.idega.block.datareport.util.ReportableField;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.contact.data.PhoneType;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.AddressType;
import com.idega.core.location.data.AddressTypeHome;
import com.idega.core.location.data.Country;
import com.idega.core.location.data.PostalCode;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.CheckBoxInputHandler;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserGroupPlugInBusiness;
import com.idega.user.business.UserInfoColumnsBusiness;
import com.idega.user.business.UserStatusBusiness;
import com.idega.user.business.UserStatusBusinessBean;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.user.data.UserInfoColumns;
import com.idega.user.data.UserStatus;
import com.idega.user.data.UserStatusHome;
import com.idega.util.CoreConstants;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

import is.idega.block.nationalregister.business.NationalRegisterBusiness;
import is.idega.block.nationalregister.data.bean.NationalRegister;
import is.idega.idegaweb.member.presentation.GroupStatsWindowPlugin;
import is.idega.idegaweb.member.presentation.UserStatsWindowPlugin;
import is.idega.idegaweb.member.util.IWMemberConstants;

/**
 * @author Sigtryggur
 *
 */
public class UserStatsBusinessBean extends IBOServiceBean implements
		UserStatsBusiness, UserGroupPlugInBusiness {

	private static final long serialVersionUID = -2878769684169022583L;

	private UserBusiness userBiz = null;
	private GroupBusiness groupBiz = null;
	private NationalRegisterBusiness nationalRegisterBiz = null;
	private UserInfoColumnsBusiness userInfoColumnsBiz = null;
	private UserStatusBusiness userStatusBiz = null;
	private IWBundle _iwb = null;
	private IWResourceBundle _iwrb = null;
	private IWResourceBundle _userIwrb = null;
	private final static String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	private final static String USER_IW_BUNDLE_IDENTIFIER = "com.idega.user";
	private static final String USR_STAT_PREFIX = "usr_stat_";
	private static final double MILLISECONDS_IN_YEAR = 31557600000d;

	private static final String LOCALIZED_CURRENT_DATE = "UserStatsBusiness.current_date";
	private static final String LOCALIZED_USER_ID = "UserStatsBusiness.user_id";
	private static final String LOCALIZED_GROUP_ID = "UserStatsBusiness.group_id";
	private static final String LOCALIZED_NAME = "UserStatsBusiness.name";
	private static final String LOCALIZED_SHORT_NAME = "UserStatsBusiness.short_name";
	private static final String LOCALIZED_ABBREVATION = "UserStatsBusiness.abbrevation";
	private static final String LOCALIZED_DISPLAY_NAME = "UserStatsBusiness.display_name";
	private static final String LOCALIZED_PERSONAL_ID = "UserStatsBusiness.personal_id";
	private static final String LOCALIZED_CUSTODIAN_NAME = "UserStatsBusiness.custodian_name";
	private static final String LOCALIZED_CUSTODIAN_PERSONAL_ID = "UserStatsBusiness.custodian_personal_id";
	private static final String LOCALIZED_CUSTODIAN_PHONE = "UserStatsBusiness.custodian_phone";
	private static final String LOCALIZED_DATE_OF_BIRTH = "UserStatsBusiness.date_of_birth";
	private static final String LOCALIZED_AGE = "UserStatsBusiness.age";
	private static final String LOCALIZED_GROUP_TYPE = "UserStatsBusiness.group_type";
	private static final String LOCALIZED_PARENT_GROUP = "UserStatsBusiness.parent_group";
	private static final String LOCALIZED_GROUP_PATH = "UserStatsBusiness.group_path";
	private static final String LOCALIZED_USER_STATUS = "UserStatsBusiness.user_status";
	private static final String LOCALIZED_STREET_ADDRESS = "UserStatsBusiness.street_address";
	private static final String LOCALIZED_POSTAL_ADDRESS = "UserStatsBusiness.postal_address";
	private static final String LOCALIZED_POST_BOX = "UserStatsBusiness.post_box";
	private static final String LOCALIZED_COUNTRY = "UserStatsBusiness.country";
	private static final String LOCALIZED_PHONE = "UserStatsBusiness.phone";
	private static final String LOCALIZED_EMAIL = "UserStatsBusiness.email";
	private static final String LOCALIZED_USER_INFO_1 = "UserStatsBusiness.user_info_1";
	private static final String LOCALIZED_USER_INFO_2 = "UserStatsBusiness.user_info_2";
	private static final String LOCALIZED_USER_INFO_3 = "UserStatsBusiness.user_info_3";
	private static final String FIELD_NAME_USER_ID = "ic_user_id";
	private static final String FIELD_NAME_GROUP_ID = "ic_group_id";
	private static final String FIELD_NAME_NAME = "name";
	private static final String FIELD_NAME_SHORT_NAME = "short_name";
	private static final String FIELD_NAME_ABBREVATION = "abbrevation";
	private static final String FIELD_NAME_DISPLAY_NAME = "display_name";
	private static final String FIELD_NAME_PERSONAL_ID = "personal_id";
	private static final String FIELD_NAME_CUSTODIAN_NAME = "custodian_name";
	private static final String FIELD_NAME_CUSTODIAN_PERSONAL_ID = "custodian_personal_id";
	private static final String FIELD_NAME_CUSTODIAN_PHONE = "custodian_phone";
	private static final String FIELD_NAME_DATE_OF_BIRTH = "date_of_birth";
	private static final String FIELD_NAME_AGE = "age";
	private static final String FIELD_NAME_GROUP_TYPE = "group_type";
	private static final String FIELD_NAME_PARENT_GROUP = "parent_group";
	private static final String FIELD_NAME_GROUP_PATH = "group_path";
	private static final String FIELD_NAME_USER_STATUS = "user_status";
	private static final String FIELD_NAME_STREET_ADDRESS = "street_address";
	private static final String FIELD_NAME_POSTAL_ADDRESS = "postal_address";
	private static final String FIELD_NAME_POST_BOX = "post_box";
	private static final String FIELD_NAME_COUNTRY = "country";
	private static final String FIELD_NAME_PHONE = "phone";
	private static final String FIELD_NAME_EMAIL = "email";
	private static final String FIELD_NAME_USER_INFO_1 = "user_info_1";
	private static final String FIELD_NAME_USER_INFO_2 = "user_info_2";
	private static final String FIELD_NAME_USER_INFO_3 = "user_info_3";

	private Map cachedGroups = new HashMap();
	private Map cachedParents = new HashMap();

	private void initializeBundlesIfNeeded(Locale locale) {
		if (this._iwb == null) {
			this._iwb = this.getIWApplicationContext().getIWMainApplication()
					.getBundle(IW_BUNDLE_IDENTIFIER);
		}
		this._iwrb = this._iwb.getResourceBundle(locale);
		this._userIwrb = this.getIWApplicationContext().getIWMainApplication()
				.getBundle(USER_IW_BUNDLE_IDENTIFIER).getResourceBundle(locale);
	}

	@Override
	public ReportableCollection getStatisticsForUsers(
			String groupIDFilter,			//	0
			String groupsRecursiveFilter,	//	1
			Collection groupTypesFilter,	//	2
			Collection userStatusesFilter,	//	3
			Integer yearOfBirthFromFilter,	//	4
			Integer yearOfBirthToFilter,	//	5
			String genderFilter,			//	6
			Collection postalCodeFilter,
			String dynamicLayout,
			String orderBy,
			String doOrderFilter,
			String runAsThread,
			String sendToEmail,
			String excel,
			String excelNoStylesheet,
			String pdf,
			String xml,
			String html,
			Locale currentLocale,
			Boolean isSuperAdmin,
			User currentUser,
			Collection<Group> sessionTopNodes,
			Collection groups,
			Group group
	) throws RemoteException {
		userStatusesFilter = userStatusesFilter == null ? Collections.emptyList() : userStatusesFilter;

		initializeBundlesIfNeeded(currentLocale);
		ReportableCollection reportCollection = new ReportableCollection();
		// PARAMETES
		// Add extra...because the inputhandlers supply the basic header texts

		reportCollection.addExtraHeaderParameter("label_current_date",
				this._iwrb.getLocalizedString(LOCALIZED_CURRENT_DATE,
						"Current date"), "current_date", TextSoap.findAndCut(
						(new IWTimestamp()).getLocaleDateAndTime(currentLocale,
								IWTimestamp.LONG, IWTimestamp.SHORT), "GMT"));

		// PARAMETERS that are also FIELDS
		// data from entity columns, can also be defined with an entity
		// definition, see getClubMemberStatisticsForRegionalUnions method
		// The name you give the field/parameter must not contain spaces or
		// special characters
		ReportableField userIDField = null;
		if (isSuperAdmin.booleanValue()) {
			userIDField = new ReportableField(FIELD_NAME_USER_ID, String.class);
			userIDField.setLocalizedName(this._iwrb.getLocalizedString(
					LOCALIZED_USER_ID, "User ID"), currentLocale);
			reportCollection.addField(userIDField);
		}
		ReportableField nameField = new ReportableField(FIELD_NAME_NAME,
				String.class);
		nameField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_NAME, "Name"), currentLocale);
		reportCollection.addField(nameField);

		ReportableField personalIDField = new ReportableField(
				FIELD_NAME_PERSONAL_ID, String.class);
		personalIDField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_PERSONAL_ID, "Personal ID"), currentLocale);
		reportCollection.addField(personalIDField);

		ReportableField dateOfBirthField = new ReportableField(
				FIELD_NAME_DATE_OF_BIRTH, String.class);
		dateOfBirthField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_DATE_OF_BIRTH, "Date of birth"), currentLocale);
		reportCollection.addField(dateOfBirthField);

		ReportableField ageField = new ReportableField(FIELD_NAME_AGE,
				String.class);
		ageField.setLocalizedName(this._iwrb.getLocalizedString(LOCALIZED_AGE,
				"Age"), currentLocale);
		reportCollection.addField(ageField);

		ReportableField parentGroupField = new ReportableField(
				FIELD_NAME_PARENT_GROUP, String.class);
		parentGroupField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_PARENT_GROUP, "Parent group"), currentLocale);
		reportCollection.addField(parentGroupField);

		ReportableField groupPathField = new ReportableField(
				FIELD_NAME_GROUP_PATH, String.class);
		groupPathField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_GROUP_PATH, "Group Path"), currentLocale);
		reportCollection.addField(groupPathField);

		ReportableField userStatusField = new ReportableField(
				FIELD_NAME_USER_STATUS, String.class);
		userStatusField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_USER_STATUS, "User Status"), currentLocale);
		reportCollection.addField(userStatusField);

		ReportableField streetAddressField = new ReportableField(
				FIELD_NAME_STREET_ADDRESS, String.class);
		streetAddressField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_STREET_ADDRESS, "Street Address"), currentLocale);
		reportCollection.addField(streetAddressField);

		ReportableField postalAddressField = new ReportableField(
				FIELD_NAME_POSTAL_ADDRESS, String.class);
		postalAddressField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_POSTAL_ADDRESS, "Postal Address"), currentLocale);
		reportCollection.addField(postalAddressField);

		ReportableField countryField = new ReportableField(FIELD_NAME_COUNTRY,
				String.class);
		countryField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_COUNTRY, "Country"), currentLocale);
		reportCollection.addField(countryField);

		ReportableField phoneField = new ReportableField(FIELD_NAME_PHONE,
				String.class);
		phoneField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_PHONE, "Phone"), currentLocale);
		reportCollection.addField(phoneField);

		ReportableField emailField = new ReportableField(FIELD_NAME_EMAIL,
				String.class);
		emailField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_EMAIL, "Email"), currentLocale);
		reportCollection.addField(emailField);

		ReportableField userInfo1Field = new ReportableField(
				FIELD_NAME_USER_INFO_1, String.class);
		userInfo1Field.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_USER_INFO_1, "User info 1"), currentLocale);
		reportCollection.addField(userInfo1Field);

		ReportableField userInfo2Field = new ReportableField(
				FIELD_NAME_USER_INFO_2, String.class);
		userInfo2Field.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_USER_INFO_2, "User info 2"), currentLocale);
		reportCollection.addField(userInfo2Field);

		ReportableField userInfo3Field = new ReportableField(
				FIELD_NAME_USER_INFO_3, String.class);
		userInfo3Field.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_USER_INFO_3, "User info 3"), currentLocale);
		reportCollection.addField(userInfo3Field);

		ReportableField custodianNameField = new ReportableField(
				FIELD_NAME_CUSTODIAN_NAME, String.class);
		custodianNameField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_CUSTODIAN_NAME, "Custodian name"), currentLocale);
		reportCollection.addField(custodianNameField);

		ReportableField custodianPersonalIDField = new ReportableField(
				FIELD_NAME_CUSTODIAN_PERSONAL_ID, String.class);
		custodianPersonalIDField.setLocalizedName(this._iwrb
				.getLocalizedString(LOCALIZED_CUSTODIAN_PERSONAL_ID,
						"Custodian personal ID"), currentLocale);
		reportCollection.addField(custodianPersonalIDField);

		ReportableField custodianPhoneField = new ReportableField(
				FIELD_NAME_CUSTODIAN_PHONE, String.class);
		custodianPhoneField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_CUSTODIAN_PHONE, "Custodian phone"), currentLocale);
		reportCollection.addField(custodianPhoneField);

		Collection<User> users = getUserBusiness().getUsersBySpecificGroupsUserstatusDateOfBirthAndGender(
				groups,
				userStatusesFilter,
				yearOfBirthFromFilter,
				yearOfBirthToFilter,
				genderFilter
		);
		Collection<Group> topNodes = getUserBusiness().getUsersTopGroupNodesByViewAndOwnerPermissionsInThread(
						currentUser,
						sessionTopNodes,
						isSuperAdmin == null ? false : isSuperAdmin,
						currentUser
		);
		Map<Object, List<ReportableData>> usersByGroups = new TreeMap<>();
		AddressTypeHome addressHome = (AddressTypeHome) IDOLookup.getHome(AddressType.class);
		AddressType at1 = null;
		try {
			at1 = addressHome.findAddressType2();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		Iterator<User> iter = users.iterator();
		while (iter.hasNext()) {
			User user = iter.next();
			Collection<Group> parentGroupCollection = null;
			try {
				parentGroupCollection = getGroupHome().findParentGroups(Integer.parseInt(user.getGroup().getPrimaryKey().toString()));
			} catch (Exception e) {
				getLogger().log(Level.WARNING, e.getMessage(), e);
			}
			parentGroupCollection.retainAll(groups);
			Iterator<Group> parIt = parentGroupCollection.iterator();

			String personalID = user.getPersonalID();
			String dateOfBirthString = null;
			String ageString = null;
			String custodianString = null;
			String custodianPersonalID = null;
			String custodianPhoneString = null;
			try {
				Date date_of_birth = user.getDateOfBirth();
				if (date_of_birth != null) {
					dateOfBirthString = new IWTimestamp(date_of_birth).getDateString("dd.MM.yyyy");
					long ageInMillisecs = IWTimestamp.getMilliSecondsBetween(new IWTimestamp(date_of_birth), new IWTimestamp());
					BigDecimal age = new BigDecimal(ageInMillisecs / MILLISECONDS_IN_YEAR);
					ageString = String.valueOf(age.intValue());
					if (age.doubleValue() < 18) {
						NationalRegister userRegister = getNationalRegisterBusiness().getEntryBySSN(user.getPersonalID());
						if (userRegister != null) {
							custodianPersonalID = userRegister.getFamilyId();
							User custodian = null;
							try {
								custodian = getUserBusiness().getUser(custodianPersonalID);
							} catch (Exception e) {}
							custodianString = custodian == null ? CoreConstants.EMPTY : custodian.getName();
							custodianPhoneString = getPhoneNumber(custodian);
						}
					} else {
						custodianPersonalID = personalID;
					}
					if (custodianPersonalID != null && custodianPersonalID.length() == 10) {
						custodianPersonalID = custodianPersonalID.substring(0, 6) + "-" + custodianPersonalID.substring(6, 10);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (personalID != null && personalID.length() == 10) {
				personalID = personalID.substring(0, 6) + "-" + personalID.substring(6, 10);
			}
			Collection<Email> emails = user.getEmails();
			Email email = null;
			String emailString = null;
			if (!emails.isEmpty()) {
				email = emails.iterator().next();
				emailString = email.getEmailAddress();
			}
			Collection<Address> addresses = null;
			if (at1 != null) {

				try {
					addresses = user.getAddresses(at1);
				} catch (IDOLookupException e1) {
					e1.printStackTrace();
				} catch (IDOCompositePrimaryKeyException e1) {
					e1.printStackTrace();
				} catch (IDORelationshipException e1) {
					e1.printStackTrace();
				}
			}
			Address address = null;
			String streetAddressString = null;
			String postalAddressString = null;
			String countryString = null;
			if (addresses != null && !addresses.isEmpty()) {
				address = addresses.iterator().next();
				PostalCode postalCode = address.getPostalCode();
				if (postalCode != null) {
					String postalCodeString = postalCode.getPostalCode();
					if (!postalCodeFilter.isEmpty() && !postalCodeFilter.contains(postalCodeString)) {
						continue;
					}
				}
				streetAddressString = address.getStreetAddress();
				postalAddressString = address.getPostalAddress();
				Country country = address.getCountry();
				if (country != null) {
					countryString = country.getName();
					Locale locale = new Locale(currentLocale.getLanguage(), country.getIsoAbbreviation());
					String localizedCountryName = locale.getDisplayCountry(currentLocale);
					if (localizedCountryName != null && !localizedCountryName.equals("")) {
						countryString = localizedCountryName;
					}
				}
			} else if (!postalCodeFilter.isEmpty()) {
				continue;
			}
			while (parIt.hasNext()) {
				Group parentGroup = parIt.next();
				Collection<UserStatus> userStatuses = null;
				String userStatusString = null;
				try {
					userStatuses = ((UserStatusHome) IDOLookup.getHome(UserStatus.class)).findAllActiveByUserIdAndGroupId(
							Integer.parseInt(user.getPrimaryKey().toString()),
							Integer.parseInt(parentGroup.getPrimaryKey().toString())
					);
					if (userStatuses != null && userStatuses.isEmpty() && userStatusesFilter.contains(UserStatusBusinessBean.STATUS_DECEASED)) {
						UserStatus userStatus = getUserStatusBusiness().getDeceasedUserStatus((Integer) user.getPrimaryKey());
						if (userStatus != null) {
							userStatuses.add(userStatus);
						}
					}
				} catch (FinderException e) {
					System.out.println(e.getMessage());
				}
				if (userStatuses.isEmpty()) {
					if (userStatusesFilter != null && !userStatusesFilter.isEmpty()) {
						continue;
					}
				} else {
					UserStatus userStatus = userStatuses.iterator().next();
					String userStatusKey = userStatus.getStatus().getStatusKey();
					if (!userStatusesFilter.isEmpty() && !userStatusesFilter.contains(userStatusKey)) {
						continue;
					} else {
						userStatusString = this._iwrb.getLocalizedString(USR_STAT_PREFIX + userStatusKey, userStatusKey);
					}
				}
				String userInfo1String = "";
				String userInfo2String = "";
				String userInfo3String = "";
				UserInfoColumns userInfoColumns = getUserInfoColumnsBusiness().getUserInfo(
								Integer.parseInt(user.getPrimaryKey().toString()),
								Integer.parseInt(group.getPrimaryKey().toString())
				);
				if (userInfoColumns != null) {
					userInfo1String = userInfoColumns.getUserInfo1();
					userInfo2String = userInfoColumns.getUserInfo2();
					userInfo3String = userInfoColumns.getUserInfo3();
				}
				String parentGroupPath = getParentGroupPath(parentGroup,
						topNodes);
				// create a new ReportData for each row
				ReportableData data = new ReportableData();
				// add the data to the correct fields/columns
				if (isSuperAdmin.booleanValue()) {
					data.addData(userIDField, user.getPrimaryKey().toString());
				}
				data.addData(nameField, user.getName());
				data.addData(personalIDField, personalID);
				data.addData(dateOfBirthField, dateOfBirthString);
				data.addData(ageField, ageString);
				data.addData(parentGroupField, parentGroup.getName());
				data.addData(groupPathField, parentGroupPath);
				data.addData(userStatusField, userStatusString);
				data.addData(streetAddressField, streetAddressString);
				data.addData(postalAddressField, postalAddressString);
				data.addData(countryField, countryString);
				data.addData(phoneField, getPhoneNumber(user));
				data.addData(emailField, emailString);
				data.addData(userInfo1Field, userInfo1String);
				data.addData(userInfo2Field, userInfo2String);
				data.addData(userInfo3Field, userInfo3String);
				data.addData(custodianNameField, custodianString);
				data.addData(custodianPersonalIDField, custodianPersonalID);
				data.addData(custodianPhoneField, custodianPhoneString);
				List<ReportableData> statsForGroup = usersByGroups.get(parentGroup.getPrimaryKey());
				if (statsForGroup == null) {
					statsForGroup = new ArrayList<>();
				}
				statsForGroup.add(data);
				usersByGroups.put(parentGroup.getPrimaryKey(), statsForGroup);
			}
		}
		// iterate through the ordered map and ordered lists and add to the
		// final collection
		Iterator<Object> statsDataIter = usersByGroups.keySet().iterator();
		while (statsDataIter.hasNext()) {

			List<ReportableData> datas = usersByGroups.get(statsDataIter.next());
			// don't forget to add the row to the collection
			reportCollection.addAll(datas);
		}

		if (doOrderFilter != null && doOrderFilter.equals(CheckBoxInputHandler.CHECKED)) {

			ReportableField[] sortFields = null;
			List orderByFields = new ArrayList();
			dynamicLayout = dynamicLayout == null ? "-1" : dynamicLayout;
			if (dynamicLayout.equals("-1")) {
				orderByFields.add(groupPathField);
			}
			if (orderBy != null) {
				if (!dynamicLayout.equals("-1") && orderBy.equals(IWMemberConstants.ORDER_BY_GROUP_PATH)) {
					orderByFields.add(groupPathField);
				} else if (orderBy.equals(IWMemberConstants.ORDER_BY_USER_STATUS)) {
					orderByFields.add(userStatusField);
				} else if (orderBy.equals(IWMemberConstants.ORDER_BY_ADDRESS)) {
					orderByFields.add(streetAddressField);
				} else if (orderBy.equals(IWMemberConstants.ORDER_BY_POSTAL_ADDRESS)) {
					orderByFields.add(postalAddressField);
				}
			}
			orderByFields.add(nameField);

			sortFields = new ReportableField[orderByFields.size()];
			for (int i = 0; i < orderByFields.size(); i++) {
				sortFields[i] = (ReportableField) orderByFields.get(i);
			}
			Comparator comparator = new FieldsComparator(sortFields);
			Collections.sort(reportCollection, comparator);
		}

		return reportCollection;
	}

	@Override
	public ReportableCollection getStatisticsForGroups(String groupIDFilter,
			String groupsRecursiveFilter, Collection groupTypesFilter,
			String dynamicLayout, String orderBy, String doOrderFilter,
			String runAsThread, String sendToEmail, String excel, String excelNoStylesheet,
			String pdf, String xml, String html, Locale currentLocale, Boolean isSuperAdmin,
			User currentUser, Collection sessionTopNodes, Collection groups, Group topGroup) throws RemoteException {
		initializeBundlesIfNeeded(currentLocale);
		ReportableCollection reportCollection = new ReportableCollection();
		// PARAMETES
		// Add extra...because the inputhandlers supply the basic header texts

		reportCollection.addExtraHeaderParameter("label_current_date",
				this._iwrb.getLocalizedString(LOCALIZED_CURRENT_DATE,
						"Current date"), "current_date", TextSoap.findAndCut(
						(new IWTimestamp()).getLocaleDateAndTime(currentLocale,
								IWTimestamp.LONG, IWTimestamp.SHORT), "GMT"));

		// PARAMETERS that are also FIELDS
		// data from entity columns, can also be defined with an entity
		// definition, see getClubMemberStatisticsForRegionalUnions method
		// The name you give the field/parameter must not contain spaces or
		// special characters
		ReportableField groupIDField = null;
		if (isSuperAdmin.booleanValue()) {
			groupIDField = new ReportableField(FIELD_NAME_GROUP_ID,
					String.class);
			groupIDField.setLocalizedName(this._iwrb.getLocalizedString(
					LOCALIZED_GROUP_ID, "Group ID"), currentLocale);
			reportCollection.addField(groupIDField);
		}
		ReportableField nameField = new ReportableField(FIELD_NAME_NAME,
				String.class);
		nameField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_NAME, "Name"), currentLocale);
		reportCollection.addField(nameField);

		ReportableField shortNameField = new ReportableField(
				FIELD_NAME_SHORT_NAME, String.class);
		shortNameField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_SHORT_NAME, "Short Name"), currentLocale);
		reportCollection.addField(shortNameField);

		ReportableField abbrevationField = new ReportableField(
				FIELD_NAME_ABBREVATION, String.class);
		abbrevationField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_ABBREVATION, "Abbrevation"), currentLocale);
		reportCollection.addField(abbrevationField);

		ReportableField displayNameField = new ReportableField(
				FIELD_NAME_DISPLAY_NAME, String.class);
		displayNameField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_DISPLAY_NAME, "Display Name"), currentLocale);
		reportCollection.addField(displayNameField);

		ReportableField groupTypeField = new ReportableField(
				FIELD_NAME_GROUP_TYPE, String.class);
		groupTypeField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_GROUP_TYPE, "Group Type"), currentLocale);
		reportCollection.addField(groupTypeField);

		ReportableField groupPathField = new ReportableField(
				FIELD_NAME_GROUP_PATH, String.class);
		groupPathField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_GROUP_PATH, "Group Path"), currentLocale);
		reportCollection.addField(groupPathField);

		ReportableField streetAddressField = new ReportableField(
				FIELD_NAME_STREET_ADDRESS, String.class);
		streetAddressField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_STREET_ADDRESS, "Street Address"), currentLocale);
		reportCollection.addField(streetAddressField);

		ReportableField postalAddressField = new ReportableField(
				FIELD_NAME_POSTAL_ADDRESS, String.class);
		postalAddressField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_POSTAL_ADDRESS, "Postal Address"), currentLocale);
		reportCollection.addField(postalAddressField);

		ReportableField pBoxField = new ReportableField(FIELD_NAME_POST_BOX,
				String.class);
		pBoxField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_POST_BOX, "Postbox"), currentLocale);
		reportCollection.addField(pBoxField);

		ReportableField phoneField = new ReportableField(FIELD_NAME_PHONE,
				String.class);
		phoneField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_PHONE, "Phone"), currentLocale);
		reportCollection.addField(phoneField);

		ReportableField emailField = new ReportableField(FIELD_NAME_EMAIL,
				String.class);
		emailField.setLocalizedName(this._iwrb.getLocalizedString(
				LOCALIZED_EMAIL, "Email"), currentLocale);
		reportCollection.addField(emailField);

		Collection topNodes = getUserBusiness()
				.getUsersTopGroupNodesByViewAndOwnerPermissionsInThread(
						currentUser, sessionTopNodes, isSuperAdmin.booleanValue(), currentUser);
		Map usersByGroups = new TreeMap();
		AddressTypeHome addressHome = (AddressTypeHome) IDOLookup
				.getHome(AddressType.class);
		AddressType at1 = null;
		try {
			at1 = addressHome.findAddressType1();
		} catch (FinderException e) {
			e.printStackTrace();
		}
		Iterator iter = groups.iterator();
		while (iter.hasNext()) {
			Group group = (Group) iter.next();
			Collection parentGroupCollection = null;
			try {
				parentGroupCollection = getGroupHome().findParentGroups(
						Integer.parseInt(group.getPrimaryKey().toString()));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			Iterator parIt = parentGroupCollection.iterator();

			String nameString = group.getName();
			String shortNameString = group.getShortName();
			String abbrevation = group.getAbbrevation();
			String displayNameString = null;

			if (abbrevation != null && !abbrevation.equals("")) {
				displayNameString = abbrevation;
			} else {
				displayNameString = nameString;
			}
			String groupTypeString = this._userIwrb.getLocalizedString(group
					.getGroupType(), group.getGroupType());
			Collection emails = group.getEmails();
			Email email = null;
			String emailString = null;
			if (!emails.isEmpty()) {
				email = (Email) emails.iterator().next();
				emailString = email.getEmailAddress();
			}
			Collection addresses = null;
			if (at1 != null) {
				try {
					addresses = group.getAddresses(at1);
				} catch (IDOLookupException e1) {
					e1.printStackTrace();
				} catch (IDOCompositePrimaryKeyException e1) {
					e1.printStackTrace();
				} catch (IDORelationshipException e1) {
					e1.printStackTrace();
				}
			}
			Address address = null;
			String streetAddressString = null;
			String postalAddressString = null;
			String postBoxString = null;
			if (addresses != null && !addresses.isEmpty()) {
				address = (Address) addresses.iterator().next();
				streetAddressString = address.getStreetAddress();
				postalAddressString = address.getPostalAddress();
				postBoxString = address.getPOBox();
			}
			while (parIt.hasNext()) {
				Group parentGroup = (Group) parIt.next();
				String parentGroupPath = getParentGroupPath(parentGroup,
						topNodes);
				// create a new ReportData for each row
				ReportableData data = new ReportableData();
				// add the data to the correct fields/columns
				if (isSuperAdmin.booleanValue()) {
					data
							.addData(groupIDField, group.getPrimaryKey()
									.toString());
				}
				data.addData(nameField, nameString);
				data.addData(shortNameField, shortNameString);
				data.addData(abbrevationField, abbrevation);
				data.addData(displayNameField, displayNameString);
				data.addData(groupTypeField, groupTypeString);
				data.addData(groupPathField, parentGroupPath);
				data.addData(emailField, emailString);
				data.addData(streetAddressField, streetAddressString);
				data.addData(postalAddressField, postalAddressString);
				data.addData(pBoxField, postBoxString);
				data.addData(phoneField, getPhoneNumber(group));
				List statsForGroup = (List) usersByGroups.get(group
						.getPrimaryKey());
				if (statsForGroup == null) {
					statsForGroup = new Vector();
				}
				statsForGroup.add(data);
				usersByGroups.put(group.getPrimaryKey(), statsForGroup);
			}
		}
		// iterate through the ordered map and ordered lists and add to the
		// final collection
		Iterator statsDataIter = usersByGroups.keySet().iterator();
		while (statsDataIter.hasNext()) {
			List datas = (List) usersByGroups.get(statsDataIter.next());
			// don't forget to add the row to the collection
			reportCollection.addAll(datas);
		}

		if (doOrderFilter != null
				&& doOrderFilter.equals("checked")) {

			ReportableField[] sortFields = null;
			List orderByFields = new ArrayList();
			dynamicLayout = dynamicLayout == null ? "-1" : dynamicLayout;
			if (dynamicLayout.equals("-1")) {
				orderByFields.add(groupPathField);
			}
			if (orderBy != null) {
				if (!dynamicLayout.equals("-1")
						&& orderBy.equals(IWMemberConstants.ORDER_BY_GROUP_PATH)) {
					orderByFields.add(groupPathField);
				} else if (orderBy.equals(IWMemberConstants.ORDER_BY_GROUP_TYPE)) {
					orderByFields.add(groupTypeField);
				} else if (orderBy.equals(IWMemberConstants.ORDER_BY_ADDRESS)) {
					orderByFields.add(streetAddressField);
				} else if (orderBy
						.equals(IWMemberConstants.ORDER_BY_POSTAL_ADDRESS)) {
					orderByFields.add(postalAddressField);
				}
			}
			orderByFields.add(nameField);

			sortFields = new ReportableField[orderByFields.size()];
			for (int i = 0; i < orderByFields.size(); i++) {
				sortFields[i] = (ReportableField) orderByFields.get(i);
			}
			Comparator comparator = new FieldsComparator(sortFields);
			Collections.sort(reportCollection, comparator);
		}
		return reportCollection;
	}

	private String getPhoneNumber(Group group) {
		if (group == null) {
			return CoreConstants.EMPTY;
		}

		Collection phones = group.getPhones();
		String phoneNumber = "";
		if (!phones.isEmpty()) {
			Phone phone = null;
			int tempPhoneType = 0;
			int selectedPhoneType = 0;

			Iterator phIt = phones.iterator();
			while (phIt.hasNext()) {
				phone = (Phone) phIt.next();
				if (phone != null) {
					tempPhoneType = phone.getPhoneTypeId();
					if (tempPhoneType != PhoneType.FAX_NUMBER_ID) {
						if (tempPhoneType == PhoneType.MOBILE_PHONE_ID) {
							phoneNumber = phone.getNumber();
							break;
						} else if (tempPhoneType == PhoneType.HOME_PHONE_ID
								&& selectedPhoneType != PhoneType.HOME_PHONE_ID) {
							phoneNumber = phone.getNumber();
							selectedPhoneType = phone.getPhoneTypeId();
						} else if (tempPhoneType == PhoneType.WORK_PHONE_ID
								&& selectedPhoneType != PhoneType.WORK_PHONE_ID) {
							phoneNumber = phone.getNumber();
							selectedPhoneType = phone.getPhoneTypeId();
						}
					}
				}
			}
		}
		return phoneNumber;
	}

	private String getParentGroupPath(Group parentGroup, Collection topNodes) {
		String parentGroupPath = parentGroup.getName();
		Collection parentGroupCollection = null;

		while (parentGroup != null && !topNodes.contains(parentGroup)) {
			String parentKey = parentGroup.getPrimaryKey().toString();
			if (this.cachedParents.containsKey((parentKey))) {
				Collection col = (Collection) this.cachedParents.get(parentKey);
				Iterator it = col.iterator();
				Integer parentID = null;
				if (it.hasNext()) {
					parentID = (Integer) it.next();
					String groupKey = parentID.toString();
					if (this.cachedGroups.containsKey(groupKey)) {
						parentGroup = (Group) this.cachedGroups.get(groupKey);
					} else {
						try {
							parentGroup = this.groupBiz
									.getGroupByGroupID(parentID.intValue());
							this.cachedGroups.put(groupKey, parentGroup);
						} catch (Exception e) {
							break;
						}
					}
				} else {
					break;
				}
			} else {
				parentGroupCollection = parentGroup.getParentGroups(
						this.cachedParents, this.cachedGroups);

				if (!parentGroupCollection.isEmpty()) {
					parentGroup = (Group) parentGroupCollection.iterator()
							.next();
				} else {
					break;
				}
			}
			parentGroupPath = parentGroup.getName() + "/" + parentGroupPath;
		}
		return parentGroupPath;
	}

	private GroupHome getGroupHome() {
		try {
			return (GroupHome) IDOLookup.getHome(Group.class);
		} catch (IDOLookupException e) {
			e.printStackTrace();
		}
		return null;
	}

	private GroupBusiness getGroupBusiness() throws RemoteException {
		if (this.groupBiz == null) {
			this.groupBiz = IBOLookup.getServiceInstance(this
					.getIWApplicationContext(), GroupBusiness.class);
		}
		return this.groupBiz;
	}

	private UserBusiness getUserBusiness() throws RemoteException {
		if (this.userBiz == null) {
			this.userBiz = IBOLookup.getServiceInstance(this
					.getIWApplicationContext(), UserBusiness.class);
		}
		return this.userBiz;
	}

	private NationalRegisterBusiness getNationalRegisterBusiness()
			throws RemoteException {
		if (this.nationalRegisterBiz == null) {
			this.nationalRegisterBiz = IBOLookup
					.getServiceInstance(this.getIWApplicationContext(),
							NationalRegisterBusiness.class);
		}
		return this.nationalRegisterBiz;
	}

	private UserInfoColumnsBusiness getUserInfoColumnsBusiness()
			throws RemoteException {
		if (this.userInfoColumnsBiz == null) {
			this.userInfoColumnsBiz = IBOLookup
					.getServiceInstance(this.getIWApplicationContext(),
							UserInfoColumnsBusiness.class);
		}
		return this.userInfoColumnsBiz;
	}

	private UserStatusBusiness getUserStatusBusiness() throws RemoteException {
		if (this.userStatusBiz == null) {
			this.userStatusBiz = IBOLookup
					.getServiceInstance(this.getIWApplicationContext(),
							UserStatusBusiness.class);
		}
		return this.userStatusBiz;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.idega.user.business.UserGroupPlugInBusiness#getMainToolbarElements()
	 */
	@Override
	public List getMainToolbarElements() throws RemoteException {
		List list = new ArrayList(1);
		list.add(new UserStatsWindowPlugin());
		list.add(new GroupStatsWindowPlugin());
		return list;
	}

	@Override
	public void afterGroupCreateOrUpdate(Group group, Group parentGroup)
			throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterUserCreateOrUpdate(User user, Group parentGroup)
			throws CreateException, RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeGroupRemove(Group group, Group parentGroup)
			throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeUserRemove(User user, Group parentGroup)
			throws RemoveException, RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public String canCreateSubGroup(Group parentGroup,
			String groupTypeOfSubGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getGroupPropertiesTabs(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getGroupToolbarElements(Group group) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List getUserPropertiesTabs(User user) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PresentationObject instanciateEditor(Group group)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PresentationObject instanciateViewer(Group group)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isUserAssignableFromGroupToGroup(User user,
			Group sourceGroup, Group targetGroup) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isUserSuitedForGroup(User user, Group targetGroup)
			throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
}