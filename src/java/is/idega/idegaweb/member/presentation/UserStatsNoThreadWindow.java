/*
 * Created on Jan 3, 2005
 *
 */
package is.idega.idegaweb.member.presentation;

import com.idega.presentation.IWContext;

/**
 * @author Sigtryggur
 *
 */
public class UserStatsNoThreadWindow extends GenericStatsWindow {
    
    public UserStatsNoThreadWindow() {
        super();
		setHeight(700);
		setWidth(400);
		setScrollbar(true);
    }

    public void main(IWContext iwc) throws Exception {
    	this.iwrb = getResourceBundle(iwc);
    	this.windowTitle = this.iwrb.getLocalizedString("userstatsnohreadwindow.userstats", "User Report No Thread");
		this.invocationFileName = "Invocation-UserStatsNoThread.xml";
		this.layoutFileName = "Layout-UserStats.xml";

		this.setRunAsThread(false);			

		super.main(iwc);		
    }
}