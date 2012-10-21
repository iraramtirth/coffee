
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
package tigase.db.comp;

//~--- non-JDK imports --------------------------------------------------------

import tigase.server.Command;
import tigase.server.Packet;

import tigase.xml.Element;

//~--- classes ----------------------------------------------------------------

/**
 * Created: Sep 23, 2010 6:53:14 PM
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public abstract class RepositoryItemAbstract implements RepositoryItem {

	/** Field description */
	public static final String OWNER_LABEL = "Owner";

	/** Field description */
	public static final String OWNER_ATT = "owner";

	/** Field description */
	public static final String ADMINS_LABEL = "Administrators";

	/** Field description */
	public static final String ADMINS_ATT = "admins";

	//~--- fields ---------------------------------------------------------------

	private String[] admins = null;
	private String owner = null;

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public abstract String getElemName();

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param packet
	 */
	@Override
	public void addCommandFields(Packet packet) {
		Command.addFieldValue(packet, OWNER_LABEL,
				(owner != null) ? owner : packet.getStanzaTo().getBareJID().toString());
		Command.addFieldValue(packet, ADMINS_LABEL, adminsToString(admins));
	}

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public String[] getAdmins() {
		return admins;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public String getOwner() {
		return owner;
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param packet
	 */
	@Override
	public void initFromCommand(Packet packet) {
		owner = Command.getFieldValue(packet, OWNER_LABEL);

		if ((owner == null) || owner.trim().isEmpty()) {
			owner = packet.getStanzaFrom().getBareJID().toString();
		}

		admins = adminsFromString(Command.getFieldValue(packet, ADMINS_LABEL));
	}

	/**
	 * Method description
	 *
	 *
	 * @param elem
	 */
	@Override
	public void initFromElement(Element elem) {
		owner = elem.getAttribute(OWNER_ATT);
		admins = adminsFromString(elem.getAttribute(ADMINS_ATT));
	}

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param id
	 *
	 * @return
	 */
	@Override
	public boolean isAdmin(String id) {
		if (admins == null) {
			return false;
		}

		for (String admin : admins) {
			if (admin.equals(id)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Method description
	 *
	 *
	 * @param id
	 *
	 * @return
	 */
	@Override
	public boolean isOwner(String id) {
		return ((owner == null) ? false : owner.equals(id));
	}

	//~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param admins
	 */
	@Override
	public void setAdmins(String[] admins) {
		this.admins = admins;
	}

	/**
	 * Method description
	 *
	 *
	 * @param owner
	 */
	@Override
	public void setOwner(String owner) {
		this.owner = owner;
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public Element toElement() {
		Element elem = new Element(getElemName());

		if (owner != null) {
			elem.addAttribute(OWNER_ATT, owner);
		}

		if (admins != null) {
			elem.addAttribute(ADMINS_ATT, adminsToString(admins));
		}

		return elem;
	}

	private String[] adminsFromString(String admins_m) {
		String[] result = null;

		if ((admins_m != null) && (admins_m.trim().length() > 0)) {
			String[] tmp = admins_m.split(",");

			result = new String[tmp.length];

			for (int i = 0; i < tmp.length; i++) {
				result[i] = tmp[i].trim();
			}
		}

		return result;
	}

	private String adminsToString(String[] admins_m) {
		StringBuilder sb = new StringBuilder(100);

		if ((admins_m != null) && (admins_m.length > 0)) {
			for (String adm : admins_m) {
				if (sb.length() == 0) {
					sb.append(adm);
				} else {
					sb.append(',').append(adm);
				}
			}
		}

		return sb.toString();
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
