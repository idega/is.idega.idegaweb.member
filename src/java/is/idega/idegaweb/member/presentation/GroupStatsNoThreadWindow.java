/*
 * Created on Jun 24, 2006
 * 
 */
package is.idega.idegaweb.member.presentation;

import com.idega.presentation.IWContext;

/**
 * @author Sigtryggur
 * 
 */
public class GroupStatsNoThreadWindow extends GenericStatsWindow {

	public GroupStatsNoThreadWindow() {
		super();
		setHeight(400);
		setWidth(400);
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		this.iwrb = getResourceBundle(iwc);
		this.windowTitle = this.iwrb.getLocalizedString(
				"groupstatsnothreadwindow.groupstats", "Group Report No Thread");
		this.invocationFileName = "Invocation-GroupStatsNoThread.xml";
		this.layoutFileName = "Layout-GroupStats.xml";

		this.setRunAsThread(false);

		super.main(iwc);
	}
}