/*
 * Created on Mar 11, 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package is.idega.idegaweb.member.presentation;

import is.idega.idegaweb.member.business.plugins.ClubInformationPluginBusiness;

import java.rmi.RemoteException;
import java.util.Hashtable;

import javax.ejb.FinderException;

import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.TextArea;
import com.idega.presentation.ui.TextInput;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.UserGroupTab;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClubPlayerTab extends UserGroupTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "cplay_tab_name";
	private static final String DEFAULT_TAB_NAME = "Club Player";

	private Text _unionField;
	private TextInput _cplayField;
	private TextInput _nameField;
	private CheckBox _competitionField;
	private TextArea _coachesField;

	private Text _unionText;
	private Text _cplayText;
	private Text _nameText;
	private Text _competitionText;
	private Text _coachesText;

	private String _unionFieldName;
	private String _cplayFieldName;
	private String _nameFieldName;
	private String _competitionFieldName;
	private String _coachesFieldName;

	public ClubPlayerTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));

		//		setName(DEFAULT_TAB_NAME);
		
//		setName("Flokkar/h�par - grunng�gn");
	}

	public ClubPlayerTab(Group group) {
		this();
		setGroupId(((Integer) group.getPrimaryKey()).intValue());
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldNames()
	 */
	public void initializeFieldNames() {
		_unionFieldName = "cplay_union";
		_cplayFieldName = "cplay_division";
		_nameFieldName = "cplay_name";
		_competitionFieldName = "cplay_comp";
		_coachesFieldName = "cplay_coach";		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFieldValues()
	 */
	public void initializeFieldValues() {
		fieldValues = new Hashtable();
		fieldValues.put(_unionFieldName, "");
		fieldValues.put(_cplayFieldName, "");
		fieldValues.put(_nameFieldName, "");
		fieldValues.put(_competitionFieldName, new Boolean(false));
		fieldValues.put(_coachesFieldName, "");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#updateFieldsDisplayStatus()
	 */
	public void updateFieldsDisplayStatus() {
		_unionField.setText((String) fieldValues.get(_unionFieldName));
		_cplayField.setContent((String) fieldValues.get(_cplayFieldName));
		_nameField.setContent((String) fieldValues.get(_nameFieldName));
		_competitionField.setChecked(((Boolean) fieldValues.get(_competitionFieldName)).booleanValue());		
		_coachesField.setContent((String) fieldValues.get(_coachesFieldName));		
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeFields()
	 */
	public void initializeFields() {
		_unionField = new Text();
		_nameField = new TextInput(_nameFieldName);
		_cplayField = new TextInput(_cplayFieldName);
		_competitionField = new CheckBox(_competitionFieldName);
		_coachesField = new TextArea(_coachesFieldName);
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initializeTexts()
	 */
	public void initializeTexts() {
			IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		_unionText = new Text(iwrb.getLocalizedString(_unionFieldName, "S�rsamband") + ":");
		_cplayText = new Text(iwrb.getLocalizedString(_cplayFieldName, "Flokkur") + ":");
		_nameText = new Text(iwrb.getLocalizedString(_nameFieldName, "Heiti flokks") + ":");
		_competitionText = new Text(iwrb.getLocalizedString(_competitionFieldName, "Keppendamerking") + ":");
		_coachesText = new Text(iwrb.getLocalizedString(_coachesFieldName, "�j�lfarar") + ":");
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#lineUpFields()
	 */
	public void lineUpFields() {
		Table t = new Table(2, 5);
		t.add(_unionText, 1, 1);
		t.add(_unionField, 2, 1);
		t.add(_cplayText, 1, 2);
		t.add(_cplayField, 2, 2);
		t.add(_nameText, 1, 3);
		t.add(_nameField, 2, 3);
		t.add(_competitionText, 1, 4);
		t.add(_competitionField, 2, 4);
		t.add(_coachesText, 1, 5);
		t.add(_coachesField, 2, 5);

		add(t);
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#collect(com.idega.presentation.IWContext)
	 */
	public boolean collect(IWContext iwc) {
		if (iwc != null) {
//			String union = iwc.getParameter(_unionFieldName);
			String flokkur = iwc.getParameter(_cplayFieldName);
			String name = iwc.getParameter(_nameFieldName);
			String comp = iwc.getParameter(_competitionFieldName);
			String coach = iwc.getParameter(_coachesFieldName);
			

//			if (union != null)
//				fieldValues.put(_unionFieldName, union);
//			else
//				fieldValues.put(_unionFieldName, "");
			if (flokkur != null)
				fieldValues.put(_cplayFieldName, flokkur);
			else
				fieldValues.put(_cplayFieldName, "");
			if (name != null)
				fieldValues.put(_nameFieldName, name);
			else
				fieldValues.put(_nameFieldName, "");
			fieldValues.put(_competitionFieldName, new Boolean(comp != null));
			if (coach != null)
				fieldValues.put(_coachesFieldName, coach);
			else
				fieldValues.put(_coachesFieldName, "");
				

			updateFieldsDisplayStatus();
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.util.datastructures.Collectable#store(com.idega.presentation.IWContext)
	 */
	public boolean store(IWContext iwc) {
		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));
			// get corressponding service bean
			ClubInformationPluginBusiness ageGenderPluginBusiness = getClubInformationPluginBusiness(iwc);

//			String union = (String) fieldValues.get(_unionFieldName);
			String flokkur = (String) fieldValues.get(_cplayFieldName);
			String name = (String) fieldValues.get(_nameFieldName);
			Boolean comp = (Boolean) fieldValues.get(_competitionFieldName);
			String coach = (String) fieldValues.get(_coachesFieldName);

//			group.setMetaData("CLUBPLAYER_UNION", union);
			group.setMetaData("CLUBPLAYER_FLOKKUR", flokkur);
			group.setMetaData("CLUBPLAYER_NAME", name);
			if (comp != null) {
				group.setMetaData("CLUBPLAYER_COMP", comp.toString());
			}
			else {
				group.setMetaData("CLUBPLAYER_COMP", Boolean.FALSE.toString());
			}
			group.setMetaData("CLUBPLAYER_COACH", coach);

			group.store();
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
			return false;
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.idega.user.presentation.UserGroupTab#initFieldContents()
	 */
	public void initFieldContents() {
		Group group;
		try {
			group = (Group) (((GroupHome) com.idega.data.IDOLookup.getHome(Group.class)).findByPrimaryKey(new Integer(getGroupId())));

//			String union = group.getMetaData("CLUBPLAYER_UNION");
			String flokkur = group.getMetaData("CLUBPLAYER_FLOKKUR");
			String name = group.getMetaData("CLUBPLAYER_NAME");
			String comp = group.getMetaData("CLUBPLAYER_COMP");
			String coach = group.getMetaData("CLUBPLAYER_COACH");

//			if (union != null)
//				fieldValues.put(_unionFieldName, union);
			if (flokkur != null)
				fieldValues.put(_cplayFieldName, flokkur);
			if (name != null)
				fieldValues.put(_nameFieldName, name);
			fieldValues.put(_competitionFieldName, new Boolean(comp!=null));
			if (coach != null)
				fieldValues.put(_coachesFieldName, coach);
			
			updateFieldsDisplayStatus();
		}
		catch (RemoteException e) {
			e.printStackTrace(System.err);
		}
		catch (FinderException e) {
			e.printStackTrace(System.err);
		}
	}

	public ClubInformationPluginBusiness getClubInformationPluginBusiness(IWApplicationContext iwc) {
		ClubInformationPluginBusiness business = null;
		if (business == null) {
			try {
				business = (ClubInformationPluginBusiness) com.idega.business.IBOLookup.getServiceInstance(iwc, ClubInformationPluginBusiness.class);
			}
			catch (java.rmi.RemoteException rme) {
				throw new RuntimeException(rme.getMessage());
			}
		}
		return business;
	}
}