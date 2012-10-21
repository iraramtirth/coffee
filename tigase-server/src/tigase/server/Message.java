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

package tigase.server;

//~--- non-JDK imports --------------------------------------------------------

import tigase.util.TigaseStringprepException;

import tigase.xml.Element;

import tigase.xmpp.JID;
import tigase.xmpp.StanzaType;

//~--- classes ----------------------------------------------------------------

/**
 * Created: Dec 31, 2009 8:38:38 PM
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class Message extends Packet {

	/** Field description */
	public static final String ELEM_NAME = "message";

	// ~--- constructors ---------------------------------------------------------

	/**
	 * Constructs ...
	 * 
	 * 
	 * @param elem
	 * 
	 * @throws TigaseStringprepException
	 */
	public Message(Element elem) throws TigaseStringprepException {
		super(elem);
	}

	/**
	 * Constructs ...
	 * 
	 * 
	 * @param elem
	 * @param stanzaFrom
	 * @param stanzaTo
	 */
	public Message(Element elem, JID stanzaFrom, JID stanzaTo) {
		super(elem, stanzaFrom, stanzaTo);
	}

	// ~--- get methods ----------------------------------------------------------

	/**
	 * Creates a packet with message stanza.
	 * 
	 * 
	 * @param from
	 *          is a <code>JID</code> instance with message source address.
	 * @param to
	 *          is a <code>JID</code> instance with message destination address.
	 * @param type
	 *          is a <code>StanzaType</code> object with the message type.
	 * @param body
	 *          is a <code>String</code> object with message body content.
	 * @param subject
	 *          is a <code>String</code> object with message subject.
	 * @param thread
	 *          is a <code>String</code> object with message thread.
	 * @param id
	 *          is a <code>String</code> object with packet id value. Normally we
	 *          do not set packet IDs for messages but in some cases this might be
	 *          useful.
	 * 
	 * @return a new <code>Packet</code> instance (more specificaly
	 *         <code>Message</code> instance) with the message stanza.
	 */
	public static Packet getMessage(JID from, JID to, StanzaType type, String body,
			String subject, String thread, String id) {
		Element message = new Element("message", new Element[] { new Element("body", body) },
			null, null);
		message.setXMLNS(CLIENT_XMLNS);

		if (from != null) {
			message.addAttribute("from", from.toString());
		}

		if (to != null) {
			message.addAttribute("to", to.toString());
		}

		if (type != null) {
			message.addAttribute("type", type.name());
		}

		if (id != null) {
			message.addAttribute("id", id);
		}

		if (subject != null) {
			message.addChild(new Element("subject", subject));
		}

		if (thread != null) {
			message.addChild(new Element("thread", thread));
		}

		return packetInstance(message, from, to);
	}
}

// ~ Formatted in Sun Code Convention

// ~ Formatted by Jindent --- http://www.jindent.com
