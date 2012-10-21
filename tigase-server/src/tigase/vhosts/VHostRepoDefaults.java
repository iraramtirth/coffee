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

package tigase.vhosts;

//~--- non-JDK imports --------------------------------------------------------

import tigase.util.DNSResolver;

import tigase.xmpp.BareJID;

import static tigase.conf.Configurable.*;

//~--- classes ----------------------------------------------------------------

/**
 * Created: Oct 3, 2009 4:26:09 PM
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public abstract class VHostRepoDefaults {
	private static final BareJID vhost_user = BareJID.bareJIDInstanceNS("vhost-manager");
	private static final String vhost_list_pkey = "vhosts-lists";

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public static String getConfigKey() {
		return HOSTNAMES_PROP_KEY;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public static String[] getDefaultPropetyItems() {
		return DNSResolver.getDefHostNames();
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public static VHostItem getItemInstance() {
		return new VHostItem();
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public static String getItemsListPKey() {
		return vhost_list_pkey;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public static String getPropertyKey() {
		return GEN_VIRT_HOSTS;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public static BareJID getRepoUser() {
		return vhost_user;
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
