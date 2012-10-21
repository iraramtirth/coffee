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

package tigase.xmpp;

//~--- non-JDK imports --------------------------------------------------------

import tigase.server.Packet;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;
import java.util.Queue;

//~--- interfaces -------------------------------------------------------------

/**
 * Describe interface XMPPStopListener here.
 *
 *
 * Created: Sat Oct 14 16:14:18 2006
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public interface XMPPStopListenerIfc extends XMPPImplIfc {
	void stopped(XMPPResourceConnection session, Queue<Packet> results, Map<String, Object> settings);
}    // XMPPStopListener


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
