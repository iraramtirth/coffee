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

package tigase.xmpp.impl;

//~--- non-JDK imports --------------------------------------------------------

import tigase.xml.Element;

import tigase.xmpp.JID;
import tigase.xmpp.NotAuthorizedException;
import tigase.xmpp.XMPPResourceConnection;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 * Created: Nov 28, 2008 10:27:55 PM
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class DynamicRosterTest implements DynamicRosterIfc {
	private static Logger log = Logger.getLogger(DynamicRosterTest.class.getName());

	//~--- fields ---------------------------------------------------------------

	private Map<String, Element> memStorage = new LinkedHashMap<String, Element>();

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param session
	 *
	 * @return
	 *
	 * @throws NotAuthorizedException
	 */
	@Override
	public JID[] getBuddies(XMPPResourceConnection session) throws NotAuthorizedException {
		return new JID[] { JID.jidInstanceNS("dynrost@test-d") };
	}

	/**
	 * Method description
	 *
	 *
	 * @param session
	 * @param buddy
	 *
	 * @return
	 *
	 * @throws NotAuthorizedException
	 */
	@Override
	public Element getBuddyItem(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException {
		if ("dynrost@test-d".equals(buddy.getBareJID().toString())) {
			return getBuddy();
		} else {
			return null;
		}
	}

	/**
	 * Method description
	 *
	 *
	 * @param item
	 *
	 * @return
	 */
	@Override
	public Element getItemExtraData(Element item) {
		String jid = item.getAttribute("jid");
		Element result = memStorage.get(jid);

		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "Retrieving item: {0}, for jid={1}", new Object[] { result, jid });
		}

		return result;
	}

	/**
	 * Method description
	 *
	 *
	 * @param session
	 *
	 * @return
	 *
	 * @throws NotAuthorizedException
	 */
	@Override
	public List<Element> getRosterItems(XMPPResourceConnection session)
			throws NotAuthorizedException {
		return new ArrayList<Element>(Arrays.asList(getBuddy()));
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param props
	 */
	@Override
	public void init(Map<String, Object> props) {}

	/**
	 * Method description
	 *
	 *
	 * @param par
	 */
	@Override
	public void init(String par) {}

	//~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param item
	 */
	@Override
	public void setItemExtraData(Element item) {
		String jid = item.getAttribute("jid");

		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "Storing item: {0}, for jid={1}", new Object[] { item, jid });
		}

		memStorage.put(jid, item);
	}

	//~--- get methods ----------------------------------------------------------

	private Element getBuddy() {
		return new Element("item", new Element[] { new Element("group", "test group") },
				new String[] { "jid",
				"name", "subscription" }, new String[] { "dynrost@test-d", "dynrost", "both" });
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
