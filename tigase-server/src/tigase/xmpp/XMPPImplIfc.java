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

import tigase.db.TigaseDBException;

import tigase.stats.StatisticsList;
import tigase.xml.Element;

import java.util.Map;

/**
 * This is a base interface for all session manager plugins. There are packet
 * processing plugins, preprocesing, postprocessing and packet filters. They all
 * have a common basic methods which are defined here.
 * 
 * 
 * Created: Sat Oct 14 16:11:22 2006
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public interface XMPPImplIfc extends Comparable<XMPPImplIfc> {

	public static final String CLIENT_XMLNS = "jabber:client";

	int concurrentQueuesNo();

	@Deprecated
	int concurrentThreadsPerQueue();

	/**
	 * Method <code>id</code> returns a unique ID of the plugin. Each plugin has
	 * own, unique ID which is used in the configuration file to determine whether
	 * it needs to be loaded or not. In most cases the ID can be equal to XMLNS of
	 * the packages processed by the plugin.
	 * 
	 * @return a <code>String</code> value
	 */
	String id();

	/**
	 * Method <code>init</code> is called just after the plugin has been loaded
	 * into memory. The idea behind this is to allow it to initialize or check the
	 * database. This might be especially useful for plugins which want to have a
	 * database access via non-standard stored procedures or need schema upgrade.
	 * 
	 * @param settings
	 *          is a Map with initial processor settings from the configuration
	 *          file.
	 * @throws TigaseDBException
	 */
	void init(Map<String, Object> settings) throws TigaseDBException;

	// ~--- get methods ----------------------------------------------------------

	/**
	 * Method <code>isSupporting</code> takes element name and namespace for this
	 * element and determines whether this element can be processed by this
	 * plugin.
	 * 
	 * @param elem
	 *          a <code>String</code> value
	 * @param ns
	 *          a <code>String</code> value
	 * @return a <code>boolean</code> value
	 */
	boolean isSupporting(String elem, String ns);

	/**
	 * Method <code>supDiscoFeatures</code> returns an array of XML
	 * <code>Element</code>s with service discovery features which have to be
	 * returned to the client uppon request. Service discovery features returned
	 * by this method correspond to services supported by this plugin.
	 * 
	 * @param session
	 *          a <code>XMPPResourceConnection</code> value
	 * @return an <code>Element[]</code> value
	 */
	Element[] supDiscoFeatures(XMPPResourceConnection session);

	/**
	 * Method <code>supElements</code> returns an array of element names for
	 * stanzas which can be processed by this plugin. Each element name
	 * corresponds to XMLNS returned in array by <code>supNamespaces()</code>
	 * method.
	 * 
	 * @return a <code>String[]</code> value
	 */
	String[] supElements();

	/**
	 * Method <code>supNamespaces</code> returns an array of namespaces for
	 * stanzas which can be processed by this pluing. Each namespace corresponds
	 * to element name returned in array by <code>supElemenets()</code> method.
	 * 
	 * @return a <code>String[]</code> value
	 */
	String[] supNamespaces();

	/**
	 * Method <code>supStreamFeatures</code> returns an array of XML
	 * <code>Element</code>s with stream features which have to be returned to the
	 * client uppon request. Stream features returned by this method correspond to
	 * features supported by this plugin.
	 * 
	 * @param session
	 *          a <code>XMPPResourceConnection</code> value
	 * @return an <code>Element[]</code> value
	 */
	Element[] supStreamFeatures(XMPPResourceConnection session);

	/**
	 * The method allows to retrieve plugin own statistics if it generates any.
	 * @param list is a statistics collection to which plugins own metrics can be added.
	 */
	void getStatistics(StatisticsList list);
} // XMPPImplIfc

