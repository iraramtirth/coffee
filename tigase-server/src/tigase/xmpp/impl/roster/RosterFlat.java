/*
 * Tigase Jabber/XMPP Server
 * Copyright (C) 2004-2012 "Artur Hefczyc" <artur.hefczyc@tigase.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
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

import tigase.db.TigaseDBException;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;
import tigase.xml.SingletonFactory;

import tigase.xmpp.BareJID;
import tigase.xmpp.JID;
import tigase.xmpp.NotAuthorizedException;
import tigase.xmpp.XMPPResourceConnection;

//~--- JDK imports ------------------------------------------------------------

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 * Describe class RosterFlat here.
 * 
 * 
 * Created: Tue Feb 21 18:05:53 2006
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class RosterFlat extends RosterAbstract {

	/**
	 * Private logger for class instances.
	 */
	private static final Logger log = Logger.getLogger(RosterFlat.class.getName());
	private static final SimpleParser parser = SingletonFactory.getParserInstance();
	private static int maxRosterSize = new Long(Runtime.getRuntime().maxMemory() / 250000L)
			.intValue();

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param relem
	 * @param roster
	 * 
	 * @return
	 */
	public static boolean addBuddy(RosterElement relem, Map<BareJID, RosterElement> roster) {
		if (roster.size() < maxRosterSize) {
			roster.put(relem.getJid().getBareJID(), relem);

			return true;
		}

		return false;
	}

	public RosterElement addTempBuddy(JID buddy, XMPPResourceConnection session)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElementInstance(buddy, null, null, session);
		relem.setPersistent(false);
		addBuddy(relem, getUserRoster(session));
		return relem;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param roster_str
	 * @param roster
	 * @param session
	 * 
	 * @return
	 */
	public static boolean parseRosterUtil(String roster_str,
			Map<BareJID, RosterElement> roster, XMPPResourceConnection session) {
		boolean result = false;
		DomBuilderHandler domHandler = new DomBuilderHandler();

		parser.parse(domHandler, roster_str.toCharArray(), 0, roster_str.length());

		Queue<Element> elems = domHandler.getParsedElements();

		if ((elems != null) && (elems.size() > 0)) {
			for (Element elem : elems) {
				try {
					RosterElement relem = new RosterElement(elem, session);

					result |= relem.isModified();

					if (!addBuddy(relem, roster)) {
						break;
					}
				} catch (Exception e) {
					log.log(Level.WARNING, "Can't load roster element: {0}", elem);
				}
			}
		}

		return result;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param buddy
	 * @param name
	 * @param groups
	 * @param otherData
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	public void addBuddy(XMPPResourceConnection session, JID buddy, String name,
			String[] groups, String otherData) throws NotAuthorizedException, TigaseDBException {

		// String buddy = JIDUtils.getNodeID(jid);
		RosterElement relem = getRosterElement(session, buddy);

		if (relem == null) {
			Map<BareJID, RosterElement> roster = getUserRoster(session);

			relem = getRosterElementInstance(buddy, name, groups, session);
			relem.setOtherData(otherData);

			if (addBuddy(relem, roster)) {
				saveUserRoster(session);
			} else {
				throw new TigaseDBException("Too many elements in the user roster.");
			}

			if (log.isLoggable(Level.FINEST)) {
				log.log(Level.FINEST, "Added buddy to roster: {0}", buddy);
			}
		} else {
			if ((name != null) && !name.isEmpty()) {
				relem.setName(name);
			}

			// Hm, as one user reported this make it impossible to remove the user
			// from
			// all groups. Let's comments it out for now to see how it works.
			// Probably added this some time ago , before RosterFlat to prevent NPE.
			// if ((groups != null) && (groups.length > 0)) {
			relem.setGroups(groups);

			// }
			saveUserRoster(session);

			if (log.isLoggable(Level.FINEST)) {
				log.log(Level.FINEST, "Updated buddy in roster: {0}", buddy);
			}
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param buddy
	 * @param groups
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	public boolean
			addBuddyGroup(XMPPResourceConnection session, JID buddy, String[] groups)
					throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem != null) {
			relem.addGroups(groups);

			// Intentionally not saving the roster here.
			// At the moment it is only used to combine dynamic roster with the
			// static roster in case a contact exist in both but in a different
			// group.
			return true;
		} else {
			return false;
		}
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
	 * @throws TigaseDBException
	 */
	@Override
	public boolean containsBuddy(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);
		return relem != null && relem.isPersistent();
	}

	// ~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	// public String[] getBuddies(XMPPResourceConnection session,
	// boolean onlineOnly)
			public
			JID[] getBuddies(XMPPResourceConnection session) throws NotAuthorizedException,
					TigaseDBException {
		Map<BareJID, RosterElement> roster = getUserRoster(session);
		
		if (roster.size() == 0) {
			return null;
		}
		
		JID[] result = new JID[roster.size()];
		int idx = 0;

		for (RosterElement rosterElement : roster.values()) {
			result[idx++] = rosterElement.getJid();
		}
		// TODO: this sorting should be optional as it may impact performance
		Arrays.sort(result, new RosterElemComparator(roster));

		return result;

		// if (onlineOnly) {
		// ArrayList<String> online = new ArrayList<String>();
		// for (Map.Entry<String, RosterElement> rosterEl : roster.entrySet()) {
		// if (rosterEl.getValue().isOnline()) {
		// online.add(rosterEl.getKey());
		// }
		// }
		// return online.toArray(new String[online.size()]);
		// } else {
		// }
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
	 * @throws TigaseDBException
	 */
	@Override
	public String[] getBuddyGroups(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem == null) {
			return null;
		} else {
			return relem.getGroups();
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param relem
	 * 
	 * @return
	 */
	public Element getBuddyItem(RosterElement relem) {
		return relem.getRosterItem();
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
	 * @throws TigaseDBException
	 */
	@Override
	public Element getBuddyItem(final XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem == null) {
			return null;
		} else {
			return getBuddyItem(relem);
		}
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
	 * @throws TigaseDBException
	 */
	@Override
	public String getBuddyName(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem == null) {
			return null;
		} else {
			return relem.getName();
		}
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
	 * @throws TigaseDBException
	 */
	@Override
	public SubscriptionType getBuddySubscription(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem == null) {
			return null;
		} else {
			return relem.getSubscription();
		}

		// return SubscriptionType.both;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param buddy
	 * @param name
	 * @param groups
	 * @param session
	 * 
	 * @return
	 */
	public RosterElement getRosterElementInstance(JID buddy, String name, String[] groups,
			XMPPResourceConnection session) {
		return new RosterElement(buddy, name, groups, session);
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
	 * @throws TigaseDBException
	 */
	@Override
	public List<Element> getRosterItems(XMPPResourceConnection session)
			throws NotAuthorizedException, TigaseDBException {
		LinkedList<Element> items = new LinkedList<Element>();
		Map<BareJID, RosterElement> roster = getUserRoster(session);

		for (RosterElement relem : roster.values()) {

			// Skip temporary roster elements added only for online presence tracking
			// from dynamic roster
			if (relem.isPersistent()) {
				items.add(getBuddyItem(relem));
			}

		}

		return items;
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
	 * @throws TigaseDBException
	 */
	@Override
	public boolean isOnline(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		return (relem != null) && relem.isOnline();
	}

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param roster_str
	 * @param roster
	 * @param session
	 * 
	 * @return
	 */
	public boolean parseRoster(String roster_str, Map<BareJID, RosterElement> roster,
			XMPPResourceConnection session) {
		return parseRosterUtil(roster_str, roster, session);
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
	 * @throws TigaseDBException
	 */
	@Override
	public boolean presenceSent(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		return (relem != null) && relem.isPresence_sent();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param jid
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	public boolean removeBuddy(XMPPResourceConnection session, JID jid)
			throws NotAuthorizedException, TigaseDBException {
		Map<BareJID, RosterElement> roster = getUserRoster(session);

		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "Removing roster buddy: {0}, before removal: {1}",
					new Object[] { jid, roster });
		}
		roster.remove(jid.getBareJID());
		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "Removing roster buddy: {0}, after removal: {1}",
					new Object[] { jid, roster });
		}
		saveUserRoster(session);

		return true;
	}

	// ~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param buddy
	 * @param name
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	public void setBuddyName(XMPPResourceConnection session, JID buddy, String name)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem != null) {
			if (log.isLoggable(Level.FINEST)) {
				log.log(Level.FINEST, "Setting name: ''{0}'' for buddy: {1}", new Object[] {
						name, buddy });
			}

			if ((name != null) && !name.isEmpty()) {
				relem.setName(name);
			}

			saveUserRoster(session);
		} else {
			log.log(Level.WARNING, "Setting buddy name for non-existen contact: {0}", buddy);
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param subscription
	 * @param buddy
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	public void setBuddySubscription(XMPPResourceConnection session,
			SubscriptionType subscription, JID buddy) throws NotAuthorizedException,
			TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem != null) {
			relem.setSubscription(subscription);
			saveUserRoster(session);
		} else {
			log.log(Level.WARNING, "Missing roster contact for subscription set: {0}", buddy);
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param buddy
	 * @param online
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	public void setOnline(XMPPResourceConnection session, JID buddy, boolean online)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem == null) {
			relem = addTempBuddy(buddy, session);
		}

		relem.setOnline(buddy.getResource(), online);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param buddy
	 * @param sent
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	public void setPresenceSent(XMPPResourceConnection session, JID buddy, boolean sent)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement relem = getRosterElement(session, buddy);

		if (relem == null) {
			relem = addTempBuddy(buddy, session);
		}
		relem.setPresence_sent(sent);
	}

	// ~--- get methods ----------------------------------------------------------

	public RosterElement getRosterElement(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		Map<BareJID, RosterElement> roster = getUserRoster(session);

		return roster.get(buddy.getBareJID());
	}

	@SuppressWarnings({ "unchecked" })
	protected Map<BareJID, RosterElement> getUserRoster(XMPPResourceConnection session)
			throws NotAuthorizedException, TigaseDBException {
		Map<BareJID, RosterElement> roster = null;

		// The method can be called from different plugins concurrently.
		// If the roster is not yet loaded from DB this causes concurent
		// access problems
		synchronized (session) {
			roster = (Map<BareJID, RosterElement>) session.getCommonSessionData(ROSTER);

			if (roster == null) {
				roster = loadUserRoster(session);
			}
		}

		return roster;
	}

	// ~--- methods --------------------------------------------------------------

	protected void saveUserRoster(XMPPResourceConnection session)
			throws NotAuthorizedException, TigaseDBException {
		Map<BareJID, RosterElement> roster = getUserRoster(session);
		StringBuilder sb = new StringBuilder(5000);

		for (RosterElement relem : roster.values()) {
			if (relem.isPersistent()) {
				sb.append(relem.getRosterElement().toString());
			}
		}

		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "Saving user roster: {0}", sb);
		}

		session.setData(null, ROSTER, sb.toString());
	}

	private Map<BareJID, RosterElement> loadUserRoster(XMPPResourceConnection session)
			throws NotAuthorizedException, TigaseDBException {

		// In most times we just read from this data structure
		// From time to time there might be some modification, posibly concurrent
		// very unlikely by more than one thread
		Map<BareJID, RosterElement> roster =
				new ConcurrentHashMap<BareJID, RosterElement>(100, 0.25f, 1);

		session.putCommonSessionData(ROSTER, roster);

		String roster_str = session.getData(null, ROSTER, null);

		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "Loaded user roster: {0}", roster_str);
		}

		if ((roster_str != null) && !roster_str.isEmpty()) {
			updateRosterHash(roster_str, session);

			boolean modified = parseRoster(roster_str, roster, session);

			if (modified) {
				saveUserRoster(session);
			}
		} else {

			// Try to load a roster from the 'old' style roster storage and
			// convert it the the flat roster storage
			Roster oldRoster = new Roster();
			JID[] buddies = oldRoster.getBuddies(session);

			if ((buddies != null) && (buddies.length > 0)) {
				for (JID buddy : buddies) {
					String name = oldRoster.getBuddyName(session, buddy);
					SubscriptionType subscr = oldRoster.getBuddySubscription(session, buddy);
					String[] groups = oldRoster.getBuddyGroups(session, buddy);
					RosterElement relem = getRosterElementInstance(buddy, name, groups, session);

					relem.setSubscription(subscr);

					if (!addBuddy(relem, roster)) {
						break;
					}
				}

				saveUserRoster(session);
			}
		}

		return roster;
	}

	// @Override
	// public void setBuddyOnline(XMPPResourceConnection session, String buddy,
	// boolean online)
	// throws NotAuthorizedException, TigaseDBException {
	// RosterElement relem = getRosterElement(session, buddy);
	// if (relem != null) {
	// relem.setOnline(online);
	// }
	// }
	//
	// @Override
	// public boolean isBuddyOnline(XMPPResourceConnection session, String buddy)
	// throws NotAuthorizedException, TigaseDBException {
	// RosterElement relem = getRosterElement(session, buddy);
	// if (relem != null) {
	// return relem.isOnline();
	// }
	// return false;
	// }

	private class RosterElemComparator implements Comparator<JID> {

		private Map<BareJID, RosterElement> roster = null;

		private RosterElemComparator(Map<BareJID, RosterElement> roster) {
			this.roster = roster;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(JID arg0, JID arg1) {
			double w0 = roster.get(arg0.getBareJID()).getWeight();
			double w1 = roster.get(arg1.getBareJID()).getWeight();
			return Double.compare(w0, w1);
		}

	}

	public String getCustomStatus(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		RosterElement rel = getRosterElement(session, buddy);
		String result = null;
		if (rel != null) {
			if (rel.getLastSeen() > RosterElement.INITIAL_LAST_SEEN_VAL) {
				result =
						"Buddy last seen on: " + new Date(rel.getLastSeen()) + ", weight: "
								+ rel.getWeight();
			} else {
				result = "Never seen";
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tigase.xmpp.impl.roster.RosterAbstract#logout()
	 */
	@Override
	public void logout(XMPPResourceConnection session) {
		try {
			if (session.isAuthorized() && isModified(session)) {
				saveUserRoster(session);
			}
		} catch (NotAuthorizedException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		} catch (TigaseDBException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	/**
	 * @param session
	 * @return
	 * @throws TigaseDBException
	 * @throws NotAuthorizedException
	 */
	public boolean isModified(XMPPResourceConnection session)
			throws NotAuthorizedException, TigaseDBException {
		Map<BareJID, RosterElement> roster = getUserRoster(session);
		boolean result = false;
		if (roster != null) {
			for (RosterElement rel : roster.values()) {
				result |= rel.isModified();
			}
		}
		return result;
	}

} // RosterFlat

// ~ Formatted in Sun Code Convention

// ~ Formatted by Jindent --- http://www.jindent.com
