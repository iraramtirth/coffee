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

import tigase.db.TigaseDBException;
import tigase.db.UserRepository;

import tigase.server.Packet;

import tigase.util.Algorithms;

import tigase.xml.Element;
import tigase.xml.XMLUtils;

import tigase.xmpp.BareJID;
import tigase.xmpp.JID;
import tigase.xmpp.NoConnectionIdException;
import tigase.xmpp.NotAuthorizedException;
import tigase.xmpp.StanzaType;
import tigase.xmpp.XMPPResourceConnection;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 * Describe class RosterAbstract here.
 * 
 * 
 * Created: Thu Sep 4 18:09:52 2008
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public abstract class RosterAbstract {

	// Below StateTransition enum is implementation of all below tables
	// coming from RFC-3921
	// Table 1: Recommended handling of outbound "subscribed" stanzas
	// +----------------------------------------------------------------+
	// | EXISTING STATE | ROUTE? | NEW STATE |
	// +----------------------------------------------------------------+
	// | "None" | no | no state change |
	// | "None + Pending Out" | no | no state change |
	// | "None + Pending In" | yes | "From" |
	// | "None + Pending Out/In" | yes | "From + Pending Out" |
	// | "To" | no | no state change |
	// | "To + Pending In" | yes | "Both" |
	// | "From" | no | no state change |
	// | "From + Pending Out" | no | no state change |
	// | "Both" | no | no state change |
	// +----------------------------------------------------------------+
	// Table 2: Recommended handling of outbound "unsubscribed" stanzas
	// +----------------------------------------------------------------+
	// | EXISTING STATE | ROUTE? | NEW STATE |
	// +----------------------------------------------------------------+
	// | "None" | no | no state change |
	// | "None + Pending Out" | no | no state change |
	// | "None + Pending In" | yes | "None" |
	// | "None + Pending Out/In" | yes | "None + Pending Out" |
	// | "To" | no | no state change |
	// | "To + Pending In" | yes | "To" |
	// | "From" | yes | "None" |
	// | "From + Pending Out" | yes | "None + Pending Out" |
	// | "Both" | yes | "To" |
	// +----------------------------------------------------------------+
	// Table 3: Recommended handling of inbound "subscribe" stanzas
	// +------------------------------------------------------------------+
	// | EXISTING STATE | DELIVER? | NEW STATE |
	// +------------------------------------------------------------------+
	// | "None" | yes | "None + Pending In" |
	// | "None + Pending Out" | yes | "None + Pending Out/In" |
	// | "None + Pending In" | no | no state change |
	// | "None + Pending Out/In" | no | no state change |
	// | "To" | yes | "To + Pending In" |
	// | "To + Pending In" | no | no state change |
	// | "From" | no * | no state change |
	// | "From + Pending Out" | no * | no state change |
	// | "Both" | no * | no state change |
	// +------------------------------------------------------------------+
	// Table 4: Recommended handling of inbound "unsubscribe" stanzas
	// +------------------------------------------------------------------+
	// | EXISTING STATE | DELIVER? | NEW STATE |
	// +------------------------------------------------------------------+
	// | "None" | no | no state change |
	// | "None + Pending Out" | no | no state change |
	// | "None + Pending In" | yes * | "None" |
	// | "None + Pending Out/In" | yes * | "None + Pending Out" |
	// | "To" | no | no state change |
	// | "To + Pending In" | yes * | "To" |
	// | "From" | yes * | "None" |
	// | "From + Pending Out" | yes * | "None + Pending Out" |
	// | "Both" | yes * | "To" |
	// +------------------------------------------------------------------+
	// Table 5: Recommended handling of inbound "subscribed" stanzas
	// +------------------------------------------------------------------+
	// | EXISTING STATE | DELIVER? | NEW STATE |
	// +------------------------------------------------------------------+
	// | "None" | no | no state change |
	// | "None + Pending Out" | yes | "To" |
	// | "None + Pending In" | no | no state change |
	// | "None + Pending Out/In" | yes | "To + Pending In" |
	// | "To" | no | no state change |
	// | "To + Pending In" | no | no state change |
	// | "From" | no | no state change |
	// | "From + Pending Out" | yes | "Both" |
	// | "Both" | no | no state change |
	// +------------------------------------------------------------------+
	// Table 6: Recommended handling of inbound "unsubscribed" stanzas
	// +------------------------------------------------------------------+
	// | EXISTING STATE | DELIVER? | NEW STATE |
	// +------------------------------------------------------------------+
	// | "None" | no | no state change |
	// | "None + Pending Out" | yes | "None" |
	// | "None + Pending In" | no | no state change |
	// | "None + Pending Out/In" | yes | "None + Pending In" |
	// | "To" | yes | "None" |
	// | "To + Pending In" | yes | "None + Pending In" |
	// | "From" | no | no state change |
	// | "From + Pending Out" | yes | "From" |
	// | "Both" | yes | "From" |
	// +------------------------------------------------------------------+
	// There are 2 tables missing I think in RFC-3921:
	// Table 7: Recommended handling of outbound "subscribe" stanzas
	// +------------------------------------------------------------------+
	// | EXISTING STATE | ROUTE? | NEW STATE |
	// +------------------------------------------------------------------+
	// | "None" | yes | "None + Pending Out" |
	// | "None + Pending Out" | no | no state change |
	// | "None + Pending In" | yes | "None + Pending Out/In" |
	// | "None + Pending Out/In" | no | no state change |
	// | "To" | no | no state change |
	// | "To + Pending In" | no | no state change |
	// | "From" | yes | "From + Pending Out" |
	// | "From + Pending Out" | no | no state change |
	// | "Both" | no | no state change |
	// +------------------------------------------------------------------+
	// Table 8: Recommended handling of outbound "unsubscribe" stanzas
	// +------------------------------------------------------------------+
	// | EXISTING STATE | ROUTE? | NEW STATE |
	// +------------------------------------------------------------------+
	// | "None" | no | no state change |
	// | "None + Pending Out" | yes | "None" |
	// | "None + Pending In" | no | no state change |
	// | "None + Pending Out/In" | yes | "None + Pending In" |
	// | "To" | yes | "None" |
	// | "To + Pending In" | yes | "None + Pending In" |
	// | "From" | no | no state change |
	// | "From + Pending Out" | yes | "From" |
	// | "Both" | yes | "From" |
	// +------------------------------------------------------------------+

	/**
	 * Enum description
	 * 
	 */
	public enum StateTransition {
		none(SubscriptionType.none, // Table 1.
				SubscriptionType.none, // Table 2.
				SubscriptionType.none_pending_in, // Table 3.
				SubscriptionType.none, // Table 4.
				SubscriptionType.none, // Table 5.
				SubscriptionType.none, // Table 6.
				SubscriptionType.none_pending_out, // Table 7.
				SubscriptionType.none // Table 8.
		), none_pending_out(SubscriptionType.none_pending_out, // Table 1.
				SubscriptionType.none_pending_out, // Table 2.
				SubscriptionType.none_pending_out_in, // Table 3.
				SubscriptionType.none_pending_out, // Table 4.
				SubscriptionType.to, // Table 5.
				SubscriptionType.none, // Table 6.
				SubscriptionType.none_pending_out, // Table 7.
				SubscriptionType.none // Table 8.
		), none_pending_in(SubscriptionType.from, // Table 1.
				SubscriptionType.none, // Table 2.
				SubscriptionType.none_pending_in, // Table 3.
				SubscriptionType.none, // Table 4.
				SubscriptionType.none_pending_in, // Table 5.
				SubscriptionType.none_pending_in, // Table 6.
				SubscriptionType.none_pending_out_in, // Table 7.
				SubscriptionType.none_pending_in // Table 8.
		), none_pending_out_in(SubscriptionType.from_pending_out, // Table 1.
				SubscriptionType.none_pending_out, // Table 2.
				SubscriptionType.none_pending_out_in, // Table 3.
				SubscriptionType.none_pending_out, // Table 4.
				SubscriptionType.to_pending_in, // Table 5.
				SubscriptionType.none_pending_in, // Table 6.
				SubscriptionType.none_pending_out_in, // Table 7.
				SubscriptionType.none_pending_in // Table 8.
		), to(SubscriptionType.to, // Table 1.
				SubscriptionType.to, // Table 2.
				SubscriptionType.to_pending_in, // Table 3.
				SubscriptionType.to, // Table 4.
				SubscriptionType.to, // Table 5.
				SubscriptionType.none, // Table 6.
				SubscriptionType.to, // Table 7.
				SubscriptionType.none // Table 8.
		), to_pending_in(SubscriptionType.both, // Table 1.
				SubscriptionType.to, // Table 2.
				SubscriptionType.to_pending_in, // Table 3.
				SubscriptionType.to, // Table 4.
				SubscriptionType.to_pending_in, // Table 5.
				SubscriptionType.none_pending_in, // Table 6.
				SubscriptionType.to_pending_in, // Table 7.
				SubscriptionType.none_pending_in // Table 8.
		), from(SubscriptionType.from, // Table 1.
				SubscriptionType.none, // Table 2.
				SubscriptionType.from, // Table 3.
				SubscriptionType.none, // Table 4.
				SubscriptionType.from, // Table 5.
				SubscriptionType.from, // Table 6.
				SubscriptionType.from_pending_out, // Table 7.
				SubscriptionType.from // Table 8.
		), from_pending_out(SubscriptionType.from_pending_out, // Table 1.
				SubscriptionType.none_pending_out, // Table 2.
				SubscriptionType.from_pending_out, // Table 3.
				SubscriptionType.none_pending_out, // Table 4.
				SubscriptionType.both, // Table 5.
				SubscriptionType.from, // Table 6.
				SubscriptionType.from_pending_out, // Table 7.
				SubscriptionType.from // Table 8.
		), both(SubscriptionType.both, // Table 1.
				SubscriptionType.to, // Table 2.
				SubscriptionType.both, // Table 3.
				SubscriptionType.to, // Table 4.
				SubscriptionType.both, // Table 5.
				SubscriptionType.from, // Table 6.
				SubscriptionType.both, // Table 7.
				SubscriptionType.from // Table 8.
		);

		private EnumMap<PresenceType, SubscriptionType> stateTransition =
				new EnumMap<PresenceType, SubscriptionType>(PresenceType.class);

		// ~--- constructors -------------------------------------------------------

		private StateTransition(SubscriptionType out_subscribed,
				SubscriptionType out_unsubscribed, SubscriptionType in_subscribe,
				SubscriptionType in_unsubscribe, SubscriptionType in_subscribed,
				SubscriptionType in_unsubscribed, SubscriptionType out_subscribe,
				SubscriptionType out_unsubscribe) {
			stateTransition.put(PresenceType.out_subscribed, out_subscribed);
			stateTransition.put(PresenceType.out_unsubscribed, out_unsubscribed);
			stateTransition.put(PresenceType.in_subscribe, in_subscribe);
			stateTransition.put(PresenceType.in_unsubscribe, in_unsubscribe);
			stateTransition.put(PresenceType.in_subscribed, in_subscribed);
			stateTransition.put(PresenceType.in_unsubscribed, in_unsubscribed);
			stateTransition.put(PresenceType.out_subscribe, out_subscribe);
			stateTransition.put(PresenceType.out_unsubscribe, out_unsubscribe);
		}

		// ~--- get methods --------------------------------------------------------

		/**
		 * Method description
		 * 
		 * 
		 * @param pres_type
		 * 
		 * @return
		 */
		public SubscriptionType getStateTransition(PresenceType pres_type) {
			SubscriptionType res = stateTransition.get(pres_type);

			if (log.isLoggable(Level.FINEST)) {
				log.finest("this=" + this.toString() + ", pres_type=" + pres_type + ", res="
						+ res);
			}

			return res;
		}
	}

	/**
	 * Enum description
	 * 
	 */
	public enum SubscriptionType {
		none("none", null), none_pending_out("none", "subscribe"), none_pending_in("none",
				null), none_pending_out_in("none", "subscribe"), to("to", null), to_pending_in(
				"to", null), from("from", null), from_pending_out("from", "subscribe"), both(
				"both", null), remove("remove", null);

		private Map<String, String> attrs = new LinkedHashMap<String, String>(2, 1.0f);

		// ~--- constructors -------------------------------------------------------

		private SubscriptionType(String subscr, String ask) {
			attrs.put("subscription", subscr);

			if (ask != null) {
				attrs.put("ask", ask);
			} // end of if (ask != null)
		}

		// ~--- get methods --------------------------------------------------------

		/**
		 * Method description
		 * 
		 * 
		 * @return
		 */
		public Map<String, String> getSubscriptionAttr() {
			return attrs;
		}
	}

	// ~--- static fields --------------------------------------------------------

	/**
	 * Private logger for class instances.
	 */
	private static Logger log = Logger.getLogger(RosterAbstract.class.getName());

	public static final String CLIENT_XMLNS = "jabber:client";

	/** Field description */
	public static final String ROSTER_XMLNS = "jabber:iq:roster";

	/** Field description */
	public static final String ROSTER = "roster";

	/** Field description */
	public static final String GROUPS = "groups";

	/** Field description */
	public static final String GROUP = "group";

	/** Field description */
	public static final String NAME = "name";

	/** Field description */
	public static final String SUBSCRIPTION = "subscription";

	/** Field description */
	public static final String ROSTERHASH = "rosterhash";

	/** Field description */
	public static final String XMLNS = "jabber:iq:roster";

	/** Field description */
	public static final String XMLNS_DYNAMIC = "jabber:iq:roster-dynamic";
	
	public static final String VER_ATT = "ver";

	/** Field description */
	public static final Element[] DISCO_FEATURES = {
			new Element("feature", new String[] { "var" }, new String[] { XMLNS }),
			new Element("feature", new String[] { "var" }, new String[] { XMLNS_DYNAMIC }) };

	/** Field description */
	public static final Element[] FEATURES = { new Element("ver", new String[] { "xmlns" },
			new String[] { "urn:xmpp:features:rosterver" }) };

	/** Field description */
	public static final EnumSet<SubscriptionType> SUB_NONE = EnumSet.of(
			SubscriptionType.none, SubscriptionType.none_pending_out,
			SubscriptionType.none_pending_in, SubscriptionType.none_pending_out_in);

	/** Field description */
	public static final EnumSet<SubscriptionType> SUB_TO = EnumSet.of(SubscriptionType.to,
			SubscriptionType.to_pending_in);

	/** Field description */
	public static final EnumSet<SubscriptionType> SUB_FROM = EnumSet.of(
			SubscriptionType.from, SubscriptionType.from_pending_out);

	/** Field description */
	public static final EnumSet<SubscriptionType> SUB_BOTH = EnumSet
			.of(SubscriptionType.both);

	/** Field description */
	public static final EnumSet<SubscriptionType> TO_SUBSCRIBED = EnumSet.of(
			SubscriptionType.to, SubscriptionType.to_pending_in, SubscriptionType.both);

	/** Field description */
	public static final EnumSet<SubscriptionType> FROM_SUBSCRIBED = EnumSet.of(
			SubscriptionType.from, SubscriptionType.from_pending_out, SubscriptionType.both);

	/** Field description */
	public static final EnumSet<StanzaType> INITIAL_PRESENCES = EnumSet.of(
			StanzaType.available, StanzaType.unavailable);

	/** Field description */
	public static final EnumSet<SubscriptionType> PENDING_IN = EnumSet.of(
			SubscriptionType.none_pending_in, SubscriptionType.none_pending_out_in,
			SubscriptionType.to_pending_in);

	/** Field description */
	public static final EnumSet<SubscriptionType> PENDING_OUT = EnumSet.of(
			SubscriptionType.none_pending_out, SubscriptionType.none_pending_out_in,
			SubscriptionType.from_pending_out);
	private static EnumMap<SubscriptionType, StateTransition> subsToStateMap =
			new EnumMap<SubscriptionType, StateTransition>(SubscriptionType.class);

	// ~--- constant enums -------------------------------------------------------

	/**
	 * Enum description
	 * 
	 */
	public enum PresenceType {
		out_initial, out_subscribe, out_unsubscribe, out_subscribed, out_unsubscribed,
		out_probe, in_initial, in_subscribe, in_unsubscribe, in_subscribed, in_unsubscribed,
		in_probe, error;
	}

	// ~--- static initializers --------------------------------------------------

	static {
		subsToStateMap.put(SubscriptionType.none, StateTransition.none);
		subsToStateMap.put(SubscriptionType.none_pending_out,
				StateTransition.none_pending_out);
		subsToStateMap.put(SubscriptionType.none_pending_in, StateTransition.none_pending_in);
		subsToStateMap.put(SubscriptionType.none_pending_out_in,
				StateTransition.none_pending_out_in);
		subsToStateMap.put(SubscriptionType.to, StateTransition.to);
		subsToStateMap.put(SubscriptionType.to_pending_in, StateTransition.to_pending_in);
		subsToStateMap.put(SubscriptionType.from, StateTransition.from);
		subsToStateMap.put(SubscriptionType.from_pending_out,
				StateTransition.from_pending_out);
		subsToStateMap.put(SubscriptionType.both, StateTransition.both);
	}

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param jid
	 * @param name
	 * @param groups
	 * @param otherData
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	public abstract void addBuddy(XMPPResourceConnection session, JID jid, String name,
			String[] groups, String otherData) throws NotAuthorizedException, TigaseDBException;

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
	public abstract boolean addBuddyGroup(final XMPPResourceConnection session, JID buddy,
			final String[] groups) throws NotAuthorizedException, TigaseDBException;

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
	public abstract boolean containsBuddy(final XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException;

	// ~--- get methods ----------------------------------------------------------

	// public abstract String[] getBuddies(final XMPPResourceConnection session,
	// boolean onlineOnly)
	// throws NotAuthorizedException, TigaseDBException;

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
	public abstract JID[] getBuddies(final XMPPResourceConnection session)
			throws NotAuthorizedException, TigaseDBException;

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
	public abstract String[]
			getBuddyGroups(final XMPPResourceConnection session, JID buddy)
					throws NotAuthorizedException, TigaseDBException;

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
	public abstract String getBuddyName(final XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException;

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
	public abstract SubscriptionType getBuddySubscription(
			final XMPPResourceConnection session, JID buddy) throws NotAuthorizedException,
			TigaseDBException;

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
	public abstract boolean isOnline(XMPPResourceConnection session, JID jid)
			throws NotAuthorizedException, TigaseDBException;

	// ~--- methods --------------------------------------------------------------

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
	public abstract boolean presenceSent(XMPPResourceConnection session, JID jid)
			throws NotAuthorizedException, TigaseDBException;

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
	public abstract boolean removeBuddy(final XMPPResourceConnection session, JID jid)
			throws NotAuthorizedException, TigaseDBException;

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
	public abstract void setBuddyName(final XMPPResourceConnection session, JID buddy,
			final String name) throws NotAuthorizedException, TigaseDBException;

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
	public abstract void setBuddySubscription(final XMPPResourceConnection session,
			final SubscriptionType subscription, JID buddy) throws NotAuthorizedException,
			TigaseDBException;

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param jid
	 * @param online
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	public abstract void setOnline(XMPPResourceConnection session, JID jid, boolean online)
			throws NotAuthorizedException, TigaseDBException;

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param jid
	 * @param sent
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	public abstract void setPresenceSent(XMPPResourceConnection session, JID jid,
			boolean sent) throws NotAuthorizedException, TigaseDBException;

	public abstract RosterElementIfc getRosterElement(XMPPResourceConnection session,
			JID jid) throws NotAuthorizedException, TigaseDBException;

	// ~--- get methods ----------------------------------------------------------

	// public String[] getBuddies(final XMPPResourceConnection session,
	// final EnumSet<SubscriptionType> subscrs, boolean onlineOnly)

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param subscrs
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	public JID[] getBuddies(final XMPPResourceConnection session,
			final EnumSet<SubscriptionType> subscrs) throws NotAuthorizedException,
			TigaseDBException {

		// final String[] allBuddies = getBuddies(session, onlineOnly);
		JID[] allBuddies = getBuddies(session);

		if (allBuddies == null) {
			return null;
		} // end of if (allBuddies == null)

		ArrayList<JID> list = new ArrayList<JID>();

		for (JID buddy : allBuddies) {
			final SubscriptionType subs = getBuddySubscription(session, buddy);

			if (subscrs.contains(subs)) {
				list.add(buddy);
			} // end of if (subscrs.contains(subs))
		} // end of for ()

		return list.toArray(new JID[list.size()]);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * 
	 * @return
	 */
	public String getBuddiesHash(final XMPPResourceConnection session) {
		String hash = (String) session.getCommonSessionData(ROSTERHASH); 
		return (hash != null ? hash : "");
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
	public Element getBuddyItem(final XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException {
		SubscriptionType subscr = getBuddySubscription(session, buddy);

		if (subscr == null) {
			subscr = SubscriptionType.none;
			setBuddySubscription(session, subscr, buddy);
		} // end of if

		Element item = new Element("item");

		item.setAttribute("jid", buddy.toString());
		item.addAttributes(subscr.getSubscriptionAttr());

		String name = getBuddyName(session, buddy);

		if (name != null) {
			item.setAttribute("name", XMLUtils.escape(name));
		}

		String[] groups = getBuddyGroups(session, buddy);

		if (groups != null) {
			for (String gr : groups) {
				Element group = new Element("group");

				group.setCData(XMLUtils.escape(gr));
				item.addChild(group);
			} // end of for ()
		} // end of if-else

		return item;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param packet
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 */
	public PresenceType getPresenceType(final XMPPResourceConnection session,
			final Packet packet) throws NotAuthorizedException {
		BareJID to =
				(packet.getStanzaTo() != null) ? packet.getStanzaTo().getBareJID() : null;
		StanzaType type = packet.getType();

		if (type == null) {
			type = StanzaType.available;
		} else {
			if (type == StanzaType.error) {
				return PresenceType.error;
			}
		}

		if ((to == null) || !session.isUserId(to)) {
			if (INITIAL_PRESENCES.contains(type)) {
				return PresenceType.out_initial;
			}

			if (type == StanzaType.subscribe) {
				return PresenceType.out_subscribe;
			} // end of if (type == StanzaType.subscribe)

			if (type == StanzaType.unsubscribe) {
				return PresenceType.out_unsubscribe;
			} // end of if (type == StanzaType.unsubscribe)

			if (type == StanzaType.subscribed) {
				return PresenceType.out_subscribed;
			} // end of if (type == StanzaType.subscribed)

			if (type == StanzaType.unsubscribed) {
				return PresenceType.out_unsubscribed;
			} // end of if (type == StanzaType.unsubscribed)

			// StanzaType.probe is invalid here....
			// if (type == StanzaType.probe) {
			// return PresenceType.out_probe;
			// } // if (type == StanzaType.probe)
		} // end of if (to == null || to.equals(session.getUserId()))

		if ((to != null) && session.isUserId(to)) {
			if (INITIAL_PRESENCES.contains(type)) {
				return PresenceType.in_initial;
			}

			if (type == StanzaType.subscribe) {
				return PresenceType.in_subscribe;
			} // end of if (type == StanzaType.subscribe)

			if (type == StanzaType.unsubscribe) {
				return PresenceType.in_unsubscribe;
			} // end of if (type == StanzaType.unsubscribe)

			if (type == StanzaType.subscribed) {
				return PresenceType.in_subscribed;
			} // end of if (type == StanzaType.subscribed)

			if (type == StanzaType.unsubscribed) {
				return PresenceType.in_unsubscribed;
			} // end of if (type == StanzaType.unsubscribed)

			if (type == StanzaType.probe) {
				return PresenceType.in_probe;
			} // end of if (type == StanzaType.probe)
		} // end of if (to != null && !to.equals(session.getUserId()))

		return null;
	}

	// public List<Element> getRosterItems(XMPPResourceConnection session, boolean
	// online)

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
	public List<Element> getRosterItems(XMPPResourceConnection session)
			throws NotAuthorizedException, TigaseDBException {
		LinkedList<Element> items = new LinkedList<Element>();

		// String[] buddies = getBuddies(session, online);
		JID[] buddies = getBuddies(session);

		if (buddies != null) {
			for (JID buddy : buddies) {
				Element buddy_item = getBuddyItem(session, buddy);

				// String item_group = buddy_item.getCData("/item/group");
				items.add(buddy_item);
			}
		}

		return items;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param subscription
	 * @param presence
	 * 
	 * @return
	 */
	public SubscriptionType getStateTransition(final SubscriptionType subscription,
			final PresenceType presence) {
		return subsToStateMap.get(subscription).getStateTransition(presence);
	}

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param buddy
	 * 
	 * @return
	 */
	public String groupNode(JID buddy) {
		return ROSTER + "/" + buddy.getBareJID();
	}

	// public abstract void setBuddyOnline(final XMPPResourceConnection session,
	// final String buddy, final boolean online)
	// throws NotAuthorizedException, TigaseDBException;
	//
	// public abstract boolean isBuddyOnline(final XMPPResourceConnection session,
	// final String buddy)
	// throws NotAuthorizedException, TigaseDBException;
	// public abstract void setBuddyGroups(XMPPResourceConnection session,
	// String buddy, String[] groups)
	// throws NotAuthorizedException, TigaseDBException;

	/**
	 * Method description
	 * 
	 * 
	 * @param repo
	 * 
	 * @throws TigaseDBException
	 * @throws TigaseDBException
	 */
	public void init(UserRepository repo) throws TigaseDBException, TigaseDBException {
	}

	// ~--- get methods ----------------------------------------------------------

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
	public boolean isPendingIn(final XMPPResourceConnection session, JID jid)
			throws NotAuthorizedException, TigaseDBException {
		SubscriptionType subscr = getBuddySubscription(session, jid);

		return PENDING_IN.contains(subscr);
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
	public boolean isSubscribedFrom(final XMPPResourceConnection session, JID jid)
			throws NotAuthorizedException, TigaseDBException {
		SubscriptionType subscr = getBuddySubscription(session, jid);

		return FROM_SUBSCRIBED.contains(subscr);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param subscr
	 * 
	 * @return
	 */
	public boolean isSubscribedFrom(SubscriptionType subscr) {
		return FROM_SUBSCRIBED.contains(subscr);
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
	public boolean isSubscribedTo(final XMPPResourceConnection session, JID jid)
			throws NotAuthorizedException, TigaseDBException {
		SubscriptionType subscr = getBuddySubscription(session, jid);

		return TO_SUBSCRIBED.contains(subscr);
	}

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param results
	 * @param item
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 * @throws NoConnectionIdException
	 */
	public void updateBuddyChange(final XMPPResourceConnection session,
			final Queue<Packet> results, final Element item) throws NotAuthorizedException,
			TigaseDBException, NoConnectionIdException {
		Element update = new Element("iq");

		update.setXMLNS(CLIENT_XMLNS);

		update.setAttribute("type", StanzaType.set.toString());

		Element query = new Element("query");

		query.setXMLNS(ROSTER_XMLNS);
		query.addAttribute(VER_ATT, getBuddiesHash(session));
		query.addChild(item);
		update.addChild(query);

		for (XMPPResourceConnection conn : session.getActiveSessions()) {
			Element conn_update = update.clone();

			conn_update.setAttribute("to", conn.getBareJID().toString());
			conn_update.setAttribute("id", "rst" + session.nextStanzaId());

			Packet pack_update = Packet.packetInstance(conn_update, null, conn.getJID());

			pack_update.setPacketTo(conn.getConnectionId());
			// pack_update.setPacketFrom(session.getJID());
			results.offer(pack_update);
		} // end of for (XMPPResourceConnection conn: sessions)
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param session
	 * @param presence
	 * @param jid
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	public boolean updateBuddySubscription(final XMPPResourceConnection session,
			final PresenceType presence, JID jid) throws NotAuthorizedException,
			TigaseDBException {
		SubscriptionType current_subscription = getBuddySubscription(session, jid);

		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "current_subscription={0} for jid={1}", new Object[] {
					current_subscription, jid });
		}

		if (current_subscription == null) {
			// don't create new roster item for incomming unsubscribe presence #219 /
			// #210
			if (presence != PresenceType.in_unsubscribe
					&& presence != PresenceType.out_unsubscribe) {
				addBuddy(session, jid, null, null, null);
			}
			current_subscription = SubscriptionType.none;
		}

		final SubscriptionType new_subscription =
				getStateTransition(current_subscription, presence);

		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "new_subscription={0} for presence={1}", new Object[] {
					new_subscription, presence });
		}

		if ((current_subscription == SubscriptionType.none_pending_in)
				&& (presence == PresenceType.out_unsubscribed)) {
			removeBuddy(session, jid);

			return false;
		}

		if (current_subscription != new_subscription) {
			setBuddySubscription(session, new_subscription, jid);

			return true;
		} else {
			return false;
		}

		// if ((SUB_NONE.contains(current_subscription)
		// && SUB_NONE.contains(new_subscription))
		// || (SUB_TO.contains(current_subscription)
		// && SUB_TO.contains(new_subscription))
		// || (SUB_FROM.contains(current_subscription)
		// && SUB_FROM.contains(new_subscription))
		// || (SUB_BOTH.contains(current_subscription)
		// && SUB_BOTH.contains(new_subscription))) {
		// return false;
		// } else {
		// setBuddySubscription(session, new_subscription, jid);
		// return true;
		// }
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param roster_str
	 * @param session
	 */
	public void updateRosterHash(String roster_str, XMPPResourceConnection session) {
		String roster_hash = null;

		try {
			roster_hash = Algorithms.hexDigest("", roster_str, "MD5");
		} catch (Exception e) {
			roster_hash = null;
		}

		session.putCommonSessionData(ROSTERHASH, roster_hash);
	}

	public abstract void logout(XMPPResourceConnection session);

	/**
	 * @param session
	 * @param buddy
	 * @return
	 * @throws TigaseDBException
	 * @throws NotAuthorizedException
	 */
	public abstract String getCustomStatus(XMPPResourceConnection session, JID buddy)
			throws NotAuthorizedException, TigaseDBException;

}
