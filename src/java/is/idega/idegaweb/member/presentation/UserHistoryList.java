/*
 * $Id$
 *
 * Copyright (C) 2000-2003 Idega Software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega Software.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.presentation;

import java.util.Collection;
import java.util.Iterator;

import com.idega.presentation.IWContext;
import com.idega.presentation.Page;
import com.idega.presentation.Table;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.UserStatus;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class UserHistoryList extends Page {

	private Collection groups = null;

	public UserHistoryList() {
		super();
	}

	public Table getGroupTable(IWContext iwc) {
		System.out.println("Getting group table ");
		Collection history = (Collection) iwc.getSessionAttribute(UserHistoryTab.SESSIONADDRESS_USERGROUPS_HISTORY);
		Collection status = (Collection) iwc.getSessionAttribute(UserHistoryTab.SESSIONADDRESS_USERGROUPS_STATUS);

		Table table = null;
		try {
			Iterator iter = null;
			int row = 1;
			if (history != null || status != null) {
				int size = 0;
				if (history != null)
					size += history.size();
				if (status != null)
					size += status.size();
				table = new Table(4, size + 1);
				table.add("H�pur", 1, row);
				table.add("Virkni", 2, row);
				table.add("Upphafsdags.", 3, row);
				table.add("Lokadags.", 4, row++);

				if (history != null) {
					iter = history.iterator();
					while (iter.hasNext()) {
						Object item = iter.next();
						if (item != null) {
							GroupRelation rel = (GroupRelation) item;

							IWTimestamp from = new IWTimestamp(rel.getInitiationDate());
							IWTimestamp to = null;
							if (rel.getTerminationDate() != null)
								to = new IWTimestamp(rel.getTerminationDate());

							table.add(rel.getGroup().getName(), 1, row);
							table.add(from.getDateString("dd-MM-yyyy"), 3, row);
							if (to != null)
								table.add(to.getDateString("dd-MM-yyyy"), 4, row);
							row++;
						}
					}
				}

				if (status != null) {
					iter = status.iterator();
					while (iter.hasNext()) {
						Object item = iter.next();
						if (item != null) {
							UserStatus stat = (UserStatus) item;

							IWTimestamp from = new IWTimestamp(stat.getDateFrom());
							IWTimestamp to = null;
							if (stat.getDateTo() != null)
								to = new IWTimestamp(stat.getDateTo());

							table.add(stat.getGroup().getName(), 1, row);
							table.add(stat.getStatus().getStatusKey(), 2, row);
							table.add(from.getDateString("dd-MM-yyyy"), 3, row);
							if (to != null)
								table.add(to.getDateString("dd-MM-yyyy"), 4, row);
							row++;
						}
					}
				}

			}
		}
		catch (Exception e) {
			add("Error: " + e.getMessage());
			e.printStackTrace();
		}

		if (table != null) {
			table.setWidth("100%");
		}

		return table;
	}

	public void main(IWContext iwc) throws Exception {
		this.getParentPage().setAllMargins(0);
		Table tb = getGroupTable(iwc);
		if (tb != null) {
			this.add(tb);
		}
	}
}