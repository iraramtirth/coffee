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

//~--- non-JDK imports --------------------------------------------------------

import tigase.db.NonAuthUserRepository;

import tigase.server.Packet;

import tigase.xmpp.Authorization;
import tigase.xmpp.BareJID;
import tigase.xmpp.NotAuthorizedException;
import tigase.xmpp.XMPPException;
import tigase.xmpp.XMPPProcessor;
import tigase.xmpp.XMPPProcessorIfc;
import tigase.xmpp.XMPPResourceConnection;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 * Describe class Jingle here.
 *
 *
 * Created: Wed Feb 21 23:05:34 2007
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class Jingle extends XMPPProcessor implements XMPPProcessorIfc {
	private static final Logger log = Logger.getLogger("tigase.xmpp.impl.Jingle");
	private static final String ID = "http://jabber.org/protocol/jingle";
	private static final String[] ELEMENTS = { "jingle", "jingle", "jingle", "session" };
	private static final String[] XMLNSS = { "http://jabber.org/protocol/jingle",
			"http://www.xmpp.org/extensions/xep-0166.html#ns",
			"http://www.xmpp.org/extensions/xep-0167.html#ns", "http://www.google.com/session" };

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public String id() {
		return ID;
	}

	// Implementation of tigase.xmpp.XMPPProcessorIfc

	/**
	 * Describe <code>process</code> method here.
	 *
	 * @param packet a <code>Packet</code> value
	 * @param conn a <code>XMPPResourceConnection</code> value
	 * @param nonAuthUserRepo a <code>NonAuthUserRepository</code> value
	 * @param results a <code>Queue</code> value
	 * @param settings
	 * @throws XMPPException
	 */
	@Override
	public void process(final Packet packet, final XMPPResourceConnection conn,
			final NonAuthUserRepository nonAuthUserRepo, final Queue<Packet> results,
				final Map<String, Object> settings)
			throws XMPPException {
		if (conn == null) {
			return;
		}

		try {
			if (log.isLoggable(Level.FINEST)) {
				log.finest("Received packet: " + packet);
			}

			// Not needed anymore. Packet filter does it for all stanzas.
//    // For all messages coming from the owner of this account set
//    // proper 'from' attribute. This is actually needed for the case
//    // when the user sends a message to himself.
//    if (packet.getFrom().equals(conn.getConnectionId())) {
//      packet.getElement().setAttribute("from", conn.getJID());
//    } // end of if (packet.getFrom().equals(session.getConnectionId()))
			BareJID id = packet.getStanzaTo().getBareJID();

			if (conn.isUserId(id)) {

				// Yes this is message to 'this' client
				// Make sure we send it to right client connection - the connection
				// which supports jingle - this Yate specific code....
				List<XMPPResourceConnection> res = conn.getParentSession().getActiveResources();
				XMPPResourceConnection session = conn;

				if ((res != null) && (res.size() > 1)) {

					// If there are more than 1 connection for this user
					// let's look for a connection with jingle flag set...
					for (XMPPResourceConnection sess : res) {
						if (sess.getSessionData("jingle") != null) {
							session = sess;

							break;
						}
					}
				}

				Packet result = packet.copyElementOnly();

				result.setPacketTo(session.getConnectionId());
				result.setPacketFrom(packet.getTo());
				results.offer(result);
			} else {

				// This is message to some other client
				results.offer(packet.copyElementOnly());
			}    // end of else
		} catch (NotAuthorizedException e) {
			log.warning("NotAuthorizedException for packet: " + packet);
			results.offer(Authorization.NOT_AUTHORIZED.getResponseMessage(packet,
					"You must authorize session first.", true));
		}    // end of try-catch
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public String[] supElements() {
		return ELEMENTS;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public String[] supNamespaces() {
		return XMLNSS;
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
