
/*
* @(#)Packet.java   2010.01.16 at 07:05:30 GMT
*
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

//~--- JDK imports ------------------------------------------------------------

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

//~--- classes ----------------------------------------------------------------

/**
 * Objects of this class carry a single XMPP packet (stanza).
 * The XMPP stanza is carried as an XML element in DOM structure by the
 * Packet object which contains some extra information and convenience methods
 * to quickly access the most important stanza information.<p/>
 * The stanza is accessible directly through the <code>getElement()</code> method
 * and then it can be handles as an XML object. <br/>
 * <strong>Please note! Even though the <code>Packet</code> object and carried the
 * stanza <code>Element</code> is not unmodifiable it should be treated as such. This
 * particular <code>Packet</code> can be processed concurrently at the same time in
 * different components or plugins of the Tigase server. Modifying it may lead to
 * unexpected and hard to diagnoze behaviours. Every time you want to change or
 * update the object you should obtaina a copy of it using one of the utility methods:
 * <code>copyElementOnly()</code>, <code>swapFromTo(...)</code>,
 * <code>errorResult(...)</code>, <code>okResult(...)</code>,
 * <code>swapStanzaFromTo(...)</code></strong><p/>
 * There are no public constructors for the class, instead you have to use factory
 * methods: <code>packetInstance(...)</code> which return instance of one of the
 * classes: <code>Iq</code>, <code>Message</code> or <code>Presence</code>.
 * While creating a new <code>Packet</code> instance JIDs are parsed and processed
 * through the stringprep. Hence some of the factory methods may throw
 * <code>TigaseStringprepException</code> exception. You can avoid this by using
 * the methods which accept preparsed JIDs. Reusing preparsed JIDs is highly
 * recommended.
 * <p/>
 * There are 3 kinds of addresses available from the <code>Packet</code> object:
 * <em>PacketFrom/To</em>, <em>StanzaFrom/To</em> and <em>From/To</em>.<br/>
 * <em>Stanza</em> addresses are the normal XMPP addresses parsed from the XML
 * stanza and as a convenience are available through methods as JID objects. This is
 * not only convenient to the developer but also this is important for performance
 * reasons as parsing JID and processing it through stringprep is quite expensive
 * operation so it is better to do it once and reuse the parsed objects. Please note
 * that any of them can be null. Note also. You should avoid parsing stanza JIDs
 * from the XML element in your code as this may impact the server performance.
 * Reuse the JIDs provided from the <code>Packet</code> methods.<br/>
 * <em>Packet</em> addresses are also JID objects but they may contain a different
 * values from the <em>Stanza</em> addresses. These are the Tigase internal
 * addresses used by the server and they usually contain Tigase component source
 * and destination address. In most cases they are used between connection managers
 * and session managers and can be ignored by other code. One advantage of setting
 * <code>PacketFrom</code> address to address of your component
 * (<code>getComponentId()</code>) address is that if there is a packet delivery problem
 * it will be returned back to the sender with apropriate error message.<br/>
 * <em>Simple From/To</em> addresses contains values following the logic: If
 * PacketFrom/To is not null then it contains PacketFrom/To values otherwise it
 * contains StanzaFrom/To values. This is because the Tigase server tries always
 * to deliver and process the <code>Packet</code> using PacketFrom/To addresses if
 * they are null then Stanza addresses are used instead. So these are just convenience
 * methods which allow avoiding extra <code>IFs</code> in the program code and also
 * save some CPU cycles.
 *
 *
 * Created: Tue Nov 22 07:07:11 2005
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class Packet {
	private static final String ERROR_NS = "urn:ietf:params:xml:ns:xmpp-stanzas";

	/**
	 * The variable control whether the toStringSecure() hides all the CData information
	 * from stanzas printed to logs or logs the full, detailed stanza content. By default the
	 * variable is set to 'false' to protect users' privacy and not reveail chat content.
	 * This is the value to be used in all production/live systems. For the debug purposes
	 * on the test or development system it can be set to 'true' to help diagnose run-time
	 * problems.<p/>
	 * You can change value of the field by setting system property:
	 * <code>'packet.debug.full'</code> to <code>'true'</code>.
	 */
	public static boolean FULL_DEBUG = Boolean.getBoolean("packet.debug.full");
	public static final String CLIENT_XMLNS = "jabber:client";

	//~--- fields ---------------------------------------------------------------

///**
// * For internal Tigase use only. The session manager stores in stanza the old, original
// * address while the packet is processd. This is sometimes necessary as the SM works
// * for many virtual domains and the main SM address may be different from the address
// * the user has put to the stanza.
// */
//public static final String OLDFROM = "oldfrom";
//
///**
// * Constant <code>OLDTO</code> is kind of hack to store old request address
// * when the packet is processed by the session manager. The problem is that
// * SessionManager may work for many virtual domains but has just one real
// * address. So to forward the request to the SessionManager the 'to' address
// * is replaced with the real SessionManager address. The response however
// * needs to be sent with the 'from' address as the original request was 'to'.
// * Therefore 'oldto' attribute temporarly stores the old 'to' address
// * and after the packet processing is completed the 'from' attribute
// * is replaced with original 'to' value.
// *
// */
//public static final String OLDTO = "oldto";
	private JID packetFrom = null;
	private JID packetTo = null;
	private String packetToString = null;
	private String packetToStringSecure = null;
	private Set<String> processorsIds = new LinkedHashSet<String>(4, 0.9f);
	private JID stanzaFrom = null;
	private String stanzaId = null;
	private JID stanzaTo = null;
	private Priority priority = Priority.NORMAL;
	private Permissions permissions = Permissions.NONE;
	protected Element elem;
	private boolean routed;
	private StanzaType type;

	//~--- constructors ---------------------------------------------------------

	/**
	 * A constructor creating the <code>Packet</code> instance. This is not part of
	 * the public API, please use <code>packetInstance(...)</code> instead.
	 *
	 * @param elem is XML element with a single XMPP stanza.
	 * @throws TigaseStringprepException exception is thrown if the stanza source or
	 * destination address stringprep processing failed.
	 */
	protected Packet(final Element elem) throws TigaseStringprepException {
		setElem(elem);
		initVars();
	}

	/**
	 * A constructor creating the <code>Packet</code> instance. This is not part of
	 * the public API, please use <code>packetInstance(...)</code> instead.
	 *
	 * @param elem is XML element with a single XMPP stanza.
	 * @param stanzaFrom is a source JID address of the stanza passed as the
	 * contructor parameter.
	 * @param stanzaTo is a destination JID address of the stanza passed as the
	 * constructor parameter.
	 */
	protected Packet(final Element elem, JID stanzaFrom, JID stanzaTo) {
		setElem(elem);
		initVars(stanzaFrom, stanzaTo);
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param el
	 *
	 * @return
	 */
	public static String elemToString(Element el) {
		String elemData = el.toString();
		int size = elemData.length();

		if (size > 1024) {
			elemData = elemData.substring(0, 1024) + " ... ";
		}

		return elemData;
	}

	/**
	 * Method description
	 *
	 *
	 * @param el
	 *
	 * @return
	 */
	public static String elemToStringSecure(Element el) {
		String elemData = el.toStringSecure();
		int size = elemData.length();

		if (size > 1024) {
			elemData = elemData.substring(0, 1024) + " ... ";
		}

		return elemData;
	}

	/**
	 * The method returns <code>Packet</code> instance.
	 * More specificly it returns instance of one of the following classes: <code>Iq</code>,
	 * <code>Message</code> or <code>Presence</code>. It takes stanza XML element
	 * as an arguments, parses some the most commonly used data and created an object.
	 * Preparsed information are: stanza from/to addresses, stanza id, type and presets
	 * the <code>Packet</code> priority.<p/>
	 * If there is a stringprep processing error for either the stanza source or destination
	 * address <code>TigaseStringprepException</code> exception is thrown.
	 * @param elem is a stanza XML <code>Element</code>
	 * @return a <code>Packet</code> instance, more specificly instance of one of the
	 * following classes: <code>Iq</code>, <code>Message</code> or <code>Presence</code>.
	 * @throws TigaseStringprepException if there is stanza from or to address parsing
	 * error.
	 */
	public static Packet packetInstance(Element elem) throws TigaseStringprepException {
		if (elem.getName() == Message.ELEM_NAME) {
			return new Message(elem);
		}

		if (elem.getName() == Presence.ELEM_NAME) {
			return new Presence(elem);
		}

		if (elem.getName() == Iq.ELEM_NAME) {
			return new Iq(elem);
		}

		return new Packet(elem);
	}

	/**
	 * The method returns <code>Packet</code> instance.
	 * More specificly it returns instance of one of the following classes: <code>Iq</code>,
	 * <code>Message</code> or <code>Presence</code>. It takes stanza XML element
	 * as an arguments and preparsed stanza from and to addresses. The method
	 * parses some other, the most commonly used data and created an object.
	 * Preparsed information are: stanza id, type and presets the <code>Packet</code>
	 * priority.<p/>
	 * This method does not parses stanza from and stanza to address from the given XML
	 * document, hence it does not throw <code>TigaseStringprepException</code>. Even
	 * though reusing parsed from and to address is highly recommended an extra care
	 * is needed to pass correct parameters as stanza JIDs or the packet may be
	 * incorrectly routed or processed.
	 *
	 * @param elem is the stanza XML <code>Element</code>
	 * @param stanzaFrom is a preparsed <code>JID</code> instance from the given stanza
	 * XML element.
	 * @param stanzaTo is a preparsed <code>JID</code> instance from the given stanza
	 * XML element.
	 * @return a <code>Packet</code> instance, more specificly instance of one of the
	 * following classes: <code>Iq</code>, <code>Message</code> or <code>Presence</code>.
	 */
	public static Packet packetInstance(Element elem, JID stanzaFrom, JID stanzaTo) {
		if (elem.getName() == Message.ELEM_NAME) {
			return new Message(elem, stanzaFrom, stanzaTo);
		}

		if (elem.getName() == Presence.ELEM_NAME) {
			return new Presence(elem, stanzaFrom, stanzaTo);
		}

		if (elem.getName() == Iq.ELEM_NAME) {
			return new Iq(elem, stanzaFrom, stanzaTo);
		}

		return new Packet(elem, stanzaFrom, stanzaTo);
	}

	/**
	 * The method creates XML stanza from given parameters and returns
	 * <code>Packet</code> instance for this XML stanza.
	 * More specificly it returns instance of one of the following classes: <code>Iq</code>,
	 * <code>Message</code> or <code>Presence</code>. <p/>
	 * The method first builds an XML stanza from given parameters: element name,
	 * from and to addresses and stanza type, then it creates a Packet instance for the
	 * stanza. It also runs all the parsing and stringprep processing, hence it throws
	 * an exception if any error is found.
	 * @param el_name XML stanza elemen name as <code>String</code>.
	 * @param from is the stanza <strong>from</strong> address as <code>String</code>
	 * @param to is the stanza <strong>to</strong> address as <code>String</code>.
	 * @param type is one of the stanza types: <strong>set</strong>, <strong>get</strong>,
	 * <strong>result</strong>, .... as <code>StanzaType</code> instance.
	 * @return a <code>Packet</code> instance, more specificly instance of one of the
	 * following classes: <code>Iq</code>, <code>Message</code> or <code>Presence</code>.
	 * @throws TigaseStringprepException if there is stanza from or to address parsing
	 * error.
	 */
	public static Packet packetInstance(String el_name, String from, String to, StanzaType type)
			throws TigaseStringprepException {
		Element elem = new Element(el_name, new String[] { "from", "to", "type" }, new String[] { from,
				to, type.toString() });

		return packetInstance(elem);
	}

	/**
	 * <code>copyElementOnly</code> method creates a copy of the packet with stanza
	 * information copied only. The <code>Packet</code> specific information stays
	 * blank (NULL): (packetFrom, packetTo, etc...).<p/>
	 * This method should be used to obtain a copy of the packet without setting
	 * packet specific fields (packetFrom or packetTo). The method reuses preparsed
	 * stanza JIDs and does not throw any exception.
	 * @return a new copy of the packet with packet specific fields set to NULL.
	 */
	public Packet copyElementOnly() {
		Element res_elem = elem.clone();
		Packet result = packetInstance(res_elem, getStanzaFrom(), getStanzaTo());

		result.setPriority(priority);

		return result;
	}

	/**
	 * Method returns a string representation of all the data enclosed by the
	 * <code>Packet</code> intance. All stanza XML element and all fields are converted
	 * to the <code>String</code> representation for debugging. Please note, this may
	 * be resources consuming process so use it only when experiencing problems with
	 * <code>Packet</code> content.
	 *
	 *
	 * @return <code>String</code> representation of the packet with all its fields.
	 */
	public String debug() {
		return toString() + ", stanzaFrom=" + stanzaFrom + ", stanzaTo=" + stanzaTo;
	}

	/**
	 * Method returns a modified copy of the <code>Packet</code> with its stanza as
	 * stanza error used for reporting errors. It is recommended not to use this
	 * method directly as there is a utility class which makes generating error responses
	 * much simpler. An example call (which uses this method underneath) looks like this
	 * example:
	 * <pre>
	 * import tigase.xmpp.Authorization;
	 * Authorization.BAD_REQUEST.getResponseMessage(packet, "Error message", true/false);
	 * </pre>
	 * This utility class and it's method acts not only as a convenience but also provides
	 * some additional checking and control.
	 *
	 *
	 * @param errorType is a <code>String</code> representation of the error type defined
	 * in the XMPP RFC-3920.
	 * @param errorCode is an integer error code defined in the XMPP RFC for backward
	 * compatibility with old Jabber implementatons.
	 * @param errorCondition is a <code>String</code> representation of the error condition
	 * defined in the XMPP RFC-3920.
	 * @param errorText human readable error message.
	 * @param includeOriginalXML a boolean parameter indicating whether stanza top element
	 * children should be included in the error message.
	 *
	 * @return a new <code>Packet</code> instance with an error type stanza which
	 * is a response to this <code>Packet</code> instance.
	 */
	public Packet errorResult(final String errorType, final Integer errorCode,
			final String errorCondition, final String errorText, final boolean includeOriginalXML) {
		Element reply = new Element(elem.getName());

		reply.setAttribute("type", StanzaType.error.toString());

		// This is not needed anymore, initVars(...) takes care of that
//  if (getStanzaFrom() != null) {
//    reply.setAttribute("to", getStanzaFrom().toString());
//  }    // end of if (getElemFrom() != null)
//
//  if (getStanzaTo() != null) {
//    reply.setAttribute("from", getStanzaTo().toString());
//  }    // end of if (getElemTo() != null)
		if (getStanzaId() != null) {
			reply.setAttribute("id", getStanzaId());
		}    // end of if (getElemId() != null)

		if (includeOriginalXML) {
			reply.addChildren(elem.getChildren());
		}    // end of if (includeOriginalXML)

//  if (getAttribute(OLDTO) != null) {
//    reply.setAttribute(OLDTO, getAttribute(OLDTO));
//  }
		if (getXMLNS() != null) {
			reply.setXMLNS(getXMLNS());
		}

		Element error = new Element("error");

		if (errorCode != null) {
			error.setAttribute("code", errorCode.toString());
		}

		error.setAttribute("type", errorType);

		Element cond = new Element(errorCondition);

		cond.setXMLNS(ERROR_NS);
		error.addChild(cond);

		if (errorText != null) {
			Element t = new Element("text", errorText, new String[] { "xml:lang", "xmlns" },
				new String[] { "en",
					ERROR_NS });

			error.addChild(t);
		}    // end of if (text != null && text.length() > 0)

		reply.addChild(error);

		return swapFromTo(reply, getStanzaTo(), getStanzaFrom());
	}

	//~--- get methods ----------------------------------------------------------

	/**
	 * A convenience method for accessing stanza top element attributes. This call is
	 * equal to the call:
	 * <pre>
	 * packet.getElement().getAttribute(key);
	 * </pre>
	 *
	 *
	 * @param key is an attribute key.
	 *
	 * @return an attribute value or NULL if there is no such attribute.
	 */
	public String getAttribute(String key) {
		return elem.getAttribute(key);
	}

	/**
	 * A convenience method for accessing stanza top level or any of it's children
	 * attribute. This call is equal to the call:
	 * <pre>
	 * packet.getElement().getAttribute(xmlPath, key);
	 * </pre>
	 *
	 * @param path is XML path for the stanza element or stanza child for which attribute
	 * is retrieved.
	 * @param key is an attribute key.
	 *
	 * @return
	 */
	public String getAttribute(String path, String key) {
		return elem.getAttribute(path, key);
	}

	/**
	 * The method alwats returns NULL. It is overwritten in the <code>Iq</code> class
	 * where it returns a command identifier if the <em>iq</code> stanza represnts an
	 * ad-hoc command. It is provided here is a convenience so the developer does not
	 * have to cast the packet to IQ before retrieving the command id.
	 *
	 *
	 * @return the method always returns a NULL.
	 */
	public Command getCommand() {
		return null;
	}

	/**
	 * Method returns character data from the enclosed stanza for a given stanza element
	 * or child pointed by the <code>xmlPath</code> parameter.
	 * This call is equal to the call:
	 * <pre>
	 * packet.getElement().getCData(xmlPath);
	 * </pre>
	 *
	 * @param xmlPath is an XML path to the stanza element for which CData is retrieved.
	 *
	 * @return CData for a given element or NULL if the element does not exist or there is
	 * no CData for the element.
	 */
	public String getElemCData(String xmlPath) {
		return elem.getCData(xmlPath);
	}

	/**
	 * Method return character data for the stanza top element.
	 * This call is equal to the call:
	 * <pre>
	 * packet.getElement().getCData();
	 * </pre>
	 *
	 *
	 * @return CData or from the stanza top element or NULL if there is no CData for the
	 * element.
	 */
	public String getElemCData() {
		return elem.getCData();
	}

	/**
	 * Method returns a list of all XML children from the enclosed stanza for a given
	 * stanza element or child pointed by the <code>xmlPath</code> parameter.
	 * This call is equal to the call:
	 * <pre>
	 * packet.getElement().getChildren(xmlPath);
	 * </pre>
	 *
	 * @param xmlPath is an XML path to the stanza element for which children are
	 * retrieved.
	 *
	 * @return children list for a given element or NULL if the element does not exist
	 * or there is no children for the element.
	 */
	public List<Element> getElemChildren(String xmlPath) {
		return elem.getChildren(xmlPath);
	}

	/**
	 * Method returns a <code>String</code> representation of the stanza source address.
	 * Use of this method is not recommended, the API is depreciated in favor of API
	 * operating on <code>JID</code> class.
	 * @return a <code>String</code> representation of the stanza source address or NULL
	 * if the source address has not been set.
	 * @deprecated use getStanzaFrom() instead.
	 */
	@Deprecated
	public String getElemFrom() {
		return (stanzaFrom != null) ? stanzaFrom.toString() : null;
	}

	/**
	 * Cnvenience method for retrieving the stanza top element name.
	 * This call is equal to the call:
	 * <pre>
	 * packet.getElement().getName();
	 * </pre>
	 *
	 * @return the stanza top element name.
	 */
	public String getElemName() {
		return elem.getName();
	}

	/**
	 * Method returns a <code>String</code> representation of the stanza destination
	 * address.
	 * Use of this method is not recommended, the API is depreciated in favor of API
	 * operating on <code>JID</code> class.
	 * @return a <code>String</code> representation of the stanza destination address or
	 * NULL if the destination address has not been set..
	 * @deprecated use getStanzaTo() instead
	 */
	@Deprecated
	public String getElemTo() {
		return (stanzaTo != null) ? stanzaTo.toString() : null;
	}

	/**
	 * Method returns the stanza XML element in DOM format.
	 *
	 *
	 * @return the stanza XML element in DOM format.
	 */
	public Element getElement() {
		return elem;
	}

	/**
	 * Method parses the stanza and returns the error condition if there is any.
	 *
	 *
	 * @return parsed stanza error condition or NULL if there is not error condition.
	 */
	public String getErrorCondition() {
		List<Element> children = elem.getChildren(elem.getName() + "/error");

		if (children != null) {
			for (Element cond : children) {
				if ( !cond.getName().equals("text")) {
					return cond.getName();
				}    // end of if (!cond.getName().equals("text"))
			}      // end of for (Element cond: children)
		}        // end of if (children == null) else

		return null;
	}

	/**
	 * Returns the packet source address. The method works as a following code:
	 * <pre>
	 * return (packetFrom != null) ? packetFrom : stanzaFrom;
	 * </pre>
	 *
	 * @return a <code>JID</code> instance of the packet source address or NULL if
	 * neither the packet source address is set nor the stanza source address is set.
	 */
	public JID getFrom() {
		return (packetFrom != null) ? packetFrom : stanzaFrom;
	}

	/**
	 * Returns the packet internal source address.
	 *
	 *
	 * @return a <code>JID>/code> instance of the packet internal source address or
	 * NULL if the packet internal source address has not been set
	 */
	public JID getPacketFrom() {
		return this.packetFrom;
	}

	/**
	 * Returns the packet internal destination address.
	 *
	 *
	 * @return a <code>JID>/code> instance of the packet internal destination address or
	 * NULL if the packet internal destination address has not been set.
	 */
	public JID getPacketTo() {
		return this.packetTo;
	}

	/**
	 * Method returns permissions set of the user who has sent the packet. Some packets
	 * carry ad-hoc commands which can change server parameters, configuration or
	 * can contains other administration commands. Such commands are not executed if
	 * the packet sender does not have enough permissions.
	 *
	 *
	 * @return a sender permissions set.
	 */
	public Permissions getPermissions() {
		return permissions;
	}

	/**
	 * Method returns the packet priority,
	 *
	 *
	 * @return the packet priority.
	 */
	public Priority getPriority() {
		return priority;
	}

	/**
	 * Method returns a set of all processor IDs which processed the packet. Each
	 * session manager processor which handles the packet can mark the packet as
	 * processed. This is used internally by the session manager to detect packets
	 * which hasn't been processed by any processor, hence a default action is
	 * applied to the apcket if possible.
	 *
	 *
	 * @return a <code>Set</code> of stanza processor IDs which handled the packet.
	 */
	public Set<String> getProcessorsIds() {
		return processorsIds;
	}

	/**
	 * Method returns source address of rhe stanza enclosed by this packet.
	 * @return a <code>JID</code> instance of the stanza source address or NULL if the
	 * source address has not been set for the stanza.
	 */
	public JID getStanzaFrom() {
		return stanzaFrom;
	}

	/**
	 * Method returns the stanza ID if set.
	 *
	 *
	 * @return a <code>String</code> representation of the stanza ID or NULL if the ID has
	 * not been set for the stanza.
	 */
	public String getStanzaId() {
		return stanzaId;
	}

	/**
	 * Method returns destinaion address of rhe stanza enclosed by this packet.
	 * @return a <code>JID</code> instance of the stanza destination address or NULL if
	 * the destination address has not been set for the stanza.
	 */
	public JID getStanzaTo() {
		return stanzaTo;
	}

	/**
	 * Returns the packet destination address. The method works as a following code:
	 * <pre>
	 * return (packetTo != null) ? packetTo : stanzaTo;
	 * </pre>
	 *
	 * @return a <code>JID</code> instance of the packet destination address or NULL if
	 * neither the packet destination address is set nor the stanza destination address
	 * is set.
	 */
	public JID getTo() {
		return (packetTo != null) ? packetTo : stanzaTo;
	}

	/**
	 * Method returns the stanza type parsed from the top XML element of the enclosed
	 * stanza.
	 *
	 *
	 * @return a <code>StanzaType</code> instance of the stanza type parsed from the
	 * top XML element of the enclosed stanza or NULL of the type has not been set.
	 */
	public StanzaType getType() {
		return type;
	}

	/**
	 * Returns the enclosed stanza top element XMLNS.
	 * This call is equal to the call:
	 * <pre>
	 * packet.getElement().getXMLNS();
	 * </pre>
	 *
	 * @return a <code>String</code> instance of the stanza top element XMLNS.
	 */
	public String getXMLNS() {
		return elem.getXMLNS();
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * The method allows for re-syncing stanza JIDs stored in the packet with the
	 * attributes of the stanza if they have been changed for any reason.
	 * <strong>Method mostly used internally only.</strong> Normally stanza carried by this
	 * Packet instance
	 * must not be changed, however there are rare occasions when it has to be changed.
	 * RFC requires that the server adds missing <em>'from'</em> attribute to every
	 * packet sent by the user. It would be highly inefficient to create a new instance
	 * of the data just to add the missing from address. In such a case SM adds missing
	 * attribute but then stanza preparsed JIDs stored in the packet are out of sync with
	 * the enclosed stanza. This method allows for setting correct stanza JIDs for the
	 * packet fields without a need to reparse the stanza.
	 *
	 *
	 * @param stanzaFrom is a parsed source address JID from the stanza enclosed by this
	 * packet.
	 * @param stanzaTo is a parsed destination address JID from the stanza enclosed by
	 * this packet.
	 */
	public void initVars(JID stanzaFrom, JID stanzaTo) {
		if (this.stanzaFrom != stanzaFrom) {
			this.stanzaFrom = stanzaFrom;

			if (stanzaFrom == null) {
				elem.removeAttribute("from");
			} else {
				elem.setAttribute("from", stanzaFrom.toString());
			}
		}

		if (this.stanzaTo != stanzaTo) {
			this.stanzaTo = stanzaTo;

			if (stanzaTo == null) {
				elem.removeAttribute("to");
			} else {
				elem.setAttribute("to", stanzaTo.toString());
			}
		}

		stanzaId = elem.getAttribute("id");
		packetToString = null;
		packetToStringSecure = null;
	}

	/**
	 * The method allows for resyncing/parsing stanza JIDs stored in the packet with the
	 * attributes of the stanza if they have been changed for any reason.
	 * <strong>Method mostly used internally only.</strong> Normally stanza carried by this
	 * Packet instance
	 * must not be changed, however there are rare occasions when it is needed.
	 * RFC requires that the server adds missing <em>'from'</em> attribute to every
	 * packet sent by the user. It would be highly inefficient to create a new instance
	 * of the data just to add the missing from address. In such a case SM adds missing
	 * attribute but then stanza preparsed JIDs stored in the packet are out of sync with
	 * the enclosed stanza. This method causes stanza JIDs reparsing and setting the packet
	 * variables.
	 *
	 *
	 * @throws TigaseStringprepException if the stringprep error occurs during the stanza
	 * JIDs parsing.
	 */
	public void initVars() throws TigaseStringprepException {
		String tmp = elem.getAttribute("to");

		if (tmp != null) {
			stanzaTo = JID.jidInstance(tmp);
		} else {
			stanzaTo = null;
		}

		tmp = elem.getAttribute("from");

		if (tmp != null) {
			stanzaFrom = JID.jidInstance(tmp);
		} else {
			stanzaFrom = null;
		}

		stanzaId = elem.getAttribute("id");
		packetToString = null;
		packetToStringSecure = null;
	}

	//~--- get methods ----------------------------------------------------------

	/**
	 * The method checks whether the stanza enclosed by this <code>Packet</code>
	 * instance is an ad-hoc command.
	 * This is a generic method which in fact always returns <code>false</code>. It
	 * is overwritten in the <code>Iq</code> class where the real checking is
	 * performed. This class has been provided as a convenience method to perform
	 * the check without a need for casting the <code>Packet</code> instance to
	 * the <code>Iq</code> class.
	 *
	 * @return a <code>boolean</code> value <code>true</code> if the stanza is
	 * an ad-hoc command and <code>false</code> otherwise.
	 */
	public boolean isCommand() {
		return false;
	}

	/**
	 * The method checks wherher the enclosed stanza is a speciifc XML element.
	 * That is, it checks whether the stanza element name and XMLNS is exactly
	 * the same as given parameters.
	 * This is a convenience method which logic is equal to the code below:
	 * <pre>
	 * return packet.getElement().getName() == name
	 *             && packet.getElement().getXMLNS() == xmlns;
	 * </pre>
	 *
	 * @param name is a <code>String</code> representing the XML element name.
	 * @param xmlns is a <code>String</code> representing the XML xmlns value.
	 *
	 * @return
	 */
	public boolean isElement(String name, String xmlns) {
		return (elem.getName() == name) && (xmlns == elem.getXMLNS());
	}

	/**
	 * Method determines whether the stanza represents so called <em>routed</em>
	 * packet.
	 * A routed packet is a packet created by a component responsible for
	 * Communication with external components. In certain work mode it can send
	 * over the link the whole packet information with all internal states and
	 * addresses. Such a packet also encloses original stanza with all it's attributes.
	 *
	 * @return a <code>boolean</code> value of <code>true</code> if the packet is
	 * routed and <code>false</code> otherwise.
	 */
	public boolean isRouted() {
		return routed;
	}

	/**
	 * A convenience method which checks whether the enclosed stanza is a service
	 * discovery query.
	 * This is a generic method which in fact always returns <code>false</code>. It
	 * is overwritten in the <code>Iq</code> class where the real checking is
	 * performed. This class has been provided as a convenience method to perform
	 * the check without a need for casting the <code>Packet</code> instance to
	 * the <code>Iq</code> class.
	 *
	 * @return a <code>boolean</code> value <code>true</code> if the stanza is
	 * a a service discovery query and <code>false</code> otherwise.
	 */
	public boolean isServiceDisco() {
		return false;
	}

	/**
	 * The method checks whether the enclosed stanza contains an XML element and
	 * XML child element for a given element path and xmlns.
	 * The <code>elementPath</code> is directory path like string.
	 *
	 * @param elementPath is a <code>String</code> value which represents XML
	 * element to a desired child element.
	 * @param xmlns is a <code>String</code> value which represents XML XMLNS.
	 *
	 * @return
	 */
	public boolean isXMLNS(String elementPath, String xmlns) {
		String this_xmlns = elem.getXMLNS(elementPath);

		if (this_xmlns == xmlns) {
			return true;
		}

		return false;
	}
	
	public void setXMLNS(String xmlns) {
		elem.setXMLNS(xmlns);
		packetToString = null;
		packetToStringSecure = null;		
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method returns a modified copy of the <code>Packet</code> with its stanza as
	 * stanza <code>result</code> used for reporting <em>IQ</em> stanza results.
	 * The method preserves all the attributes of the original stanza, swaps stanza
	 * source and destination addresses and can optionally add more child XML
	 * elements and can preserve existing children elements up to given depth.
	 *
	 * @param includeXML is an XML content serialized to <code>String</code> or just
	 * character data as <code>String</code> which has to be added to response
	 * stanza.
	 * @param originalXML parameter specified whether and if so to what depth the
	 * original stanza child elements have to be preserved in the response packet.
	 * @return a new <code>Packet</code> instance with an OK (result) type stanza
	 * which is a response to this <code>Packet</code> instance.
	 */
	public Packet okResult(final String includeXML, final int originalXML) {
		Element reply = new Element(elem.getName());
		
		if (getXMLNS() != null) {
			reply.setXMLNS(getXMLNS());
		}

		reply.setAttribute("type", StanzaType.result.toString());

		// Not needed anymore, initVars(...) takes care of that
//  if (getStanzaFrom() != null) {
//    reply.setAttribute("to", getStanzaFrom().toString());
//  }    // end of if (getElemFrom() != null)
//
//  if (getStanzaTo() != null) {
//    reply.setAttribute("from", getStanzaTo().toString());
//  }    // end of if (getElemFrom() != null)
//
		if (getStanzaId() != null) {
			reply.setAttribute("id", getStanzaId());
		}    // end of if (getElemId() != null)

//  if (getAttribute(OLDTO) != null) {
//    reply.setAttribute(OLDTO, getAttribute(OLDTO));
//  }
		Element old_child = elem;
		Element new_child = reply;

		for (int i = 0; i < originalXML; i++) {
			old_child = old_child.getChildren().get(0);

			Element tmp = new Element(old_child.getName());

			tmp.setXMLNS(old_child.getXMLNS());
			new_child.addChild(tmp);
			new_child = tmp;
		}    // end of for (int i = 0; i < originalXML; i++)

		if (includeXML != null) {
			new_child.setCData(includeXML);
		}    // end of if (includeOriginalXML)

		Packet result = swapFromTo(reply, getStanzaTo(), getStanzaFrom());

		result.setPriority(priority);

		return result;
	}

	/**
	 * Method returns a modified copy of the <code>Packet</code> with its stanza as
	 * stanza <code>result</code> used for reporting <em>IQ</em> stanza results.
	 * The method preserves all the attributes of the original stanza, swaps stanza
	 * source and destination addresses and can optionally add more child XML
	 * elements and can preserve existing children elements up to given depth.
	 *
	 * @param includeXML is an XML content which has to be added to the response
	 * stanza.
	 * @param originalXML parameter specified whether and if so to what depth the
	 * original stanza child elements have to be preserved in the response packet.
	 * @return a new <code>Packet</code> instance with an OK (result) type stanza
	 * which is a response to this <code>Packet</code> instance.
	 */
	public Packet okResult(Element includeXML, int originalXML) {
		Element reply = new Element(elem.getName());

		if (getXMLNS() != null) {
			reply.setXMLNS(getXMLNS());
		}

		reply.setAttribute("type", StanzaType.result.toString());

		// Not needed anymore, initVars(...) takes care of that
//  if (getStanzaFrom() != null) {
//    reply.setAttribute("to", getStanzaFrom().toString());
//  }    // end of if (getElemFrom() != null)
//
//  if (getStanzaTo() != null) {
//    reply.setAttribute("from", getStanzaTo().toString());
//  }    // end of if (getElemFrom() != null)
//
		if (getStanzaId() != null) {
			reply.setAttribute("id", getStanzaId());
		}    // end of if (getElemId() != null)

//  if (getAttribute(OLDTO) != null) {
//    reply.setAttribute(OLDTO, getAttribute(OLDTO));
//  }
		Element old_child = elem;
		Element new_child = reply;

		for (int i = 0; i < originalXML; i++) {
			old_child = old_child.getChildren().get(0);

			Element tmp = new Element(old_child.getName());

			tmp.setXMLNS(old_child.getXMLNS());
			new_child.addChild(tmp);
			new_child = tmp;
		}    // end of for (int i = 0; i < originalXML; i++)

		if (includeXML != null) {
			new_child.addChild(includeXML);
		}    // end of if (includeOriginalXML)

		Packet result = swapFromTo(reply, getStanzaTo(), getStanzaFrom());

		result.setPriority(priority);

		return result;
	}

	/**
	 * Returns a new <code>Packet</code> instance with stanza <em>routed</em>
	 * which means an original stanza has been enclosed inside a <code>route</code>
	 * XML element which contains additional information taken from
	 * <code>Packet</code> packet instance internal attributes.
	 *
	 * @return a new <code>Packet</code> instance with <code>route</code> stanza.
	 */
	public Packet packRouted() {
		Element routedp = new Element("route", new String[] { "to", "from" },
			new String[] { getTo().toString(),
				getFrom().toString() });

		routedp.addChild(elem);

		return packetInstance(routedp, getFrom(), getTo());
	}

	/**
	 * The method marks that the packet has been processed by a packet processor
	 * with a given ID.
	 *
	 * @param id is a <code>String</code> instance of the packet processer identifier.
	 */
	public void processedBy(String id) {
		processorsIds.add(id);
	}

	//~--- set methods ----------------------------------------------------------

	/**
	 * The method sets a source address for the <code>Packet</code> instance.
	 *
	 *
	 * @param from is a <code>JID</code> instance of the packet new source address.
	 */
	public void setPacketFrom(JID from) {
		this.packetFrom = from;
	}

	/**
	 * The method sets a destination address for the <code>Packet</code> instance.
	 *
	 *
	 * @param to is a <code>JID</code> instance of the packet new destination
	 * address.
	 */
	public void setPacketTo(JID to) {
		this.packetTo = to;
	}

	/**
	 * The method sets permissions for the packet of a user who sent the stanza.
	 *
	 *
	 * @param perm is <code>Permissions</code> instance of the stanza sender
	 * permissions calculated by the session manager.
	 */
	public void setPermissions(Permissions perm) {
		packetToString = null;
		packetToStringSecure = null;
		permissions = perm;
	}

	/**
	 * The method sets the packet priority. Depending on the priority the packet
	 * is put to a queue with corresponding priority. This matter only on system
	 * which experience overload and some packets may be delivered with a delay
	 * if they are low priority packets.
	 *
	 * @param priority os a new <code>Priority</code> instance set for the packet.
	 */
	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * The method left for compatiblity with an old API reasons. Use
	 * <code>swapStanzaFromTo()</code> instead.
	 *
	 * @return a new packet instance with a copy of the stanza element with
	 * swapped source and destination addresses.
	 * @deprecated Use <code>swapStanzaFromTo()</code> instead.
	 */
	@Deprecated
	public Packet swapElemFromTo() {
		return swapStanzaFromTo();
	}

	/**
	 * The method left for compatiblity with an old API reasons. Use
	 * <code>swapStanzaFromTo()</code> instead.
	 *
	 * @param type a new stanza type which has to be set to the generated
	 * stanza copy.
	 * @return a new packet instance with a copy of the stanza element with
	 * swapped source and destination addresses.
	 * @deprecated Use <code>swapStanzaFromTo()</code> instead.
	 */
	@Deprecated
	public Packet swapElemFromTo(final StanzaType type) {
		return swapStanzaFromTo(type);
	}

	/**
	 * The method creates a new instance of the <code>Packet</code> class with the
	 * packet source and destination addresses swapped and sets the given stanza
	 * element plus source and destination addresses for the new stanza.
	 * This method gives you slightly more flexibility as you can set any source
	 * and destination address for the new stanza.
	 * This method is rarely used in packet processors which don't sent a simple
	 * "ok result" response. Some data flow requires a completely new packet
	 * to be send as a response to the original call, but the response has to be
	 * delivered to the original sends. As an example are the SASL authentication
	 * and TLS handshaking.
	 *
	 * @param el is an XML element set for the new packet.
	 * @param stanzaFrom is the stanza source address
	 * @param stanzaTo is the stanza destination address
	 *
	 * @return a new <code>Packet</code> instance.
	 */
	public Packet swapFromTo(Element el, JID stanzaFrom, JID stanzaTo) {
		Packet packet = packetInstance(el, stanzaFrom, stanzaTo);

		packet.setPacketTo(getFrom());
		packet.setPacketFrom(getTo());
		packet.setPriority(priority);

		return packet;
	}

	/**
	 * Creates a new <code>Packet</code> instance with swapped packet source and
	 * destination addresses. Please note the new packet contains unchanged copy of the
	 * original stanza. Stanza source and destination addresses are no swapped.
	 *
	 * @return a new <code>Packet>/code> instance.
	 */
	public Packet swapFromTo() {
		Element el = elem.clone();
		Packet packet = packetInstance(el, getStanzaFrom(), getStanzaTo());

		packet.setPacketTo(getFrom());
		packet.setPacketFrom(getTo());
		packet.setPriority(priority);

		return packet;
	}

	/**
	 * The method creates a new <code>Packet</code> instance with a stanza copy
	 * with swapped source and destination addresses. The packet source and
	 * destination addresses are set to null.
	 *
	 *
	 * @return a new <code>Packet</code> instance.
	 */
	public Packet swapStanzaFromTo() {
		Element copy = elem.clone();

		// Not needed anymore, initVars(...) takes care of that
//  copy.setAttribute("to", getStanzaFrom().toString());
//  copy.setAttribute("from", getStanzaTo().toString());
		Packet result = packetInstance(copy, getStanzaTo(), getStanzaFrom());

		result.setPriority(priority);

		return result;
	}

	/**
	 * The method creates a new <code>Packet</code> instance with a stanza copy
	 * with swapped source and destination addresses and the given type set.
	 * The packet source and destination addresses are set to null.
	 *
	 *
	 * @param type is a new type for the stanza copy to set.
	 * @return a new <code>Packet</code> instance.
	 */
	public Packet swapStanzaFromTo(final StanzaType type) {
		Element copy = elem.clone();

		// Not needed anymore, initVars(...) takes care of that
//  copy.setAttribute("to", getStanzaFrom().toString());
//  copy.setAttribute("from", getStanzaTo().toString());
		copy.setAttribute("type", type.toString());

		Packet result = packetInstance(copy, getStanzaTo(), getStanzaFrom());

		result.setPriority(priority);

		return result;
	}

	/**
	 * The method converts the <code>Packet</code> instance to a <code>String</code>
	 * representation. The stanza XML element is presented as the string and all packet
	 * attributes are also added to the string.
	 * The method is for a debugging purposes to log the whole packet content to
	 * the debug file for further analysis. It is recommended to use
	 * <code>toStringSecure()</code> instead as it removes all the CData from the
	 * stanza avoiding exposing user chat message content. The secure method
	 * also preserves you from flooding your log files in case of a huge chunks of
	 * data are sent in packets (user photos in vCards or files).
	 *
	 * @return a <code>String</code> representation of the packet instance.
	 */
	public String toStringFull() {
		if (packetToString == null) {
			String elemData = elemToString(elem);

			packetToString = calcToString(elemData);

//    ", DATA=" + elemData + ", SIZE=" + elem.toString().length() + ", XMLNS="
//    + elem.getXMLNS() + ", PRIORITY=" + priority + ", PERMISSION=" + permissions;
		}

		return "from=" + packetFrom + ", to=" + packetTo + packetToString;
	}

	@Override
	public String toString() {
		return toString(FULL_DEBUG);
	}

	/**
	 * Is a convenience method which allows you to call always the same method
	 * but parametrize (configure) whether you want to get a secure packet string
	 * representation or full representation.
	 *
	 * @param secure parameter specifies whether the secure packet representation
	 * should be returned (<code>true</code> value) or the full one
	 * (<code>false</code>).
	 *
	 * @return a <code>String</code> representation of the packet instance.
	 */
	public String toString(boolean secure) {
		String result;

		if (secure) {
			result = toStringSecure();
		} else {
			result = toStringFull();
		}

		return result;
	}

	/**
	 * The method returns a <code>String</code> representation of the packet with
	 * all CData content replaced with text: <em>"CData size: NN"</em>. This is a
	 * preferable method to log the packets for debuging purposes.
	 *
	 * @return a <code>String</code> representation of the packet instance.
	 */
	public String toStringSecure() {
		if (FULL_DEBUG) {
			return toStringFull();
		} else {
			if (packetToStringSecure == null) {
				String elemData = elemToStringSecure(elem);

				packetToStringSecure = calcToString(elemData);

//      ", DATA=" + elemData + ", SIZE=" + elem.toString().length()
//      + ", XMLNS=" + elem.getXMLNS() + ", PRIORITY=" + priority + ", PERMISSION="
//        + permissions;
			}

			return "from=" + packetFrom + ", to=" + packetTo + packetToStringSecure;
		}
	}

	/**
	 * The method unpacks the original packet and stanza from <code>route</code>
	 * stanza.
	 * This is the opposite action to the <code>packRouted()</code> method.
	 *
	 *
	 * @return a new instance of the <code>Packet</code> class with unpacket
	 * packet and stanza from <code>route</code> stanza.
	 *
	 * @throws TigaseStringprepException if there was a problem with addresses
	 * stringprep processing.
	 */
	public Packet unpackRouted() throws TigaseStringprepException {
		Packet result = packetInstance(elem.getChildren().get(0));

		result.setPacketTo(getTo());
		result.setPacketFrom(getFrom());

		return result;
	}

	/**
	 * The method determines whether the packet has been processed by any of
	 * the packet processors.
	 * In fact it says whether there has been called method
	 * <code>processedBy(...)</code> on the packet.
	 *
	 * @return a <code>boolean</code> value of <code>true</code> of the packet was
	 * processed by any processor and <code>false</code> otherwise.
	 */
	public boolean wasProcessed() {
		return processorsIds.size() > 0;
	}

	/**
	 * The method checks whether the packet has been processed by a
	 * packet processor with the specified ID.
	 *
	 * @param id is a <code>String</code> instance of the packet processor identifier.
	 *
	 * @return a <code>boolean</code> value of <code>true</code> of the packet was
	 * processed by a processor with specified ID and <code>false</code> otherwise.
	 */
	public boolean wasProcessedBy(String id) {
		return processorsIds.contains(id);
	}

	private String calcToString(String elemData) {
		return ", DATA=" + elemData + ", SIZE=" + elem.toString().length() + ", XMLNS="
				+ elem.getXMLNS() + ", PRIORITY=" + priority + ", PERMISSION=" + permissions + ", TYPE="
					+ type;
	}

	//~--- set methods ----------------------------------------------------------

	private void setElem(Element elem) {
		if (elem == null) {
			throw new NullPointerException();
		}    // end of if (elem == null)

		this.elem = elem;

		if (elem.getAttribute("type") != null) {
			type = StanzaType.valueof(elem.getAttribute("type"));
		} else {
			type = null;
		}    // end of if (elem.getAttribute("type") != null) else

		if (elem.getName() == "cluster") {
			setPriority(Priority.CLUSTER);
		} else {
			if ((elem.getName() == "presence")
					&& ((type == null) || (type == StanzaType.available) || (type == StanzaType.unavailable)
						|| (type == StanzaType.probe))) {
				setPriority(Priority.PRESENCE);
			} else {
				if (elem.getName() == "route") {
					routed = true;
				} else {
					routed = false;
				}    // end of if (elem.getName().equals("route")) else
			}
		}
	}
}


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
