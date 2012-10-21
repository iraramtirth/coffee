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

package tigase.server;

//~--- non-JDK imports --------------------------------------------------------

import tigase.conf.ConfiguratorAbstract;

import tigase.disco.ServiceEntity;
import tigase.disco.ServiceIdentity;
import tigase.disco.XMPPService;

import tigase.stats.StatisticsList;

import tigase.sys.TigaseRuntime;

import tigase.util.UpdatesChecker;

import tigase.xml.Element;

import tigase.xmpp.Authorization;
import tigase.xmpp.JID;
import tigase.xmpp.PacketErrorTypeException;
import tigase.xmpp.StanzaType;

import static tigase.server.MessageRouterConfig.*;

//~--- JDK imports ------------------------------------------------------------

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import java.util.ArrayDeque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- classes ----------------------------------------------------------------

/**
 * Class MessageRouter
 * 
 * 
 * Created: Tue Nov 22 07:07:11 2005
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class MessageRouter extends AbstractMessageReceiver implements MessageRouterIfc {

	// implements XMPPService {
	// public static final String INFO_XMLNS =
	// "http://jabber.org/protocol/disco#info";
	// public static final String ITEMS_XMLNS =
	// "http://jabber.org/protocol/disco#items";
	private static final Logger log = Logger.getLogger(MessageRouter.class.getName());

	// ~--- fields ---------------------------------------------------------------

	private ConfiguratorAbstract config = null;

	// private static final long startupTime = System.currentTimeMillis();
	// private Set<String> localAddresses = new CopyOnWriteArraySet<String>();
	private String disco_name = DISCO_NAME_PROP_VAL;
	private boolean disco_show_version = DISCO_SHOW_VERSION_PROP_VAL;
	private ServiceEntity serviceEntity = null;
	private UpdatesChecker updates_checker = null;
	private Map<String, XMPPService> xmppServices =
			new ConcurrentHashMap<String, XMPPService>();
	private Map<String, ComponentRegistrator> registrators =
			new ConcurrentHashMap<String, ComponentRegistrator>();
	private Map<String, MessageReceiver> receivers =
			new ConcurrentHashMap<String, MessageReceiver>();
	private boolean inProperties = false;
	private Map<JID, ServerComponent> components_byId =
			new ConcurrentHashMap<JID, ServerComponent>();
	private Map<String, ServerComponent> components =
			new ConcurrentHashMap<String, ServerComponent>();
	private Set<String> connectionManagerNames = new ConcurrentSkipListSet<String>();

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param component
	 */
	public void addComponent(ServerComponent component) {
		log.info("Adding component: " + component.getClass().getSimpleName());

		for (ComponentRegistrator registr : registrators.values()) {
			if (registr != component) {
				if (log.isLoggable(Level.FINER)) {
					log.finer("Adding: " + component.getName() + " component to "
							+ registr.getName() + " registrator.");
				}

				registr.addComponent(component);
			} // end of if (reg != component)
		} // end of for ()

		components.put(component.getName(), component);
		components_byId.put(component.getComponentId(), component);

		if (component instanceof XMPPService) {
			xmppServices.put(component.getName(), (XMPPService) component);
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param registr
	 */
	public void addRegistrator(ComponentRegistrator registr) {
		log.log(Level.INFO, "Adding registrator: {0}", registr.getClass().getSimpleName());
		registrators.put(registr.getName(), registr);
		addComponent(registr);

		for (ServerComponent comp : components.values()) {

			// if (comp != registr) {
			registr.addComponent(comp);

			// } // end of if (comp != registr)
		} // end of for (ServerComponent comp : components)
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param receiver
	 */
	public void addRouter(MessageReceiver receiver) {
		log.info("Adding receiver: " + receiver.getClass().getSimpleName());
		addComponent(receiver);
		receivers.put(receiver.getName(), receiver);
	}

	// ~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param params
	 * 
	 * @return
	 */
	@Override
	public Map<String, Object> getDefaults(Map<String, Object> params) {
		Map<String, Object> defs = super.getDefaults(params);

		MessageRouterConfig.getDefaults(defs, params, getName());

		return defs;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param node
	 * @param jid
	 * @param from
	 * 
	 * @return
	 */
	@Override
	public Element getDiscoInfo(String node, JID jid, JID from) {
		Element query = serviceEntity.getDiscoInfo(null);

		if (log.isLoggable(Level.FINEST)) {
			log.finest("Returing disco-info: " + query.toString());
		}

		return query;
	}

	// public List<Element> getDiscoItems(String node, String jid) {
	// return null;
	// }

	/**
	 * Method description
	 * 
	 * 
	 * @param list
	 */
	@Override
	public void getStatistics(StatisticsList list) {
		super.getStatistics(list);
		list.add(getName(), "Local hostname", getDefHostName().getDomain(), Level.INFO);

		TigaseRuntime runtime = TigaseRuntime.getTigaseRuntime();

		list.add(getName(), "Uptime", runtime.getUptimeString(), Level.INFO);

		NumberFormat format = NumberFormat.getNumberInstance();

		format.setMaximumFractionDigits(4);
		list.add(getName(), "Load average", format.format(runtime.getLoadAverage()),
				Level.FINE);
		list.add(getName(), "CPUs no", runtime.getCPUsNumber(), Level.FINEST);
		list.add(getName(), "Threads count", runtime.getThreadsNumber(), Level.FINEST);

		float cpuUsage = runtime.getCPUUsage();
		float heapUsage = runtime.getHeapMemUsage();
		float nonHeapUsage = runtime.getNonHeapMemUsage();
		list.add(getName(), "CPU usage [%]", cpuUsage, Level.FINE);
		list.add(getName(), "HEAP usage [%]", heapUsage, Level.FINE);
		list.add(getName(), "NONHEAP usage [%]", nonHeapUsage, Level.FINE);

		format = NumberFormat.getNumberInstance();
		format.setMaximumFractionDigits(1);

		// if (format instanceof DecimalFormat) {
		// DecimalFormat decf = (DecimalFormat)format;
		// decf.applyPattern(decf.toPattern()+"%");
		// }
		list.add(getName(), "CPU usage", format.format(cpuUsage) + "%", Level.INFO);

		MemoryUsage heap = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
		MemoryUsage nonHeap = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();

		format = NumberFormat.getIntegerInstance();

		if (format instanceof DecimalFormat) {
			DecimalFormat decf = (DecimalFormat) format;

			decf.applyPattern(decf.toPattern() + " KB");
		}

		list.add(getName(), "Max Heap mem", format.format(heap.getMax() / 1024), Level.INFO);
		list.add(getName(), "Used Heap", format.format(heap.getUsed() / 1024), Level.INFO);
		list.add(getName(), "Free Heap",
				format.format((heap.getMax() - heap.getUsed()) / 1024), Level.FINE);
		list.add(getName(), "Max NonHeap mem", format.format(nonHeap.getMax() / 1024),
				Level.FINE);
		list.add(getName(), "Used NonHeap", format.format(nonHeap.getUsed() / 1024),
				Level.FINE);
		list.add(getName(), "Free NonHeap",
				format.format((nonHeap.getMax() - nonHeap.getUsed()) / 1024), Level.FINE);
	}

	// ~--- methods --------------------------------------------------------------

	// private String isToLocalComponent(String jid) {
	// String nick = JIDUtils.getNodeNick(jid);
	// if (nick == null) {
	// return null;
	// }
	// String host = JIDUtils.getNodeHost(jid);
	// if (isLocalDomain(host) && components.get(nick) != null) {
	// return nick;
	// }
	// return null;
	// }
	// private boolean isLocalDomain(String domain) {
	// return localAddresses.contains(domain);
	// }

	/**
	 * Method description
	 * 
	 * 
	 * @param packet
	 */
	@Override
	public void processPacket(Packet packet) {

		// We do not process packets with not destination address
		// Just dropping them and writing a log message
		if (packet.getTo() == null) {
			log.warning("Packet with TO attribute set to NULL: " + packet);

			return;
		} // end of if (packet.getTo() == null)

		// Intentionally comparing to static, final String
		// This is kind of a hack to handle packets addressed specifically for the
		// SessionManager
		// TODO: Replace the hack with a proper solution
		// if (packet.getTo() == NULL_ROUTING) {
		// log.info("NULL routing, it is normal if server doesn't know how to"
		// + " process packet: " + packet.toStringSecure());
		//
		// try {
		// Packet error =
		// Authorization.FEATURE_NOT_IMPLEMENTED.getResponseMessage(packet,
		// "Feature not supported yet.", true);
		//
		// addOutPacketNB(error);
		// } catch (PacketErrorTypeException e) {
		// log.warning("Packet processing exception: " + e);
		// }
		//
		// return;
		// }
		// if (log.isLoggable(Level.FINER)) {
		// log.finer("Processing packet: " + packet.getElemName()
		// + ", type: " + packet.getType());
		// }
		if (log.isLoggable(Level.FINEST)) {
			log.finest("Processing packet: " + packet);
		}

		// Detect inifinite loop if from == to
		// Maybe it is not needed anymore...
		// There is a need to process packets with the same from and to address
		// let't try to relax restriction and block all packets with error type
		// 2008-06-16
		if (((packet.getType() == StanzaType.error) && (packet.getFrom() != null) && packet
				.getFrom().equals(packet.getTo()))) {
			if (log.isLoggable(Level.FINEST)) {
				log.finest("Possible infinite loop, dropping packet: " + packet);
			}

			return;
		}

		// Catch and process all service discovery packets here....
		ServerComponent comp =
				(packet.getStanzaTo() == null) ? null : getLocalComponent(packet.getStanzaTo());

		if (packet.isServiceDisco()
				&& (packet.getType() == StanzaType.get)
				&& (packet.getStanzaFrom() != null)
				&& (((comp != null) && !(comp instanceof DisableDisco)) || isLocalDomain(packet
						.getStanzaTo().toString()))) {
			Queue<Packet> results = new ArrayDeque<Packet>();

			processDiscoQuery(packet, results);

			if (results.size() > 0) {
				for (Packet res : results) {

					// No more recurrential calls!!
					addOutPacketNB(res);
				} // end of for ()
			}

			return;
		}

		// It it is not a service discovery packet, we have to find a component to
		// process
		// the packet. The below block of code is to "quickly" find a component if
		// the
		// the packet is addressed to the component ID where the component ID is
		// either
		// of one below:
		// 1. component name + "@" + default domain name
		// 2. component name + "@" + any virtual host name
		// 3. component name + "." + default domain name
		// 4. component name + "." + any virtual host name
		// TODO: check the efficiency for packets addressed to c2s component
		comp = getLocalComponent(packet.getTo());

		if (comp != null) {
			if (log.isLoggable(Level.FINEST)) {
				log.finest("1. Packet will be processed by: " + comp.getComponentId() + ", "
						+ packet);
			}

			Queue<Packet> results = new ArrayDeque<Packet>();

			if (comp == this) {

				// This is addressed to the MessageRouter itself. Has to be processed
				// separately to avoid recurential calls by the packet processing
				// method.
				processPacketMR(packet, results);
			} else {

				// All other components process the packet the same way.
				comp.processPacket(packet, results);
			}

			if (results.size() > 0) {
				for (Packet res : results) {

					// No more recurrential calls!!
					addOutPacketNB(res);

					// processPacket(res);
				} // end of for ()
			}

			// If the component is found the processing ends here as there can be
			// only one component with specific ID.
			return;
		}

		// This packet is not processed yet
		// The packet can be addressed to just a domain, one of the virtual hosts
		// The code below finds all components which handle packets addressed
		// to a virtual domains (implement VHostListener and return 'true' from
		// handlesLocalDomains() method call)
		String host = packet.getTo().getDomain();
		ServerComponent[] comps = getComponentsForLocalDomain(host);

		if (comps == null) {

			// Still no component found, now the most expensive lookup.
			// Checking regex routings provided by the component.
			comps = getServerComponentsForRegex(packet.getTo().getBareJID().toString());
		}

		if ((comps == null) && !isLocalDomain(host)) {

			// None of the component want to process the packet.
			// If the packet is addressed to non-local domain then it is processed by
			// all components dealing with external world, like s2s
			comps = getComponentsForNonLocalDomain(host);
		}

		// Ok, if any component has been found then process the packet in a standard
		// way
		if (comps != null) {

			// Processing packet and handling results out
			Queue<Packet> results = new ArrayDeque<Packet>();

			for (ServerComponent serverComponent : comps) {
				if (log.isLoggable(Level.FINEST)) {
					log.finest("2. Packet will be processed by: "
							+ serverComponent.getComponentId() + ", " + packet);
				}

				serverComponent.processPacket(packet, results);

				if (results.size() > 0) {
					for (Packet res : results) {

						// No more recurrential calls!!
						addOutPacketNB(res);

						// processPacket(res);
					} // end of for ()
				}
			}
		} else {

			// No components for the packet, sending an error back
			if (log.isLoggable(Level.FINEST)) {
				log.finest("There is no component for the packet, sending it back");
			}

			try {
				addOutPacketNB(Authorization.SERVICE_UNAVAILABLE.getResponseMessage(packet,
						"There is no service found to process your request.", true));
			} catch (PacketErrorTypeException e) {

				// This packet is to local domain, we don't want to send it out
				// drop packet :-(
				log.warning("Can't process packet to local domain, dropping..."
						+ packet.toStringSecure());
			}
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param packet
	 * @param results
	 */
	public void processPacketMR(Packet packet, Queue<Packet> results) {
		Iq iq = null;

		if (packet instanceof Iq) {
			iq = (Iq) packet;
		} else {

			// Not a command for sure...
			log.warning("I expect command (Iq) packet here, instead I got: "
					+ packet.toString());

			return;
		}

		if (packet.getPermissions() != Permissions.ADMIN) {
			try {
				Packet res =
						Authorization.NOT_AUTHORIZED.getResponseMessage(packet,
								"You are not authorized for this action.", true);

				results.offer(res);

				// processPacket(res);
			} catch (PacketErrorTypeException e) {
				log.warning("Packet processing exception: " + e);
			}

			return;
		}

		if (log.isLoggable(Level.FINEST)) {
			log.finest("Command received: " + iq.toString());
		}

		switch (iq.getCommand()) {
			case OTHER:
				if (iq.getStrCommand() != null) {
					if (iq.getStrCommand().startsWith("controll/")) {
						String[] spl = iq.getStrCommand().split("/");
						String cmd = spl[1];

						if (cmd.equals("stop")) {
							Packet result = iq.commandResult(Command.DataType.result);

							results.offer(result);

							// processPacket(result);
							new Timer("Stopping...", true).schedule(new TimerTask() {
								@Override
								public void run() {
									System.exit(0);
								}
							}, 2000);
						}
					}
				}

				break;

			default:
				break;
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int processingInThreads() {
		return Runtime.getRuntime().availableProcessors() * 4;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int processingOutThreads() {
		return 1;
	}

	// ~--- set methods ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param config
	 */
	@Override
	public void setConfig(ConfiguratorAbstract config) {
		components.put(getName(), this);
		this.config = config;
		addRegistrator(config);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param props
	 */
	@Override
	public void setProperties(Map<String, Object> props) {
		if (inProperties) {
			return;
		} else {
			inProperties = true;
		} // end of if (inProperties) else
		connectionManagerNames.add("c2s");
		connectionManagerNames.add("bosh");
		connectionManagerNames.add("s2s");

		if (props.get(DISCO_SHOW_VERSION_PROP_KEY) != null) {
			disco_show_version = (Boolean) props.get(DISCO_SHOW_VERSION_PROP_KEY);
		}
		if (props.get(DISCO_NAME_PROP_KEY) != null) {
			disco_name = (String) props.get(DISCO_NAME_PROP_KEY);
			serviceEntity = new ServiceEntity("Tigase", "server", "Session manager");
			serviceEntity.addIdentities(new ServiceIdentity[] { new ServiceIdentity("server",
					"im", disco_name
							+ (disco_show_version ? (" ver. " + tigase.server.XMPPServer
									.getImplementationVersion()) : "")) });
			serviceEntity.addFeatures(XMPPService.DEF_FEATURES);
		}

		try {
			super.setProperties(props);
			if (props.size() == 1) {
				// If props.size() == 1, it means this is a single property update and
				// MR does
				// not support it yet.
				return;
			}

			MessageRouterConfig conf = new MessageRouterConfig(props);
			String[] reg_names = conf.getRegistrNames();

			for (String name : reg_names) {

				// First remove it and later, add it again if still active
				ComponentRegistrator cr = registrators.remove(name);
				String cls_name = (String) props.get(REGISTRATOR_PROP_KEY + name + ".class");

				try {
					if ((cr == null) || !cr.getClass().getName().equals(cls_name)) {
						if (cr != null) {
							cr.release();
						}

						cr = conf.getRegistrInstance(name);
						cr.setName(name);
					} // end of if (cr == null)

					addRegistrator(cr);
				} catch (Exception e) {
					e.printStackTrace();
				} // end of try-catch
			} // end of for (String name: reg_names)

			String[] msgrcv_names = conf.getMsgRcvNames();

			for (String name : msgrcv_names) {
				if (log.isLoggable(Level.FINER)) {
					log.log(Level.FINER, "Loading and registering message receiver: {0}", name);
				}

				// First remove it and later, add it again if still active
				ServerComponent mr = receivers.remove(name);
				String cls_name = (String) props.get(MSG_RECEIVERS_PROP_KEY + name + ".class");

				try {
					if ((mr == null) || !mr.getClass().getName().equals(cls_name)) {
						if (mr != null) {
							mr.release();
						}

						mr = conf.getMsgRcvInstance(name);
						mr.setName(name);

						if (mr instanceof MessageReceiver) {
							((MessageReceiver) mr).setParent(this);
							((MessageReceiver) mr).start();
						}
					} // end of if (cr == null)

					if (mr instanceof MessageReceiver) {
						addRouter((MessageReceiver) mr);
					} else {
						addComponent(mr);
					}
				} // end of try
				catch (Exception e) {
					e.printStackTrace();
				} // end of try-catch
			} // end of for (String name: reg_names)

			// for (MessageReceiver mr : tmp_rec.values()) {
			// mr.release();
			// } // end of for ()
			//
			// tmp_rec.clear();
			if ((Boolean) props.get(UPDATES_CHECKING_PROP_KEY)) {
				installUpdatesChecker((Long) props.get(UPDATES_CHECKING_INTERVAL_PROP_KEY));
			} else {
				log.log(Level.INFO, "Disabling updates checker.");
				stopUpdatesChecker();
			}
		} finally {
			inProperties = false;
		} // end of try-finally

		for (ServerComponent comp : components.values()) {
			log.log(Level.INFO, "Initialization completed notification to: {0}", comp.getName());
			comp.initializationCompleted();
		}

		// log.info("Initialization completed notification to: " +
		// config.getName());
		// config.initializationCompleted();
	}

	// ~--- get methods ----------------------------------------------------------

	@Override
	protected Integer getMaxQueueSize(int def) {
		return def * 10;
	}

	private ServerComponent[] getComponentsForLocalDomain(String domain) {
		return vHostManager.getComponentsForLocalDomain(domain);
	}

	private ServerComponent[] getComponentsForNonLocalDomain(String domain) {
		return vHostManager.getComponentsForNonLocalDomain(domain);
	}

	private ServerComponent getLocalComponent(JID jid) {

		// Fast lookup in the server components to find a candidate
		// by the component ID (JID). If the packet is addressed directly
		// to the component ID then this is where the processing must happen.
		// Normally the component id is: component name + "@" + default hostname
		// However the component may "choose" to have any ID.
		ServerComponent comp = components_byId.get(jid);

		if (comp != null) {
			return comp;
		}

		// Note, component ID consists of the component name + default hostname
		// which can be different from a virtual host. There might be many
		// virtual hosts and the packet can be addressed to the component by
		// the component name + virtual host name
		// Code below, tries to find a destination by the component name + any
		// active virtual hostname.
		if (jid.getLocalpart() != null) {
			comp = components.get(jid.getLocalpart());

			if ((comp != null)
					&& (isLocalDomain(jid.getDomain()) || jid.getDomain().equals(
							getDefHostName().getDomain()))) {
				return comp;
			}
		}

		// Instead of a component ID built of: component name + "@" domain name
		// Some components have an ID of: component name + "." domain name
		// Code below tries to find a packet receiver if the address have the other
		// type of form.
		int idx = jid.getDomain().indexOf('.');

		if (idx > 0) {
			String cmpName = jid.getDomain().substring(0, idx);
			String basename = jid.getDomain().substring(idx + 1);

			comp = components.get(cmpName);

			if ((comp != null)
					&& (isLocalDomain(basename) || basename.equals(getDefHostName().getDomain()))) {
				return comp;
			}
		}

		return null;
	}

	@Override
	public int hashCodeForPacket(Packet packet) {
		// This is actually quite tricky part. We want to both avoid
		// packet reordering and also even packets distribution among
		// different threads.
		// If packet comes from a connection manager we must use packetFrom
		// address. However if the packet comes from SM, PubSub or other similar
		// component all packets would end-up in the same queue.
		// So, kind of a workaround here....
		// TODO: develop a proper solution discovering which components are
		// connection managers and use their names here instead of static names.
		if (packet.getPacketFrom() != null && packet.getPacketFrom().getLocalpart() != null) {
			if (connectionManagerNames.contains(packet.getPacketFrom().getLocalpart())) {
				return packet.getPacketFrom().hashCode();
			}
		}

		if (packet.getPacketTo() != null && packet.getPacketTo().getLocalpart() != null) {
			if (connectionManagerNames.contains(packet.getPacketTo().getLocalpart())) {
				return packet.getPacketTo().hashCode();
			}
		}

		if (packet.getStanzaTo() != null) {
			return packet.getStanzaTo().getBareJID().hashCode();
		}

		if ((packet.getPacketFrom() != null)
				&& !getComponentId().equals(packet.getPacketFrom())) {

			// This comes from connection manager so the best way is to get hashcode
			// by the connectionId, which is in the getFrom()
			return packet.getPacketFrom().hashCode();
		}

		if (packet.getPacketTo() != null && !getComponentId().equals(packet.getPacketTo())) {
			return packet.getPacketTo().hashCode();
		}

		// If not, then a better way is to get hashCode from the elemTo address
		// as this would be by the destination address user name:
		return 1;
	}

	private ServerComponent[] getServerComponentsForRegex(String id) {
		LinkedHashSet<ServerComponent> comps = new LinkedHashSet<ServerComponent>();

		for (MessageReceiver mr : receivers.values()) {
			if (log.isLoggable(Level.FINEST)) {
				log.finest("Checking routings for: " + mr.getName());
			}

			if (mr.isInRegexRoutings(id)) {
				comps.add(mr);
			}
		}

		if (comps.size() > 0) {
			return comps.toArray(new ServerComponent[comps.size()]);
		} else {
			return null;
		}
	}

	// ~--- methods --------------------------------------------------------------

	private void installUpdatesChecker(long interval) {
		stopUpdatesChecker();
		updates_checker =
				new UpdatesChecker(interval, this,
						"This is automated message generated by updates checking module.\n"
								+ " You can disable this function changing configuration option: " + "'/"
								+ getName() + "/" + UPDATES_CHECKING_PROP_KEY + "' or adjust"
								+ " updates checking interval time changing option: " + "'/" + getName()
								+ "/" + UPDATES_CHECKING_INTERVAL_PROP_KEY + "' which" + " now set to "
								+ interval + " days.");
		updates_checker.start();
	}

	private void processDiscoQuery(final Packet packet, final Queue<Packet> results) {
		if (log.isLoggable(Level.FINEST)) {
			log.log(Level.FINEST, "Processing disco query by: {0}", packet.toStringSecure());
		}

		JID toJid = packet.getStanzaTo();
		JID fromJid = packet.getStanzaFrom();
		String node = packet.getAttribute("/iq/query", "node");
		Element query = packet.getElement().getChild("query").clone();

		if (packet.isXMLNS("/iq/query", INFO_XMLNS)) {
			if (isLocalDomain(toJid.toString()) && (node == null)) {
				query = getDiscoInfo(node, toJid, fromJid);

				for (XMPPService comp : xmppServices.values()) {

					// Buggy custom component may throw exceptions here (NPE most likely)
					// which may cause service disco problems for well-behaving components
					// too
					// So this is kind of a protection
					try {
						List<Element> features = comp.getDiscoFeatures(fromJid);

						if (features != null) {
							query.addChildren(features);
						}
					} catch (Exception e) {
						log.log(Level.WARNING, "Component service disco problem: " + comp.getName(),
								e);
					}
				}
			} else {
				for (XMPPService comp : xmppServices.values()) {

					// Buggy custom component may throw exceptions here (NPE most likely)
					// which may cause service disco problems for well-behaving components
					// too
					// So this is kind of a protection
					try {

						// if (jid.startsWith(comp.getName() + ".")) {
						Element resp = comp.getDiscoInfo(node, toJid, fromJid);

						if (resp != null) {
							query.addChildren(resp.getChildren());
						}
					} catch (Exception e) {
						log.log(Level.WARNING, "Component service disco problem: " + comp.getName(),
								e);
					}

					// }
				}
			}
		}

		if (packet.isXMLNS("/iq/query", ITEMS_XMLNS)) {
			boolean localDomain = isLocalDomain(toJid.toString());

			if (localDomain) {
				for (XMPPService comp : xmppServices.values()) {

					// Buggy custom component may throw exceptions here (NPE most likely)
					// which may cause service disco problems for well-behaving components
					// too
					// So this is kind of a protection
					try {

						// if (localDomain || (nick != null && comp.getName().equals(nick)))
						// {
						List<Element> items = comp.getDiscoItems(node, toJid, fromJid);

						if (log.isLoggable(Level.FINEST)) {
							log.log(Level.FINEST,
									"Localdomain: {0}, DiscoItems processed by: {1}, items: {2}",
									new Object[] { toJid, comp.getComponentId(),
											(items == null) ? null : items.toString() });
						}

						if ((items != null) && (items.size() > 0)) {
							query.addChildren(items);
						}
					} catch (Exception e) {
						log.log(Level.WARNING, "Component service disco problem: " + comp.getName(),
								e);
					}
				} // end of for ()
			} else {
				ServerComponent comp = getLocalComponent(toJid);

				if ((comp != null) && (comp instanceof XMPPService)) {
					List<Element> items = ((XMPPService) comp).getDiscoItems(node, toJid, fromJid);

					if (log.isLoggable(Level.FINEST)) {
						log.log(
								Level.FINEST,
								"DiscoItems processed by: {0}, items: {1}",
								new Object[] { comp.getComponentId(),
										(items == null) ? null : items.toString() });
					}

					if ((items != null) && (items.size() > 0)) {
						query.addChildren(items);
					}
				}
			}
		}

		results.offer(packet.okResult(query, 0));
	}

	private void stopUpdatesChecker() {
		if (updates_checker != null) {
			updates_checker.interrupt();
			updates_checker = null;
		}
	}
}

// ~ Formatted in Sun Code Convention

// ~ Formatted by Jindent --- http://www.jindent.com
