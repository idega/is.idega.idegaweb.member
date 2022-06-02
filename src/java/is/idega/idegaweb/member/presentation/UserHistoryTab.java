package is.idega.idegaweb.member.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.idega.event.IWLinkEvent;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.help.presentation.Help;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.IFrame;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.GroupRelationHome;
import com.idega.user.data.User;
import com.idega.user.data.UserStatus;
import com.idega.user.data.UserStatusHome;
import com.idega.user.presentation.UserGroupList;
import com.idega.user.presentation.UserTab;
import com.idega.util.CoreConstants;

/**
 * @author Laddi
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class UserHistoryTab extends UserTab {
	private static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	private static final String TAB_NAME = "usr_his_tab_name";
	private static final String DEFAULT_TAB_NAME = "History";

	private static final String MEMBER_HELP_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";
	private static final String HELP_TEXT_KEY = "user_history_tab";


	private IFrame memberofFrame;

	public static final String PARAMETER_USER_ID = "ic_user_id";
	public static final String SESSIONADDRESS_USERGROUPS_HISTORY =
		"ic_user_ic_group_history";
	public static final String SESSIONADDRESS_USERGROUPS_STATUS =
		"ic_user_ic_group_status";

	protected Text memberof;

	public UserHistoryTab() {
		super();
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

		setName(iwrb.getLocalizedString(TAB_NAME, DEFAULT_TAB_NAME));
	}

	@Override
	public void initFieldContents() {
		updateFieldsDisplayStatus();
	}

	@Override
	public void updateFieldsDisplayStatus() {
	}

	@Override
	public void initializeFields() {
		this.memberofFrame = new IFrame("ic_user_history", UserHistoryList.class);
		this.memberofFrame.setHeight(280);
		this.memberofFrame.setWidth("100%");
		this.memberofFrame.setStyleAttribute("border", "1px #bbbbbb solid;");
		this.memberofFrame.setScrolling(IFrame.SCROLLING_YES);
	}

	public void actionPerformed(IWLinkEvent e) {
		this.collect(e.getIWContext());
	}

	@Override
	public void initializeTexts() {
		IWContext iwc = IWContext.getInstance();
		IWResourceBundle iwrb = getResourceBundle(iwc);

//		memberof = this.getTextObject();
		this.memberof = new Text(iwrb.getLocalizedString("usr_history","History"));
		this.memberof.setBold();
	}

	@Override
	public boolean store(IWContext iwc) {
		return true;
	}

	@Override
	public void lineUpFields() {
		this.resize(1, 1);
		setCellpadding(5);
		setCellspacing(0);

		this.add(this.memberof, 1, 1);
		add(Text.getBreak(), 1, 1);
		this.add(this.memberofFrame, 1, 1);
	}

	@Override
	public boolean collect(IWContext iwc) {
		return true;
	}

	@Override
	public void initializeFieldNames() {
	}

	@Override
	public void initializeFieldValues() {
		updateFieldsDisplayStatus();
	}

	public void dispose(IWContext iwc) {
		iwc.removeSessionAttribute(
			UserGroupList.SESSIONADDRESS_USERGROUPS_DIRECTLY_RELATED);
	}

	@Override
	public void main(IWContext iwc) throws Exception {
		if (getPanel() != null) {
			getPanel().addHelpButton(getHelpButton());
		}
		User viewedUser = getUser();
		User viewingUser = iwc.getCurrentUser();
		boolean isAdmin = iwc.isSuperAdmin();
		boolean isSameUser = viewedUser.getPrimaryKey().equals(viewingUser.getPrimaryKey());
		boolean checkNeeded = !(isAdmin || isSameUser);
		System.out.println("User " + viewingUser.getName() + " is viewing user " + viewedUser.getName() + ", checkNeede=" + checkNeeded);

		Collection groupRelations = ((GroupRelationHome) com.idega.data.IDOLookup.getHome(GroupRelation.class)).findAllGroupsRelationshipsByRelatedGroupOrderedByInitiationDate(getUserId(),CoreConstants.GROUP_RELATION_PARENT);
		if(checkNeeded) {
			groupRelations = getFilteredGroupRelations(iwc, Collections.unmodifiableCollection(groupRelations), viewingUser);
		}
		if (groupRelations != null) {
			iwc.setSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_HISTORY,
				groupRelations);
		}
		else {
			iwc.removeSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_HISTORY);
		}

		Collection statuses = ((UserStatusHome) com.idega.data.IDOLookup.getHome(UserStatus.class)).findAllByUserId(getUserId());
		if(checkNeeded) {
			statuses = getFilteredStatuses(iwc, Collections.unmodifiableCollection(statuses), viewingUser);
		}
		if (statuses != null) {
			iwc.setSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_STATUS,
				statuses);
		}
		else {
			iwc.removeSessionAttribute(
				UserHistoryTab.SESSIONADDRESS_USERGROUPS_STATUS);
		}

	}

	/**
	 * Filters Statuses by users persmission to see groups. Only statuses pertaining to a groups that are descendants of one of users top group
	 * nodes are returned
	 * @param iwc IWContext
	 * @param statuses The Statuses to filter
	 * @param user The user whos top groups nodes are used for the filtering
	 * @return All the Statuses in <code>statuses</code> that are for a group that is a descendant of one of <code>user</code> top groups
	 */
	private Collection getFilteredStatuses(IWContext iwc, Collection statuses, User user) {
		Collection result = new ArrayList();
		Iterator statusIter = statuses.iterator();
		while(statusIter.hasNext()) {
			UserStatus status = (UserStatus) statusIter.next();
			boolean ok = false;

			if (status.getGroupId() !=  -1) {
				ok = iwc.getAccessController().hasViewPermissionFor(status.getGroup(), iwc);
			}

			if(ok) {
				result.add(status);
			} else {
				if (status.getGroupId() !=  -1) {
					System.out.println("User status in group " + status.getGroup().getName() + " is filtered out");
				} else {
					System.out.println("User status: "+status.getStatus().getStatusKey()+" that was set: "+status.getDateFrom() + " is filtered out");
				}
			}
		}

		return result;
	}

	/**
	 * Filters GroupRelations by users persmission to see groups. Only GroupRelations pertaining to groups that are descendants of
	 * one of users top group nodes are returned
	 * @param iwc IWContext
	 * @param groupRelations The GroupRelations to filter
	 * @param user The user whos top groups nodes are used for the filtering
	 * @return All the GroupRelations in <code>groupRelations</code> that are for groups that are both descendants of one of <code>user</code> top groups
	 */
	private Collection getFilteredGroupRelations(IWContext iwc, Collection groupRelations, User user) {
		Collection result = new ArrayList();
		Iterator groupRelationIter = groupRelations.iterator();
		while(groupRelationIter.hasNext()) {
			GroupRelation rel = (GroupRelation) groupRelationIter.next();
			boolean ok = false;

			ok = iwc.getAccessController().hasViewPermissionFor(rel.getGroup(), iwc);

			if(ok) {
				result.add(rel);
			} else {
				System.out.println("Group relation between " + rel.getGroup().getName() + " and " + rel.getRelatedGroup().getName() + " not shown");
			}
		}

		return result;
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


	@Override
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}