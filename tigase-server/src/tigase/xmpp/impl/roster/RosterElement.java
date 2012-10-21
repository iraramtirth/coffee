/*
 *   Tigase Jabber/XMPP Server
 *  Copyright (C) 2004-2012 "Artur Hefczyc" <artur.hefczyc@tigase.org>
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

package tigase.xmpp.impl.roster;

//~--- non-JDK imports --------------------------------------------------------

import tigase.util.TigaseStringprepException;
import tigase.util.XMPPStringPrepFactory;

import tigase.xml.Element;
import tigase.xml.XMLUtils;

import tigase.xmpp.JID;
import tigase.xmpp.XMPPResourceConnection;

import static tigase.xmpp.impl.roster.RosterAbstract.SubscriptionType;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 * Describe class RosterElement here.
 * 
 * 
 * Created: Wed Oct 29 14:21:16 2008
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class RosterElement implements RosterElementIfc {
	private static final Logger log = Logger.getLogger(RosterElement.class.getName());
	private static final String ELEM_NAME = "contact";
	private static final String JID_ATT = "jid";
	private static final String NAME_ATT = "name";
	private static final String SUBS_ATT = "subs";
	private static final String GRP_ATT = "groups";
	private static final String OTHER_ATT = "other";
	private static final String STRINGPREP_ATT = "preped";
	private static final String ACTIVITY_ATT = "activity";
	private static final String WEIGHT_ATT = "weight";
	private static final String LAST_SEEN_ATT = "last-seen";

	private static final double INITIAL_ACTIVITY_VAL = 1d;
	private static final double INITIAL_WEIGHT_VAL = 1d;
	protected static final long INITIAL_LAST_SEEN_VAL = 1000l;

	// ~--- fields ---------------------------------------------------------------

	private String[] groups = null;
	private JID jid = null;
	private String name = null;
	private String otherData = null;
	private long lastSeen = INITIAL_LAST_SEEN_VAL;
	private double activity = INITIAL_ACTIVITY_VAL;
	private double weight = INITIAL_WEIGHT_VAL;
	private XMPPResourceConnection session = null;
	private String stringpreped = null;
	private SubscriptionType subscription = null;
	private boolean presence_sent = false;
	private Map<String, Boolean> onlineMap = new HashMap<String, Boolean>();

	// private Element item = null;
	// private boolean online = false;
	private boolean modified = false;
	private boolean persistent = true;

	// ~--- constructors ---------------------------------------------------------

	/**
	 * Creates a new <code>RosterElement</code> instance.
	 * 
	 * 
	 * @param roster_el
	 * @param session
	 * @throws TigaseStringprepException
	 */
	public RosterElement(Element roster_el, XMPPResourceConnection session)
			throws TigaseStringprepException {
		this.session = session;

		if (roster_el.getName() == ELEM_NAME) {
			this.stringpreped = roster_el.getAttribute(STRINGPREP_ATT);
			setJid(roster_el.getAttribute(JID_ATT));
			setName(roster_el.getAttribute(NAME_ATT));

			if (roster_el.getAttribute(SUBS_ATT) == null) {
				subscription = SubscriptionType.none;
			} else {
				subscription = SubscriptionType.valueOf(roster_el.getAttribute(SUBS_ATT));
			}

			String grps = roster_el.getAttribute(GRP_ATT);

			if ((grps != null) && !grps.trim().isEmpty()) {
				groups = grps.split(",");
			}

			String other_data = roster_el.getAttribute(OTHER_ATT);

			if ((other_data != null) && !other_data.trim().isEmpty()) {
				otherData = other_data;
			}

			String num_str = roster_el.getAttribute(ACTIVITY_ATT);
			if (num_str != null) {
				try {
					activity = Double.parseDouble(num_str);
				} catch (NumberFormatException nfe) {
					log.warning("Incorrect activity field: " + num_str);
					activity = INITIAL_ACTIVITY_VAL;
				}
			}

			num_str = roster_el.getAttribute(WEIGHT_ATT);
			if (num_str != null) {
				try {
					weight = Double.parseDouble(num_str);
				} catch (NumberFormatException nfe) {
					log.warning("Incorrect weight field: " + num_str);
					weight = INITIAL_WEIGHT_VAL;
				}
			}

			num_str = roster_el.getAttribute(LAST_SEEN_ATT);
			if (num_str != null) {
				try {
					lastSeen = Long.parseLong(num_str);
				} catch (NumberFormatException nfe) {
					log.warning("Incorrect last seen field: " + num_str);
					lastSeen = INITIAL_LAST_SEEN_VAL;
				}
			}

		} else {
			log.warning("Incorrect roster data: " + roster_el.toString());
		}
	}

	/**
	 * Constructs ...
	 * 
	 * 
	 * @param jid
	 * @param name
	 * @param groups
	 * @param session
	 */
	public RosterElement(JID jid, String name, String[] groups,
			XMPPResourceConnection session) {
		this.stringpreped = XMPPStringPrepFactory.STRINGPREP_PROCESSOR;
		this.session = session;
		setJid(jid);
		setName(name);
		this.groups = groups;
		this.subscription = SubscriptionType.none;
	}

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param groups
	 */
	public void addGroups(String[] groups) {
		if (groups != null) {
			if (this.groups == null) {
				this.groups = groups;
			} else {

				// Groups names must be unique
				Set<String> groupsSet = new HashSet<String>();

				for (String group : this.groups) {
					groupsSet.add(group);
				}

				for (String group : groups) {
					groupsSet.add(group);
				}

				this.groups = groupsSet.toArray(new String[groupsSet.size()]);
			}
		}

		// item = null;
	}

	// ~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public String[] getGroups() {
		return groups;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public JID getJid() {
		return jid;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public String getOtherData() {
		return otherData;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public Element getRosterElement() {
		Element elem =
				new Element(ELEM_NAME,
						new String[] { JID_ATT, SUBS_ATT, NAME_ATT, STRINGPREP_ATT }, new String[] {
								jid.toString(), subscription.toString(), name, "" + stringpreped });

		if ((groups != null) && (groups.length > 0)) {
			String grps = "";

			for (String group : groups) {
				grps += group + ",";
			}

			grps = grps.substring(0, grps.length() - 1);
			elem.setAttribute(GRP_ATT, grps);
		}

		if (otherData != null) {
			elem.setAttribute(OTHER_ATT, otherData);
		}

		elem.setAttribute(ACTIVITY_ATT, Double.toString(activity));
		elem.setAttribute(WEIGHT_ATT, Double.toString(weight));
		elem.setAttribute(LAST_SEEN_ATT, Long.toString(lastSeen));

		modified = false;

		return elem;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public Element getRosterItem() {

		// This is actually not a good idea to cache the item element.
		// This causes a huge memory consumption and usually the item
		// is needed only once at the roster retrieving time.
		// if (item == null) {
		Element item = new Element("item");

		item.setAttribute("jid", jid.toString());
		item.addAttributes(subscription.getSubscriptionAttr());

		if (name != null) {
			item.setAttribute("name", XMLUtils.escape(name));
		}

		if (groups != null) {
			for (String gr : groups) {
				Element group = new Element("group");

				group.setCData(XMLUtils.escape(gr));
				item.addChild(group);
			} // end of for ()
		} // end of if-else

		// }
		return item;
	}

	public String toString() {
		return getRosterItem().toString();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public SubscriptionType getSubscription() {
		return subscription;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public boolean isModified() {
		return modified;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public boolean isOnline() {
		return onlineMap.size() > 0;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public boolean isPresence_sent() {
		return presence_sent;
	}

	// ~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param groups
	 */
	public void setGroups(String[] groups) {
		this.groups = groups;
		modified = true;

		// item = null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param name
	 */
	public void setName(String name) {
		if (name == null) {
			this.name = this.jid.getLocalpart();

			if ((this.name == null) || this.name.trim().isEmpty()) {
				this.name = this.jid.getBareJID().toString();
			}
			modified = true;
		} else {
			this.name = name;
		}

	}

	/**
	 * Method description
	 * 
	 * 
	 * @param online
	 */
	public void setOnline(String resource, boolean online) {
		if (onlineMap != null) {
			if (online) {
				onlineMap.put(resource, Boolean.TRUE);
			} else {
				onlineMap.remove(resource);
			}
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param other_data
	 */
	public void setOtherData(String other_data) {
		otherData = other_data;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param presence_sent
	 */
	public void setPresence_sent(boolean presence_sent) {
		this.presence_sent = presence_sent;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param subscription
	 */
	public void setSubscription(SubscriptionType subscription) {
		if (subscription == null) {
			this.subscription = SubscriptionType.none;
		} else {
			this.subscription = subscription;
		}

		modified = true;

		// item = null;
	}

	private void setJid(JID jid) {
		this.jid = jid;
		modified = true;
	}

	private void setJid(String jid) throws TigaseStringprepException {
		if (XMPPStringPrepFactory.STRINGPREP_PROCESSOR.equals(stringpreped)) {
			this.jid = JID.jidInstanceNS(jid);
		} else {
			this.jid = JID.jidInstance(jid);
			modified = true;
		}

		stringpreped = XMPPStringPrepFactory.STRINGPREP_PROCESSOR;
	}

	/**
	 * @return
	 */
	public boolean isPersistent() {
		return persistent;
	}

	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	/**
	 * @return the activity
	 */
	public double getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *          the activity to set
	 */
	public void setActivity(double activity) {
		this.activity = activity;
		if (activity != 0) {
			weight = 1 / activity;
		}
		modified = true;
	}

	/**
	 * @return the weight
	 */
	public double getWeight() {
		return weight;
	}

	/**
	 * @param weight
	 *          the weight to set
	 */
	public void setWeight(double weight) {
		this.weight = weight;
		modified = true;

	}

	/**
	 * @return the lastSeen
	 */
	public long getLastSeen() {
		return lastSeen;
	}

	/**
	 * @param lastSeen the lastSeen to set
	 */
	public void setLastSeen(long lastSeen) {
		this.lastSeen = lastSeen;
		modified = true;
	}

}
