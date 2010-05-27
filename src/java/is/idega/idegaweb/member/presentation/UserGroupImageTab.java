package is.idega.idegaweb.member.presentation;

import com.idega.block.media.presentation.ImageInserter;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.presentation.UserTab;

public class UserGroupImageTab extends UserTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "usr_group_imag_tab_name";
	private static final String DEFAULT_TAB_NAME = "User group image";

	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "user_group_logo_tab";

	private ImageInserter imageField;
	private String imageFieldName;
	private Text imageText;

	private CheckBox removeImageField;
	private String removeImageFieldName;
	private Text removeImageText;

	private int systemImageId = -1;

	public UserGroupImageTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	@Override
	public boolean collect(IWContext iwc) {
		String imageID = iwc.getParameter(this.imageFieldName + "-"
				+ getUserId() + "-" + getGroupID());
		if (imageID != null) {
			this.fieldValues.put(this.imageFieldName, imageID);
		}

		this.fieldValues.put(this.removeImageFieldName, new Boolean(iwc
				.isParameterSet(this.removeImageFieldName)));

		return true;
	}

	@Override
	public void initFieldContents() {
		try {
			this.imageField.setImSessionImageName(this.imageFieldName + "-"
					+ getUserId() + "-" + getGroupID());

			try {
				this.systemImageId = Integer.parseInt(getUser().getMetaData(
						"group_image-" + getGroupID()));
			} catch (NumberFormatException n) {
				this.systemImageId = -1;
			}

			if (this.systemImageId != -1) {
				this.fieldValues.put(this.imageFieldName, Integer
						.toString(this.systemImageId));
			}

			this.fieldValues.put(this.removeImageFieldName, new Boolean(false));

			updateFieldsDisplayStatus();
		} catch (Exception e) {
			e.printStackTrace();
			this.systemImageId = -1;
			System.err
					.println("UserGroupImageTab error initFieldContents, userId : "
							+ getUserId() + ", groupID : " + getGroupID());
		}

	}

	@Override
	public void initializeFieldNames() {
		this.imageFieldName = "usr_grp_imag_userSystemImageId";
		this.removeImageFieldName = "usr_grp_imag_removeImageFieldName";
	}

	@Override
	public void initializeFieldValues() {
		this.systemImageId = -1;
		this.fieldValues.put(this.removeImageFieldName, new Boolean(false));
	}

	@Override
	public void initializeFields() {
		this.imageField = new ImageInserter(this.imageFieldName + "-"
				+ getUserId() + "-" + getGroupID());
		this.imageField.setHasUseBox(false);
		this.removeImageField = new CheckBox(this.removeImageFieldName);
		this.removeImageField.setWidth("10");
		this.removeImageField.setHeight("10");
	}

	@Override
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		this.imageText = new Text(iwrb.getLocalizedString(this.imageFieldName,
				"Image"));
		this.imageText.setBold();

		this.removeImageText = new Text(iwrb.getLocalizedString(
				this.removeImageFieldName, "do not show an image"));
		this.removeImageText.setBold();
	}

	@Override
	public void lineUpFields() {
		this.resize(1, 1);

		Table imageTable = new Table(1, 2);
		imageTable.setWidth(Table.HUNDRED_PERCENT);
		imageTable.setCellpadding(5);
		imageTable.setCellspacing(0);

		imageTable.add(this.imageText, 1, 1);
		imageTable.add(Text.getBreak(), 1, 1);
		imageTable.add(this.imageField, 1, 1);

		imageTable.add(this.removeImageField, 1, 2);
		imageTable.add(this.removeImageText, 1, 2);

		this.add(imageTable, 1, 1);
	}

	@Override
	public boolean store(IWContext iwc) {
		try {
			if (getUser() != null && getGroupID() > -1) {

				String image = (String) this.fieldValues
						.get(this.imageFieldName);

				if ((image != null) && (!image.equals("-1"))
						&& (!image.equals(""))) {
					int tempId;
					if (((Boolean) this.fieldValues
							.get(this.removeImageFieldName)).booleanValue()) {
						getUser().setMetaData("group_image-" + getGroupID(),
								"-1");
						// set variables to default values
						this.systemImageId = -1;
						this.fieldValues.put(this.imageFieldName, "-1");
						getUser().store();
						updateFieldsDisplayStatus();
					} else if ((tempId = Integer.parseInt(image)) != this.systemImageId) {
						this.systemImageId = tempId;
						getUser().setMetaData("group_image-" + getGroupID(),
								Integer.toString(this.systemImageId));
						getUser().store();
						updateFieldsDisplayStatus();
					}

					iwc.removeSessionAttribute(this.imageFieldName + "-"
							+ getUserId() + "-" + getGroupID());

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("update group exception");
		}
		return true;
	}

	@Override
	public void updateFieldsDisplayStatus() {
		this.imageField.setImageId(this.systemImageId);
		this.removeImageField.setChecked(((Boolean) this.fieldValues
				.get(this.removeImageFieldName)).booleanValue());
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}

}