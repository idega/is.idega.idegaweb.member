package is.idega.idegaweb.member.presentation;

import java.util.Hashtable;

import com.idega.core.location.data.Address;
import com.idega.core.location.data.Country;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CountryDropdownMenu;
import com.idega.presentation.ui.GenericButton;
import com.idega.presentation.ui.PostalCodeDropdownMenu;
import com.idega.presentation.ui.StyledButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.GroupBusiness;
import com.idega.user.data.Group;
import com.idega.user.presentation.PostalCodeEditorWindow;
import com.idega.user.presentation.UserGroupTab;

/**
 *@author     <a href="mailto:thomas@idega.is">Thomas Hilbig</a>
 *@version    1.0
 */
public class GroupOfficeAddressTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "grp_oaddr_tab_name";
	private static final String DEFAULT_TAB_NAME = "Address";
	
	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "group_office_address_tab";

	private TextInput streetField;
	private TextInput cityField;
	private TextInput provinceField;
	private PostalCodeDropdownMenu postalCodeField;
	private CountryDropdownMenu countryField;
	private TextInput poBoxField;

	private static final String streetFieldName = "UMstreet";
	private static final String cityFieldName = "UMcity";
	private static final String provinceFieldName = "UMprovince";
	private static final String postalCodeFieldName = PostalCodeDropdownMenu.IW_POSTAL_CODE_MENU_PARAM_NAME;
	private static final String countryFieldName = "UMcountry";
	private static final String poBoxFieldName = "UMpoBox";

	private Text streetText;
	private Text cityText;
	private Text provinceText;
	private Text postalCodeText;
	private Text countryText;
	private Text poBoxText;

	public GroupOfficeAddressTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	public GroupOfficeAddressTab(Group group) {
		this();
		// do not store the group because this tab instance will be also used by other groups
		// (see setGroupId() !)
		setGroupId(((Integer) group.getPrimaryKey()).intValue());
	}

	public void initializeFieldNames() {

	}

	public void initializeFieldValues() {
		if (this.fieldValues == null) {
			this.fieldValues = new Hashtable();
		}
	}

	public void updateFieldsDisplayStatus() {
		String street = (String) this.fieldValues.get(streetFieldName);
		String city = (String) this.fieldValues.get(cityFieldName);
		String province = (String) this.fieldValues.get(provinceFieldName);
		String postalId = (String) this.fieldValues.get(postalCodeFieldName);
		String countryId = (String) this.fieldValues.get(countryFieldName);
		String poBox = (String) this.fieldValues.get(poBoxFieldName);

		if (street != null) {
			this.streetField.setContent(street);
		}
		if (city != null) {
			this.cityField.setContent(city);
		}
		if (province != null) {
			this.provinceField.setContent(province);
		}

		if (postalId != null && !postalId.equals("")) {
			this.postalCodeField.setSelectedElement(Integer.parseInt(postalId));
		}

		if(countryId!=null && !countryId.equals("") ){
			this.countryField.setSelectedElement(countryId);	
		}
			
			
			
		if (poBox != null) {
			this.poBoxField.setContent(poBox);
		}
	}

	public void initializeFields() {
		this.streetField = new TextInput(streetFieldName);
		this.streetField.setLength(20);

		this.cityField = new TextInput(cityFieldName);
		this.cityField.setLength(20);

		this.provinceField = new TextInput(provinceFieldName);
		this.provinceField.setLength(20);

		//only works for Iceland
		if (this.postalCodeField == null) {
			this.postalCodeField = new PostalCodeDropdownMenu();
			this.postalCodeField.setCountry("Iceland"); //hack
		}

		this.countryField = new CountryDropdownMenu(countryFieldName);
		this.countryField.setDisabled(true);
		this.countryField.setSelectedCountry("Iceland"); //TODO remove hack

		this.poBoxField = new TextInput(poBoxFieldName);
		this.poBoxField.setLength(10);
	}

	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		this.streetText = new Text(iwrb.getLocalizedString(streetFieldName,"Street"));
		this.streetText.setBold();

		this.cityText = new Text(iwrb.getLocalizedString(cityFieldName,"City"));
		this.cityText.setBold();

		this.provinceText = new Text(iwrb.getLocalizedString(provinceFieldName,"Province"));
		this.provinceText.setBold();

		this.postalCodeText = new Text(iwrb.getLocalizedString(postalCodeFieldName,"Postal"));
		this.postalCodeText.setBold();

		this.countryText = new Text(iwrb.getLocalizedString(countryFieldName,"Country"));
		this.countryText.setBold();

		this.poBoxText = new Text(iwrb.getLocalizedString(poBoxFieldName,"P.O.Box"));
		this.poBoxText.setBold();
	}

	public void lineUpFields() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = this.getResourceBundle(iwc);
		
		resize(1, 1);

		Table table = new Table();
		table.setWidth("100%");
		table.setCellpadding(5);
		table.setCellspacing(0);

		table.add(this.streetText, 1, 1);
		table.add(Text.getBreak(), 1, 1);
		table.add(this.streetField, 1, 1);
		
		table.add(this.cityText, 2, 1);
		table.add(Text.getBreak(), 2, 1);
		table.add(this.cityField, 2, 1);

		table.add(this.provinceText, 1, 2);
		table.add(Text.getBreak(), 1, 2);
		table.add(this.provinceField, 1, 2);

		table.add(this.countryText, 2, 2);
		table.add(Text.getBreak(), 2, 2);
		table.add(this.countryField, 2, 2);

		//    fpane.add(addressTable);
		table.add(this.postalCodeText, 1, 3);
		table.add(Text.getBreak(), 1, 3);
		Table postalTable = new Table();
		postalTable.setCellpaddingAndCellspacing(0);
		postalTable.add(this.postalCodeField,1,1);
		table.add(postalTable, 1, 3);
		GenericButton addPostal = new GenericButton("add_postal", iwrb.getLocalizedString("GroupOfficeAddressTab.postalcodewindow.add","Add"));
		addPostal.setWindowToOpen(PostalCodeEditorWindow.class);
		StyledButton button = new StyledButton(addPostal);
		postalTable.setWidth(2, 3);
		postalTable.add(button, 3, 1);
				
		table.add(this.poBoxText, 2, 3);
		table.add(Text.getBreak(), 2, 3);
		table.add(this.poBoxField, 2, 3);

		add(table);
	}

	public void main(IWContext iwc) {
		getPanel().addHelpButton(getHelpButton());		
	}

	public boolean collect(IWContext iwc) {

		if (iwc != null) {
			String street = iwc.getParameter(streetFieldName);
			String city = iwc.getParameter(cityFieldName);
			String province = iwc.getParameter(provinceFieldName);
			String postal = iwc.getParameter(postalCodeFieldName);
			String country = iwc.getParameter(countryFieldName);
			String poBox = iwc.getParameter(poBoxFieldName);

			if (street != null) {
				this.fieldValues.put(streetFieldName, street);
			}
			if (city != null) {
				this.fieldValues.put(cityFieldName, city);
			}
			if (province != null) {
				this.fieldValues.put(provinceFieldName, province);
			}
			if (postal != null) {
				this.fieldValues.put(postalCodeFieldName, postal);
			}
			if (country != null) {
				this.fieldValues.put(countryFieldName, country);
			}
			if (poBox != null) {
				this.fieldValues.put(poBoxFieldName, poBox);
			}

			updateFieldsDisplayStatus();

			return true;
		}
		return false;
	}

	public boolean store(IWContext iwc) {

		Integer groupId = new Integer(getGroupId());
		String street = iwc.getParameter(streetFieldName);

		if (street != null) {
			try {
				Integer postalCodeId = null;
				String postal = iwc.getParameter(postalCodeFieldName);
				if (postal != null) {
					postalCodeId = new Integer(postal);
				}
				String country = iwc.getParameter(countryFieldName);
				String city = iwc.getParameter(cityFieldName);
				String province = iwc.getParameter(provinceFieldName);
				String poBox = iwc.getParameter(poBoxFieldName);

				getGroupBusiness(iwc).updateGroupMainAddressOrCreateIfDoesNotExist(groupId, street, postalCodeId, country, city, province, poBox);

			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		return true;

	}

	public void initFieldContents() {
		try {
			GroupBusiness groupBiz = getGroupBusiness(getEventIWContext());
			Group group = groupBiz.getGroupByGroupID(getGroupId());
			Address addr = groupBiz.getGroupMainAddress(group);

			boolean hasAddress = false;
			if (addr != null) {
				hasAddress = true;
			}

			if (hasAddress) {
				/** @todo remove this fieldValues bullshit!**/
				String street = addr.getStreetAddress();
				int code = addr.getPostalCodeID();
				Country country = addr.getCountry();
				String countryName = null;
				if (country != null) {
					countryName = country.getName();
				}
				String city = addr.getCity();
				String province = addr.getProvince();
				String poBox = addr.getPOBox();

				if (street != null) {
					this.fieldValues.put(streetFieldName, street);
				}
				if (city != null) {
					this.fieldValues.put(cityFieldName, city);
				}
				if (province != null) {
					this.fieldValues.put(provinceFieldName, province);
				}
				if (code != -1) {
					this.fieldValues.put(postalCodeFieldName, String.valueOf(code));
				}
				if (countryName != null) {
					this.fieldValues.put(countryFieldName, countryName);
				}
				if (poBox != null) {
					this.fieldValues.put(poBoxFieldName, poBox);
				}
			}

			updateFieldsDisplayStatus();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("AddressInfoTab error initFieldContents, groupId : " + getGroupId());
		}
	}
	public Help getHelpButton() {
		IWContext iwc = IWContext.getInstance();
		IWBundle iwb = getBundle(iwc);
		Help help = new Help();
		Image helpImage = iwb.getImage("help.gif");
		help.setHelpTextBundle( MEMBER_HELP_BUNDLE_IDENTIFIER);
		help.setHelpTextKey(HELP_TEXT_KEY);
		help.setImage(helpImage);
		return help;
		
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}	
} // Class AddressInfoTab