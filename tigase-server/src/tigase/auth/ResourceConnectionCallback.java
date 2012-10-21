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
package tigase.auth;

import javax.security.auth.callback.Callback;
import tigase.xmpp.XMPPResourceConnection;

/**
 * Describe class ResourceConnectionCallback here.
 *
 *
 * Created: Sat Feb 18 14:13:34 2006
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class ResourceConnectionCallback implements Callback {

	private XMPPResourceConnection connection = null;

	public void setResourceConnection(final XMPPResourceConnection connection) {
		this.connection = connection;
	}

	public XMPPResourceConnection getResourceConnection() {
		return connection;
	}

} // ResourceConnectionCallback