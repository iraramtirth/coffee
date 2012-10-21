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

package tigase.util;

//~--- non-JDK imports --------------------------------------------------------

import tigase.server.Packet;

import tigase.xmpp.XMPPProcessorIfc;
import tigase.xmpp.XMPPResourceConnection;

//~--- classes ----------------------------------------------------------------

/**
 * Created: Apr 21, 2009 9:05:23 PM
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class QueueItem {
	private XMPPResourceConnection conn;
	private Packet packet;
	private XMPPProcessorIfc processor;

	//~--- constructors ---------------------------------------------------------

	/**
	 * Constructs ...
	 *
	 *
	 * @param processor
	 * @param packet
	 * @param conn
	 */
	public QueueItem(XMPPProcessorIfc processor, Packet packet, XMPPResourceConnection conn) {
		if (processor == null) {
			throw new NullPointerException("Processor parameter cannot be null!");
		}

		if (packet == null) {
			throw new NullPointerException("Packet parameter cannot be null!");
		}

		this.processor = processor;
		this.packet = packet;
		this.conn = conn;
	}

	//~--- get methods ----------------------------------------------------------

	/**
	 * @return the conn
	 */
	public XMPPResourceConnection getConn() {
		return conn;
	}

	/**
	 * @return the packet
	 */
	public Packet getPacket() {
		return packet;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public XMPPProcessorIfc getProcessor() {
		return processor;
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
