/*
 * Created on Jun 24, 2006
 * 
 */
package is.idega.idegaweb.member.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.ui.UserEmailDropDownMenu;

/**
 * @author Sigtryggur
 * 
 */
public class GroupStatsWindow extends GenericStatsWindow {

	public GroupStatsWindow() {
		super();
		setHeight(400);
		setWidth(400);
		setScrollbar(true);
	}

	public void main(IWContext iwc) throws Exception {
		this.iwrb = getResourceBundle(iwc);
		this.windowTitle = this.iwrb.getLocalizedString(
				"groupstatswindow.groupstats", "Group Report");
		this.invocationFileName = "Invocation-GroupStats.xml";
		this.layoutFileName = "Layout-GroupStats.xml";

		this.setRunAsThread(true);

		String email = iwc.getParameter("dr_sendToEmail");
		String excel = iwc.getParameter("dr_excel");
		String excelNoStylesheet = iwc.getParameter("dr_excelNoStylesheet");
		String pdf = iwc.getParameter("dr_pdf");
		String xml = iwc.getParameter("dr_xml");
		String html = iwc.getParameter("dr_html");

		if (email != null && !"".equals(email.trim())) {
			UserEmailDropDownMenu tmp = new UserEmailDropDownMenu();
			email = tmp.getDisplayForResultingObject(email, iwc);
			this.setEmail(email);
		}

		if (excel != null) {
			this.setGenerateExcelReport(true);
		} else {
			this.setGenerateExcelReport(false);
		}

		if (excelNoStylesheet != null) {
			this.setGenerateSimpleExcelReport(true);
		} else {
			this.setGenerateSimpleExcelReport(false);
		}

		if (pdf != null) {
			this.setGeneratePDFReport(true);
		} else {
			this.setGeneratePDFReport(false);
		}

		if (xml != null) {
			this.setGenerateXMLReport(true);
		} else {
			this.setGenerateXMLReport(false);
		}

		if (html != null) {
			this.setGenerateHTMLReport(true);
		} else {
			this.setGenerateHTMLReport(false);
		}

		System.out.println("email = " + email);
		System.out.println("excel = " + excel);
		System.out.println("excelNoStylesheet = " + excelNoStylesheet);
		System.out.println("pdf = " + pdf);
		System.out.println("xml = " + xml);
		System.out.println("html = " + html);

		super.main(iwc);
	}
}