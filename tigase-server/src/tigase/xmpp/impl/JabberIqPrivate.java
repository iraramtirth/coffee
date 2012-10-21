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
import tigase.db.TigaseDBException;

import tigase.server.Packet;

import tigase.xml.DomBuilderHandler;
import tigase.xml.Element;
import tigase.xml.SimpleParser;
import tigase.xml.SingletonFactory;

import tigase.xmpp.Authorization;
import tigase.xmpp.NotAuthorizedException;
import tigase.xmpp.StanzaType;
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
 * Describe class JabberIqPrivate here.
 *
 *
 * Created: Mon Apr 16 08:28:18 2007
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class JabberIqPrivate extends XMPPProcessor implements XMPPProcessorIfc {

	/**
	 * Private logger for class instancess.
	 */
	private static Logger log = Logger.getLogger("tigase.xmpp.impl.JabberIqPrivate");
	private static final String XMLNS = "jabber:iq:private";
	private static final String PRIVATE_KEY = XMLNS;
	private static final String ID = XMLNS;
	private static final String[] ELEMENTS = { "query" };
	private static final String[] XMLNSS = { XMLNS };
	private static final Element[] DISCO_FEATURES = {
		new Element("feature", new String[] { "var" }, new String[] { XMLNS }) };
	private static final SimpleParser parser = SingletonFactory.getParserInstance();

	//~--- methods --------------------------------------------------------------

	// Implementation of tigase.xmpp.XMPPImplIfc

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
	 * @param session a <code>XMPPResourceConnection</code> value
	 * @param repo a <code>NonAuthUserRepository</code> value
	 * @param results a <code>Queue</code> value
	 * @param settings
	 * @throws XMPPException
	 */
	@Override
	public void process(Packet packet, XMPPResourceConnection session,
			NonAuthUserRepository repo, Queue<Packet> results, Map<String, Object> settings)
			throws XMPPException {

		// Don't do anything if session is null
		if (session == null) {
			log.info("Session null, dropping packet: " + packet.toString());

			return;
		}    // end of if (session == null)

		try {
			if ((packet.getStanzaTo() != null)
					&&!session.isUserId(packet.getStanzaTo().getBareJID())) {
				results.offer(Authorization.SERVICE_UNAVAILABLE.getResponseMessage(packet,
						"You are not authorized to access this private storage.", true));

				return;
			}

			if (packet.getFrom().equals(session.getConnectionId())) {
				List<Element> elems = packet.getElemChildren("/iq/query");

				if ((elems != null) && (elems.size() > 0)) {
					Element elem = elems.get(0);
					StanzaType type = packet.getType();

					switch (type) {
						case get :
							String priv = session.getData(PRIVATE_KEY, elem.getName() + elem.getXMLNS(),
								null);

							if (log.isLoggable(Level.FINEST)) {
								log.finest("Loaded private data for key: " + elem.getName() + ": " + priv);
							}

							if (priv != null) {
								results.offer(parseXMLData(priv, packet));

								break;
							}

							results.offer(packet.okResult((String) null, 2));

							break;

						case set :
							if (log.isLoggable(Level.FINEST)) {
								log.finest("Saving private data: " + elem.toString());
							}

							session.setData(PRIVATE_KEY, elem.getName() + elem.getXMLNS(), elem.toString());
							results.offer(packet.okResult((String) null, 0));

							break;

						case result :

							// Should never happen, it is an error and should be ignored
							break;

						default :
							results.offer(Authorization.BAD_REQUEST.getResponseMessage(packet,
									"Request type is incorrect", false));

							break;
					}    // end of switch (type)
				} else {
					results.offer(Authorization.NOT_ACCEPTABLE.getResponseMessage(packet,
							"Missing query child element", true));
				}
			} else {
				results.offer(Authorization.NOT_AUTHORIZED.getResponseMessage(packet,
						"You are not authorized to access this private storage.", true));
			}    // end of else
		} catch (NotAuthorizedException e) {
			log.warning("Received privacy request but user session is not authorized yet: "
					+ packet.toString());
			results.offer(Authorization.NOT_AUTHORIZED.getResponseMessage(packet,
					"You must authorize session first.", true));
		} catch (TigaseDBException e) {
			log.warning("Database proble, please contact admin: " + e);
			results.offer(Authorization.INTERNAL_SERVER_ERROR.getResponseMessage(packet,
					"Database access problem, please contact administrator.", true));
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
	public Element[] supDiscoFeatures(final XMPPResourceConnection session) {
		return DISCO_FEATURES;
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

	private Packet parseXMLData(String data, Packet packet) {
		DomBuilderHandler domHandler = new DomBuilderHandler();

		parser.parse(domHandler, data.toCharArray(), 0, data.length());

		Queue<Element> elems = domHandler.getParsedElements();
		Packet result = packet.okResult((Element) null, 1);
		Element query = result.getElement().findChild("/iq/query");

		for (Element el : elems) {
			query.addChild(el);
		}    // end of for (Element el: elems)

		return result;
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
