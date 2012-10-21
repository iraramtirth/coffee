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

package tigase.xmpp.impl;

import tigase.db.MsgRepositoryIfc;
import tigase.db.NonAuthUserRepository;
import tigase.db.TigaseDBException;
import tigase.db.UserNotFoundException;

import tigase.server.Packet;

import tigase.util.DNSResolver;
import tigase.util.TigaseStringprepException;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;
import tigase.xml.SingletonFactory;

import tigase.xmpp.JID;
import tigase.xmpp.NotAuthorizedException;
import tigase.xmpp.StanzaType;
import tigase.xmpp.XMPPPostprocessorIfc;
import tigase.xmpp.XMPPProcessor;
import tigase.xmpp.XMPPProcessorIfc;
import tigase.xmpp.XMPPResourceConnection;

import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Describe class OfflineMessages here.
 * 
 * 
 * Created: Mon Oct 16 13:28:53 2006
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class OfflineMessages extends XMPPProcessor implements XMPPPostprocessorIfc,
		XMPPProcessorIfc {

	/**
	 * Private logger for class instances.
	 */
	private static final Logger log = Logger.getLogger(OfflineMessages.class.getName());
	private static final String ID = "msgoffline";
	protected static final String XMLNS = "jabber:client";
	private static final String[] ELEMENTS = { Presence.PRESENCE_ELEMENT_NAME };
	private static final String[] XMLNSS = { XMLNS };
	private static final Element[] DISCO_FEATURES = { new Element("feature",
			new String[] { "var" }, new String[] { "msgoffline" }) };
	private static final String defHost = DNSResolver.getDefaultHostname();

	private final SimpleDateFormat formatter =
			new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int concurrentQueuesNo() {
		return Runtime.getRuntime().availableProcessors();
	}

	/**
	 * Describe <code>id</code> method here.
	 * 
	 * @return a <code>String</code> value
	 */
	@Override
	public String id() {
		return ID;
	}

	// Implementation of tigase.xmpp.XMPPPostprocessorIfc

	/**
	 * Describe <code>postProcess</code> method here.
	 * 
	 * @param packet
	 *          a <code>Packet</code> value
	 * @param conn
	 *          a <code>XMPPResourceConnection</code> value
	 * @param repo
	 *          a <code>NonAuthUserRepository</code> value
	 * @param queue
	 *          a <code>Queue</code> value
	 * @param settings
	 */
	@Override
	public void postProcess(final Packet packet, final XMPPResourceConnection conn,
			final NonAuthUserRepository repo, final Queue<Packet> queue,
			Map<String, Object> settings) {
		if (conn == null) {
			try {
				MsgRepositoryIfc msg_repo = getMsgRepoImpl(repo, conn);

				savePacketForOffLineUser(packet, msg_repo);
			} catch (UserNotFoundException e) {
				if (log.isLoggable(Level.FINEST)) {
					log.finest("UserNotFoundException at trying to save packet for off-line user."
							+ packet);
				}
			} // end of try-catch
		} // end of if (conn == null)
	}

	/**
	 * Describe <code>process</code> method here.
	 * 
	 * @param packet
	 *          a <code>Packet</code> value
	 * @param conn
	 *          a <code>XMPPResourceConnection</code> value
	 * @param repo
	 *          a <code>NonAuthUserRepository</code> value
	 * @param results
	 *          a <code>Queue</code> value
	 * @param settings
	 * @throws NotAuthorizedException
	 */
	@Override
	public void process(final Packet packet, final XMPPResourceConnection conn,
			final NonAuthUserRepository repo, final Queue<Packet> results,
			final Map<String, Object> settings) throws NotAuthorizedException {
		if (loadOfflineMessages(packet, conn)) {
			try {
				MsgRepositoryIfc msg_repo = getMsgRepoImpl(repo, conn);
				Queue<Packet> packets = restorePacketForOffLineUser(conn, msg_repo);

				if (packets != null) {
					if (log.isLoggable(Level.FINER)) {
						log.finer("Sending off-line messages: " + packets.size());
					}

					results.addAll(packets);
				} // end of if (packets != null)
			} catch (UserNotFoundException e) {
				log.info("Something wrong, DB problem, cannot load offline messages. " + e);
			} // end of try-catch
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param conn
	 * @param repo
	 * @return
	 * 
	 * @throws UserNotFoundException
	 * @throws NotAuthorizedException
	 */
	public Queue<Packet> restorePacketForOffLineUser(XMPPResourceConnection conn,
			MsgRepositoryIfc repo) throws UserNotFoundException, NotAuthorizedException {
		Queue<Element> elems = repo.loadMessagesToJID(conn.getJID(), true);

		if (elems != null) {
			LinkedList<Packet> pacs = new LinkedList<Packet>();
			Element elem = null;

			while ((elem = elems.poll()) != null) {
				try {
					pacs.offer(Packet.packetInstance(elem));
				} catch (TigaseStringprepException ex) {
					log.warning("Packet addressing problem, stringprep failed: " + elem);
				}
			} // end of while (elem = elems.poll() != null)

			try {
				Collections.sort(pacs, new StampComparator());
			} catch (NullPointerException e) {
				try {
					log.warning("Can not sort off line messages: " + pacs + ",\n" + e);
				} catch (Exception exc) {
					log.log(Level.WARNING, "Can not print log message.", exc);
				}
			}

			return pacs;
		}

		return null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param pac
	 * @param repo
	 * 
	 * @return
	 * 
	 * @throws UserNotFoundException
	 */
	public boolean savePacketForOffLineUser(Packet pac, MsgRepositoryIfc repo)
			throws UserNotFoundException {
		StanzaType type = pac.getType();

		if ((pac.getElemName().equals("message")
				&& (pac.getElemCData("/message/body") != null)
					&& ((type == null) || (type == StanzaType.normal)
						|| (type == StanzaType.chat))) || (pac.getElemName().equals("presence")
							&& ((type == StanzaType.subscribe) || (type == StanzaType.subscribed)
								|| (type == StanzaType.unsubscribe) || (type == StanzaType.unsubscribed)))) {
			
			if (log.isLoggable(Level.FINEST)) {
				log.log(Level.FINEST, "Storing packet for offline user: {0}", pac);
			}
			
			Element elem = pac.getElement().clone();
			String stamp = null;

			synchronized (formatter) {
				stamp = formatter.format(new Date());
			}

			String from = pac.getStanzaTo().getDomain();
			Element x = new Element("delay", "Offline Storage - " + defHost,
				new String[] { "from", "stamp", "xmlns" }, new String[] { from,
					stamp, "urn:xmpp:delay" });

			elem.addChild(x);
			repo.storeMessage(pac.getStanzaFrom(), pac.getStanzaTo(), null, elem);
			pac.processedBy(ID);

			return true;
		} else {
			if (log.isLoggable(Level.FINEST)) {
				log.log(Level.FINEST, "Packet for offline user not suitable for storing: {0}", pac);
			}
		}

		return false;
	}

	/**
	 * Describe <code>supDiscoFeatures</code> method here.
	 * 
	 * @param session
	 *          a <code>XMPPResourceConnection</code> value
	 * @return a <code>String[]</code> value
	 */
	@Override
	public Element[] supDiscoFeatures(final XMPPResourceConnection session) {
		return DISCO_FEATURES;
	}

	// Implementation of tigase.xmpp.XMPPImplIfc

	/**
	 * Describe <code>supElements</code> method here.
	 * 
	 * @return a <code>String[]</code> value
	 */
	@Override
	public String[] supElements() {
		return ELEMENTS;
	}

	/**
	 * Describe <code>supNamespaces</code> method here.
	 * 
	 * @return a <code>String[]</code> value
	 */
	@Override
	public String[] supNamespaces() {
		return XMLNSS;
	}

	// ~--- get methods ----------------------------------------------------------

	protected MsgRepositoryIfc getMsgRepoImpl(NonAuthUserRepository repo,
			XMPPResourceConnection conn) {
		return new MsgRepositoryImpl(repo, conn);
	}

	// ~--- methods --------------------------------------------------------------

	// Implementation of tigase.xmpp.XMPPProcessorIfc
	protected boolean loadOfflineMessages(Packet packet, XMPPResourceConnection conn) {

		// If the user session is null or the user is anonymous just
		// ignore it.
		if ((conn == null) || conn.isAnonymous()) {
			return false;
		} // end of if (session == null)

		// Try to restore the offline messages only once for the user session
		if (conn.getSessionData(ID) != null) {
			return false;
		}

		StanzaType type = packet.getType();

		if ((type == null) || (type == StanzaType.available)) {

			// Should we send off-line messages now?
			// Let's try to do it here and maybe later I find better place.
			String priority_str = packet.getElemCData("/presence/priority");
			int priority = 0;

			if (priority_str != null) {
				try {
					priority = Integer.decode(priority_str);
				} catch (NumberFormatException e) {
					priority = 0;
				} // end of try-catch
			} // end of if (priority != null)

			if (priority >= 0) {
				conn.putSessionData(ID, ID);

				return true;
			} // end of if (priority >= 0)
		} // end of if (type == null || type == StanzaType.available)

		return false;
	}

	// ~--- inner classes --------------------------------------------------------

	private class MsgRepositoryImpl implements MsgRepositoryIfc {
		private XMPPResourceConnection conn = null;
		private SimpleParser parser = SingletonFactory.getParserInstance();
		private NonAuthUserRepository repo = null;

		// ~--- constructors -------------------------------------------------------

		private MsgRepositoryImpl(NonAuthUserRepository repo, XMPPResourceConnection conn) {
			this.repo = repo;
			this.conn = conn;
		}

		// ~--- get methods --------------------------------------------------------

		/**
		 * Method description
		 * 
		 * 
		 * @param time
		 * @param delete
		 * 
		 * @return
		 */
		@Override
		public Element getMessageExpired(long time, boolean delete) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		// ~--- methods ------------------------------------------------------------

		/**
		 * Method description
		 * 
		 * 
		 * @param to
		 * @param delete
		 * 
		 * @return
		 * 
		 * @throws UserNotFoundException
		 */
		@Override
		public Queue<Element> loadMessagesToJID(JID to, boolean delete)
				throws UserNotFoundException {
			try {
				DomBuilderHandler domHandler = new DomBuilderHandler();
				String[] msgs = conn.getOfflineDataList(ID, "messages");

				if ((msgs != null) && (msgs.length > 0)) {
					conn.removeOfflineData(ID, "messages");

					LinkedList<Packet> pacs = new LinkedList<Packet>();
					StringBuilder sb = new StringBuilder();

					for (String msg : msgs) {
						sb.append(msg);
					}

					char[] data = sb.toString().toCharArray();

					parser.parse(domHandler, data, 0, data.length);

					return domHandler.getParsedElements();
				} // end of while (elem = elems.poll() != null)
			} catch (NotAuthorizedException ex) {
				log.info("User not authrized to retrieve offline messages, "
						+ "this happens quite often on some installations where there"
						+ " are a very short living client connections. They can "
						+ "disconnect at any time. " + ex);
			} catch (TigaseDBException ex) {
				log.warning("Error accessing database for offline message: " + ex);
			}

			return null;
		}

		/**
		 * Method description
		 * 
		 * 
		 * @param from
		 * @param to
		 * @param expired
		 * @param msg
		 * 
		 * @throws UserNotFoundException
		 */
		@Override
		public void storeMessage(JID from, JID to, Date expired, Element msg)
				throws UserNotFoundException {
			repo.addOfflineDataList(to.getBareJID(), ID, "messages",
					new String[] { msg.toString() });
		}
	}

	private class StampComparator implements Comparator<Packet> {

		/**
		 * Method description
		 * 
		 * 
		 * @param p1
		 * @param p2
		 * 
		 * @return
		 */
		@Override
		public int compare(Packet p1, Packet p2) {
			String stamp1 = "";
			String stamp2 = "";

			// Try XEP-0203 - the new XEP...
			Element stamp_el1 = p1.getElement().getChild("delay", "urn:xmpp:delay");

			if (stamp_el1 == null) {

				// XEP-0091 support - the old one...
				stamp_el1 = p1.getElement().getChild("x", "jabber:x:delay");
			}

			stamp1 = stamp_el1.getAttribute("stamp");

			// Try XEP-0203 - the new XEP...
			Element stamp_el2 = p2.getElement().getChild("delay", "urn:xmpp:delay");

			if (stamp_el2 == null) {

				// XEP-0091 support - the old one...
				stamp_el2 = p2.getElement().getChild("x", "jabber:x:delay");
			}

			stamp2 = stamp_el2.getAttribute("stamp");

			return stamp1.compareTo(stamp2);
		}
	}
} // OfflineMessages

// ~ Formatted in Sun Code Convention

// ~ Formatted by Jindent --- http://www.jindent.com
