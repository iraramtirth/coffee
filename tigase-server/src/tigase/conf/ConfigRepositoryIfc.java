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

package tigase.conf;

import java.util.Map;
import java.util.Set;

import tigase.db.comp.ComponentRepository;

/**
 * Created: Dec 10, 2009 2:04:20 PM
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public interface ConfigRepositoryIfc extends ComponentRepository<ConfigItem> {

	public static final String RESOURCE_URI = "--resource-uri";
	public static final String RELOAD_DELAY = "--reload-delay";

	/**
	 * Initializes the configuration repository.
	 * @param params
	 * @throws ConfigurationException
	 */
	void init(Map<String, Object> params) throws ConfigurationException;

	/**
	 * Returns all known settings for the given component name.
	 * @param compName
	 * @return
	 * @throws ConfigurationException
	 */
	Map<String, Object> getProperties(String compName) throws ConfigurationException;

	Set<ConfigItem> getItemsForComponent(String compName);

//	/**
//	 * Returns all known settings for the given component name. And all property values
//	 * are converted to their string representation.
//	 * @param compName
//	 * @return
//	 * @throws ConfigurationException
//	 */
//	Map<String, String> getPropertiesAsStrings(String compName) throws ConfigurationException;

	/**
	 * Sets/adds properties for the given component name.
	 * @param compName
	 * @param props
	 * @throws ConfigurationException
	 */
	void putProperties(String compName, Map<String, Object> props)
			throws ConfigurationException;

//	/**
//	 * Sets/adds properties for the given component name.
//	 * @param compName
//	 * @param props
//	 * @throws ConfigurationException
//	 */
//	void putPropertiesFromStrings(String compName, Map<String, String> props)
//			throws ConfigurationException;

	/**
	 * Returns a configuration setting for a given component, node and key. If the
	 * configuration parameters is not found, returns given default value.
	 * @param compName
	 * @param node
	 * @param key
	 * @param def
	 * @return
	 */
	Object get(String compName, String node, String key, Object def);

	/**
	 * Puts/sets/adds/updates a configuration setting to the configuration repository.
	 * @param compName
	 * @param node
	 * @param key
	 * @param value
	 */
	void set(String compName, String node, String key, Object value);

	/**
	 * Returns all component names for which there are some configuration settings
	 * available.
	 * @return
	 */
	String[] getCompNames();

	/**
	 * Returns an array of all configuration keys for a given component and configuration
	 * node.
	 * @param compName
	 * @param node
	 * @return
	 */
	String[] getKeys(String compName, String node);

	/**
	 * Removes a configuration setting from the configuration repository.
	 * @param compName
	 * @param node
	 * @param key
	 */
	void remove(String compName, String node, String key);

	/**
	 * Method adds an Item to the configuration repository where the key is
	 * the item key constructed of component name, node name and property key name.
	 * @param key
	 * @param value
	 * @throws ConfigurationException
	 */
	void addItem(String key, Object value) throws ConfigurationException;

	/**
	 * This is used to load a configuration for a selected cluster node. The configuration
	 * repository (file or database) may contain settings for all cluster nodes, some
	 * of the settings may be exclusive to one or another cluster node. This method
	 * informs the repository what node name (hostname) it is running on.
	 * @param hostname
	 */
	void setDefHostname(String hostname);

	Map<String, Object> getInitProperties();

}
