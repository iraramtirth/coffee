/*
 * Tigase Jabber/XMPP Server
 * Copyright (C) 2004-2012 "Artur Hefczyc" <artur.hefczyc@tigase.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 *
 * $Rev: 2996 $
 * Last modified by $Author: wojtek $
 * $Date: 2012-08-21 06:29:57 +0800 (Tue, 21 Aug 2012) $
 */

package tigase.server.script;

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedHashMap;
import java.util.Map;

//~--- classes ----------------------------------------------------------------

/**
 * Created: Jan 2, 2009 2:32:17 PM
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public abstract class AbstractScriptCommand implements CommandIfc {

	/** Field description */
	public static final Map<String, String> lineCommentStart = new LinkedHashMap<String, String>(20);

	//~--- static initializers --------------------------------------------------

	static {
		lineCommentStart.put("groovy", "//");
		lineCommentStart.put("scala", "//");
		lineCommentStart.put("python", "#");
		lineCommentStart.put("py", "#");
		lineCommentStart.put("js", "//");
		lineCommentStart.put("ruby", "#");
		lineCommentStart.put("rb", "#");
		lineCommentStart.put("perl", "#");
		lineCommentStart.put("pl", "#");
		lineCommentStart.put("awk", "//");
		lineCommentStart.put("lisp", ";");
		lineCommentStart.put("el", ";");
		lineCommentStart.put("cl", ";");
		lineCommentStart.put("gc1", ";");
		lineCommentStart.put("gc3", ";");
		lineCommentStart.put("java", "//");
	}

	//~--- fields ---------------------------------------------------------------

	private String commandId = null;
	private String description = null;
	private boolean adminOnly = true;

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public String getCommandId() {
		return this.commandId;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param id
	 * @param description
	 */
	@Override
	public void init(String id, String description) {
		this.commandId = id;
		this.description = description;
	}

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public boolean isAdminOnly() {
		return adminOnly;
	}

	//~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param adminOnly
	 */
	@Override
	public void setAdminOnly(boolean adminOnly) {
		this.adminOnly = adminOnly;
	}

	//~--- get methods ----------------------------------------------------------

	protected boolean isEmpty(String val) {
		return (val == null) || val.isEmpty();
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
