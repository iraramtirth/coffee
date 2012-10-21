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

import tigase.server.Command;
import tigase.server.Packet;

import tigase.xml.Element;

import tigase.xmpp.Authorization;
import tigase.xmpp.BareJID;
import tigase.xmpp.NotAuthorizedException;
import tigase.xmpp.XMPPException;
import tigase.xmpp.XMPPProcessor;
import tigase.xmpp.XMPPProcessorIfc;
import tigase.xmpp.XMPPResourceConnection;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 * Describe class JabberIqCommand here.
 *
 *
 * Created: Mon Jan 22 22:41:17 2007
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class JabberIqCommand extends XMPPProcessor implements XMPPProcessorIfc {
	private static final String[] ELEMENTS = { "command" };
	private static final Logger log = Logger.getLogger("tigase.xmpp.impl.JabberIqCommand");
	private static final String[] XMLNSS = { Command.XMLNS };
	private static final String XMLNS = Command.XMLNS;
	private static final String ID = XMLNS;
	private static final Element[] DISCO_FEATURES = {
		new Element("feature", new String[] { "var" }, new String[] { XMLNS }) };

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public String id() {
		return ID;
	}

	/**
	 * Method description
	 *
	 *
	 * @param packet
	 * @param session
	 * @param repo
	 * @param results
	 * @param settings
	 *
	 * @throws XMPPException
	 */
	@Override
	public void process(final Packet packet, final XMPPResourceConnection session,
			final NonAuthUserRepository repo, final Queue<Packet> results,
				final Map<String, Object> settings)
			throws XMPPException {
		if (session == null) {
			return;
		}

		// Processing only commands (that should be guaranteed by name space)
		// and only unknown commands. All known commands are processed elsewhere
//  if (!packet.isCommand() || packet.getCommand() != Command.OTHER) {
//    return;
//  }
		try {
			if (log.isLoggable(Level.FINEST)) {
				log.log(Level.FINEST, "Received packet: {0}", packet);
			}

			// Not needed anymore. Packet filter does it for all stanzas.
//    // For all messages coming from the owner of this account set
//    // proper 'from' attribute. This is actually needed for the case
//    // when the user sends a message to himself.
//    if (packet.getFrom().equals(session.getConnectionId())) {
//      packet.getElement().setAttribute("from", session.getJID());
//    } // end of if (packet.getFrom().equals(session.getConnectionId()))
			if (packet.getStanzaTo() == null) {

				// No need for that, initVars(...) takes care of that
				// packet.getElement().setAttribute("to", session.getSMComponentId().toString());
				packet.initVars(packet.getStanzaFrom(), session.getSMComponentId());
				// TODO: Code below prevents from executing commands sent to vhost set to
				// the same value as the machine hostname. Not sure why it was implemented
				// that way. Maybe it caused internal, infinite loop in some cases.
				// More testing is needed....
//			} else {
//				if (packet.getStanzaTo().equals(session.getSMComponentId())) {
//					// This should be handled by SM, if it is not then drop it here.
//					if (log.isLoggable(Level.FINEST)) {
//						log.log(Level.FINEST, "Dropping unhandled packet addressed to SM: {0}", packet);
//					}
//					return;
//				}
			}

			BareJID id = packet.getStanzaTo().getBareJID();

			if (session.isUserId(id)) {

				// Yes this is message to 'this' client
				Packet result = packet.copyElementOnly();

				result.setPacketTo(session.getConnectionId(packet.getStanzaTo()));
				result.setPacketFrom(packet.getTo());
				results.offer(result);
			} else {

				// This is message to some other client
				Packet result = packet.copyElementOnly();

				results.offer(result);
			}    // end of else
		} catch (NotAuthorizedException e) {
			log.log(Level.WARNING, "NotAuthorizedException for packet: {0}", packet);
			results.offer(Authorization.NOT_AUTHORIZED.getResponseMessage(packet,
					"You must authorize session first.", true));
		}    // end of try-catch
	}

	/**
	 * Method description
	 *
	 *
	 * @param session
	 *
	 * @return
	 */
	@Override
	public Element[] supDiscoFeatures(final XMPPResourceConnection session) {
		return DISCO_FEATURES;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public String[] supElements() {
		return ELEMENTS;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public String[] supNamespaces() {
		return XMLNSS;
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com