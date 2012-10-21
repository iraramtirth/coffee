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

package tigase.stats;

//~--- non-JDK imports --------------------------------------------------------

import tigase.sys.TigaseRuntime;

import tigase.util.AllHistoryCache;
import tigase.util.FloatHistoryCache;
import tigase.util.IntHistoryCache;

//~--- JDK imports ------------------------------------------------------------

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.StandardMBean;

//~--- classes ----------------------------------------------------------------

/**
 * Class StatisticsProvider
 * 
 * @author kobit
 */
public class StatisticsProvider extends StandardMBean implements StatisticsProviderMBean {
	private static final Logger log = Logger.getLogger(StatisticsProvider.class.getName());

	// ~--- fields ---------------------------------------------------------------

	private StatisticsCache cache = null;
	private StatisticsCollector theRef;

	// ~--- constructors ---------------------------------------------------------

	/**
	 * Constructs ...
	 * 
	 * 
	 * @param theRef
	 * 
	 * @throws NotCompliantMBeanException
	 */
	public StatisticsProvider(StatisticsCollector theRef, int historySize,
			long updateInterval) throws NotCompliantMBeanException {

		// WARNING Uncomment the following call to super() to make this class
		// compile (see BUG ID 122377)
		super(StatisticsProviderMBean.class, false);
		this.theRef = theRef;
		cache = new StatisticsCache(historySize, updateInterval);
	}

	// ~--- get methods ----------------------------------------------------------

	/**
	 * Operation exposed for management
	 * 
	 * @param level
	 * @return java.util.Map<String, String>
	 */
	@Override
	public Map<String, String> getAllStats(int level) {
		StatisticsList list = new StatisticsList(Level.parse("" + level));

		theRef.getAllStats(list);

		return getMapFromList(list);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int getCLIOQueueSize() {
		return cache.clIOQueue;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float[] getCLPacketsPerSecHistory() {
		return cache.clpacks_history != null ? cache.clpacks_history.getCurrentHistory()
				: null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int getCLQueueSize() {
		return cache.clQueue;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getCPUUsage() {
		return cache.cpuUsage;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float[] getCPUUsageHistory() {
		return cache.cpu_usage_history != null ? cache.cpu_usage_history.getCurrentHistory()
				: null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int getCPUsNumber() {
		return TigaseRuntime.getTigaseRuntime().getCPUsNumber();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int getClusterCacheSize() {

		// cache.updateIfOlder(1000);
		return cache.clusterCache;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getClusterCompressionRatio() {
		return cache.clusterCompressionRatio;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getClusterNetworkBytes() {
		return cache.clusterNetworkBytes;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getClusterNetworkBytesPerSecond() {
		return cache.clusterNetworkBytesPerSecond;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getClusterPackets() {

		// cache.updateIfOlder(1000);
		return cache.clusterPackets;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getClusterPacketsPerSec() {
		return cache.clusterPacketsPerSec;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param comp
	 * 
	 * @return
	 */
	public int getCompConnections(String comp) {
		return cache.allStats.getCompConnections(comp);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param comp
	 * 
	 * @return
	 */
	public long getCompIqs(String comp) {
		return cache.allStats.getCompIq(comp);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param comp
	 * 
	 * @return
	 */
	public long getCompMessages(String comp) {
		return cache.allStats.getCompMsg(comp);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param comp
	 * 
	 * @return
	 */
	public long getCompPackets(String comp) {
		return cache.allStats.getCompPackets(comp);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param comp
	 * 
	 * @return
	 */
	public long getCompPresences(String comp) {
		return cache.allStats.getCompPres(comp);
	}

	/**
	 * Operation exposed for management
	 * 
	 * @param compName
	 * @param level
	 * @return java.util.Map<String, String>
	 */
	@Override
	public Map<String, String> getComponentStats(String compName, int level) {
		StatisticsList list = new StatisticsList(Level.parse("" + level));

		theRef.getComponentStats(compName, list);

		return getMapFromList(list);
	}

	// /**
	// * Get Attribute exposed for management
	// * @return java.util.Map<String, String>
	// */
	// @Override
	// public Map getAllStats() {
	// return getAllStats(0);
	// }

	/**
	 * Get Attribute exposed for management
	 * 
	 * @return
	 */
	@Override
	public List<String> getComponentsNames() {
		return theRef.getComponentsNames();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int getConnectionsNumber() {

		// cache.updateIfOlder(1000);
		return cache.clientConnections;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int[] getConnectionsNumberHistory() {
		return cache.conns_history != null ? cache.conns_history.getCurrentHistory() : null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getHeapMemUsage() {
		return TigaseRuntime.getTigaseRuntime().getHeapMemUsage();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float[] getHeapUsageHistory() {
		return cache.heap_usage_history != null ? cache.heap_usage_history
				.getCurrentHistory() : null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getIQAuthNumber() {

		// cache.updateIfOlder(1000);
		return cache.iqAuthNumber;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getIQOtherNumber() {

		// cache.updateIfOlder(1000);
		return cache.iqOtherNumber;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getIQOtherNumberPerSec() {
		return cache.iqOtherNumberPerSec;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public MBeanInfo getMBeanInfo() {
		MBeanInfo mbinfo = super.getMBeanInfo();

		return new MBeanInfo(mbinfo.getClassName(), mbinfo.getDescription(),
				mbinfo.getAttributes(), mbinfo.getConstructors(), mbinfo.getOperations(),
				getNotificationInfo());
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getMessagesNumber() {

		// cache.updateIfOlder(1000);
		return cache.messagesNumber;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getMessagesNumberPerSec() {
		return cache.messagesPerSec;
	}

	/**
	 * Get Attribute exposed for management
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return theRef.getName();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getNonHeapMemUsage() {
		return TigaseRuntime.getTigaseRuntime().getNonHeapMemUsage();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public MBeanNotificationInfo[] getNotificationInfo() {
		return new MBeanNotificationInfo[] {};
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getPresencesNumber() {

		// cache.updateIfOlder(1000);
		return cache.presencesNumber;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getPresencesNumberPerSec() {
		return cache.presencesPerSec;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getProcesCPUTime() {
		return TigaseRuntime.getTigaseRuntime().getProcessCPUTime();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getQueueOverflow() {

		// cache.updateIfOlder(1000);
		return cache.queueOverflow;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int getQueueSize() {

		// cache.updateIfOlder(1000);
		return cache.queueSize;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public long getRegistered() {
		return cache.registered;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getSMPacketsNumber() {
		return cache.smPackets;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float getSMPacketsNumberPerSec() {
		return cache.smPacketsPerSec;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public float[] getSMPacketsPerSecHistory() {
		return cache.smpacks_history != null ? cache.smpacks_history.getCurrentHistory()
				: null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int getSMQueueSize() {
		return cache.smQueue;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int getServerConnections() {

		// cache.updateIfOlder(1000);
		return cache.serverConnections;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public int[] getServerConnectionsHistory() {
		return cache.server_conns_history != null ? cache.server_conns_history
				.getCurrentHistory() : null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param cmp_name
	 * @param stat
	 * @param def
	 * 
	 * @return
	 */
	public long getStats(String cmp_name, String stat, long def) {
		return cache.allStats.getValue(cmp_name, stat, def);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param cmp_name
	 * @param stat
	 * @param def
	 * 
	 * @return
	 */
	public float getStats(String cmp_name, String stat, float def) {
		return cache.allStats.getValue(cmp_name, stat, def);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param cmp_name
	 * @param stat
	 * @param def
	 * 
	 * @return
	 */
	public String getStats(String cmp_name, String stat, String def) {
		return cache.allStats.getValue(cmp_name, stat, def);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param cmp_name
	 * @param stat
	 * @param def
	 * 
	 * @return
	 */
	public int getStats(String cmp_name, String stat, int def) {
		return cache.allStats.getValue(cmp_name, stat, def);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public String getSystemDetails() {

		// cache.updateIfOlder(1000);
		return cache.systemDetails;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public long getUptime() {
		return TigaseRuntime.getTigaseRuntime().getUptime();
	}

	/**
	 * Override customization hook: You can supply a customized description for
	 * MBeanInfo.getDescription()
	 * 
	 * @param info
	 * @return
	 */
	@Override
	protected String getDescription(MBeanInfo info) {
		return "Provides the Tigase server statistics";
	}

	/**
	 * Override customization hook: You can supply a customized description for
	 * MBeanAttributeInfo.getDescription()
	 * 
	 * @param info
	 * @return
	 */
	@Override
	protected String getDescription(MBeanAttributeInfo info) {
		String description = null;

		if (info.getName().equals("AllStats")) {
			description = "Collection of statistics from all components.";
		} else {
			if (info.getName().equals("ComponentsNames")) {
				description = "List of components names for which statistics are available";
			} else {
				if (info.getName().equals("Name")) {
					description =
							"This is a component name - name of the statistics collector component,";
				} else {
					if (info.getName().equals("getUptime")) {
						description = "Returns JVM uptime.";
					} else {
						if (info.getName().equals("getProcesCPUTime")) {
							description = "Returns JMV process CPU time.";
						}
					}
				}
			}
		}

		return description;
	}

	/**
	 * Override customization hook: You can supply a customized description for
	 * MBeanParameterInfo.getDescription()
	 * 
	 * @param op
	 * @param param
	 * @param sequence
	 * @return
	 */
	@Override
	protected String getDescription(MBeanOperationInfo op, MBeanParameterInfo param,
			int sequence) {
		if (op.getName().equals("getAllStats")) {
			switch (sequence) {
				case 0:
					return "Statistics level, 0 - All, 500 - Medium, 800 - Minimal";

				default:
					return null;
			}
		} else {
			if (op.getName().equals("getComponentStats")) {
				switch (sequence) {
					case 0:
						return "The component name to provide statistics for";

					case 1:
						return "Statistics level, 0 - All, 500 - Medium, 800 - Minimal";

					default:
						return null;
				}
			}
		}

		return null;
	}

	/**
	 * Override customization hook: You can supply a customized description for
	 * MBeanOperationInfo.getDescription()
	 * 
	 * @param info
	 * @return
	 */
	@Override
	protected String getDescription(MBeanOperationInfo info) {
		String description = null;
		MBeanParameterInfo[] params = info.getSignature();
		String[] signature = new String[params.length];

		for (int i = 0; i < params.length; i++) {
			signature[i] = params[i].getType();
		}

		String[] methodSignature;

		methodSignature = new String[] { java.lang.Integer.TYPE.getName() };

		if (info.getName().equals("getAllStats") && Arrays.equals(signature, methodSignature)) {
			description = "Provides statistics for all components for a given level.";
		}

		methodSignature =
				new String[] { java.lang.String.class.getName(), java.lang.Integer.TYPE.getName() };

		if (info.getName().equals("getComponentStats")
				&& Arrays.equals(signature, methodSignature)) {
			description =
					"Provides statistics for a given component name and statistics level.";
		}

		return description;
	}

	/**
	 * Override customization hook: You can supply a customized description for
	 * MBeanParameterInfo.getName()
	 * 
	 * @param op
	 * @param param
	 * @param sequence
	 * @return
	 */
	@Override
	protected String getParameterName(MBeanOperationInfo op, MBeanParameterInfo param,
			int sequence) {
		if (op.getName().equals("getAllStats")) {
			switch (sequence) {
				case 0:
					return "level";

				default:
					return null;
			}
		} else {
			if (op.getName().equals("getComponentStats")) {
				switch (sequence) {
					case 0:
						return "compName";

					case 1:
						return "level";

					default:
						return null;
				}
			}
		}

		return null;
	}

	private Map<String, String> getMapFromList(StatisticsList list) {
		if (list != null) {
			Map<String, String> result = new LinkedHashMap<String, String>(300);

			for (StatRecord rec : list) {
				String key = rec.getComponent() + "/" + rec.getDescription();
				String value = rec.getValue();

				if (rec.getType() == StatisticType.LIST) {
					value = rec.getListValue().toString();
				}

				result.put(key, value);
			}

			return result;
		} else {
			return null;
		}
	}

	public Map<String, LinkedList<Object>> getStatsHistory(String[] statsKeys) {
		log.log(Level.INFO, "Generating history for metrics: {0}", Arrays.toString(statsKeys));
		Map<String, LinkedList<Object>> result = null;
		if (cache.allHistory != null) {
			result = new LinkedHashMap<String, LinkedList<Object>>();
			for (StatisticsList stHist : cache.allHistory.getCurrentHistory()) {
				for (String key : statsKeys) {
					LinkedList<Object> statsForKey = result.get(key);
					if (statsForKey == null) {
						statsForKey = new LinkedList<Object>();
						result.put(key, statsForKey);
					}
					statsForKey.add(stHist.getValue(key));
				}
			}
		} else {
			log.log(Level.INFO, "The server does not keep metrics history.");
		}
		// log.log(Level.INFO, "History generated: {0}", result);
		return result;
	}

	public Map<String, Object> getCurStats(String[] statsKeys) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		for (String key : statsKeys) {
			result.put(key, cache.allStats.getValue(key));
		}
		return result;
	}

	// ~--- inner classes --------------------------------------------------------

	private class StatisticsCache {
		private static final String BOSH_COMP = "bosh";
		private static final String C2S_COMP = "c2s";
		private static final String S2S_COMP = "s2s";
		private static final String CL_COMP = "cl-comp";
		// private static final int HISTORY_SIZE = 8640;
		private static final int HISTORY_SIZE = 30;
		private static final long SECOND = 1000;
		private static final String SM_COMP = "sess-man";
		private static final long MINUTE = 60 * SECOND;
		private static final long HOUR = 60 * MINUTE;

		// ~--- fields -------------------------------------------------------------

		private int clIOQueue = 0;
		private int clQueue = 0;
		private int clientConnections = 0;
		private int clusterCache = 0;
		private float clusterCompressionRatio = 0f;
		private long clusterNetworkBytes = 0L;
		private float clusterNetworkBytesPerSecond = 0L;
		private long clusterNetworkBytesReceived = 0L;
		private long clusterNetworkBytesSent = 0L;
		private long clusterPackets = 0L;
		private float clusterPacketsPerSec = 0;
		private long clusterPacketsReceived = 0L;
		private long clusterPacketsSent = 0L;
		private int cnt = 0;
		private float cpuUsage = 0f;
		private int inter = 10;
		private long iqAuthNumber = 0;
		private long iqOtherNumber = 0;
		private float iqOtherNumberPerSec = 0;
		private String largeQueues = "";
		private long lastPresencesReceived = 0;
		private long lastPresencesSent = 0;
		private long messagesNumber = 0;
		private float messagesPerSec = 0;
		private long presencesNumber = 0;
		private float presencesPerSec = 0;
		private long presences_received_per_update = 0;
		private long presences_sent_per_update = 0;
		private long prevClusterNetworkBytes = 0L;
		private float prevClusterNetworkBytesPerSecond = 0L;
		private long prevClusterPackets = 0L;
		private float prevClusterPacketsPerSec = 0;
		private float prevCpuUsage = 0f;
		private float prevIqOtherNumberPerSec = 0;
		private long prevMessagesNumber = 0;
		private float prevMessagesPerSec = 0;
		private long prevPresencesNumber = 0;
		private float prevPresencesPerSec = 0;
		private long prevSmPackets = 0;
		private float prevSmPacketsPerSec = 0;
		private long queueOverflow = 0;
		private int queueSize = 0;
		private long registered = 0;
		private int runs_counter = 100;
		private int serverConnections = 0;
		private long smPackets = 0;
		private float smPacketsPerSec = 0;
		private int smQueue = 0;
		private String systemDetails = "";
		private Timer updateTimer = null;
		private FloatHistoryCache smpacks_history = null;
		private IntHistoryCache server_conns_history = null;
		private FloatHistoryCache heap_usage_history = null;
		private FloatHistoryCache cpu_usage_history = null;
		private IntHistoryCache conns_history = null;
		private FloatHistoryCache clpacks_history = null;
		private AllHistoryCache allHistory = null;

		// private long lastUpdate = 0;
		private StatisticsList allStats = new StatisticsList(Level.FINER);

		// ~--- constructors -------------------------------------------------------

		private StatisticsCache(int historySize, long cacheUpdate) {
			if (historySize > 0) {
				smpacks_history = new FloatHistoryCache(historySize);
				server_conns_history = new IntHistoryCache(historySize);
				heap_usage_history = new FloatHistoryCache(historySize);
				cpu_usage_history = new FloatHistoryCache(historySize);
				conns_history = new IntHistoryCache(historySize);
				clpacks_history = new FloatHistoryCache(historySize);
				allHistory = new AllHistoryCache(historySize);
			}

			updateTimer = new Timer("stats-cache", true);
			updateTimer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					try {
						update();
						updateSystemDetails();
						theRef.statsUpdated();
					} catch (Exception e) {
						log.log(Level.WARNING, "Problem retrieving statistics: ", e);
					}
				}
			}, 10 * 1000, cacheUpdate * 1000);
		}

		private void update() {
			float temp = cpuUsage;

			cpuUsage =
					(prevCpuUsage + (temp * 2) + TigaseRuntime.getTigaseRuntime().getCPUUsage()) / 4;
			if (cpu_usage_history != null) {
				cpu_usage_history.addItem(cpuUsage);
			}
			prevCpuUsage = temp;
			if (heap_usage_history != null) {
				heap_usage_history.addItem(getHeapMemUsage());
			}

			if (++runs_counter >= 100) {
				allStats = new StatisticsList(Level.FINEST);
				runs_counter = 0;
			} else {
				allStats = new StatisticsList(Level.FINER);
			}

			theRef.getAllStats(allStats);
			if (allHistory != null) {
				allHistory.addItem(allStats);
			}

			long tmp_reg = allStats.getValue(SM_COMP, "Registered accounts", -1L);

			if (tmp_reg > 0) {
				registered = tmp_reg;
			}

			// System.out.println(allStats.toString());
			clusterCompressionRatio =
					(allStats.getValue(CL_COMP, "Average compression ratio", -1f) + allStats
							.getValue(CL_COMP, "Average decompression ratio", -1f)) / 2f;
			clusterPacketsReceived = allStats.getCompReceivedPackets(CL_COMP);
			clusterPacketsSent = allStats.getCompSentPackets(CL_COMP);
			clusterPackets = clusterPacketsSent + clusterPacketsReceived;
			temp = clusterPacketsPerSec;
			clusterPacketsPerSec =
					(prevClusterPacketsPerSec + (temp * 2f) + (clusterPackets - prevClusterPackets)) / 4f;
			if (clpacks_history != null) {
				clpacks_history.addItem(clusterPacketsPerSec);
			}
			prevClusterPacketsPerSec = temp;
			prevClusterPackets = clusterPackets;
			smPackets = allStats.getCompPackets(SM_COMP);
			temp = smPacketsPerSec;
			smPacketsPerSec =
					(prevSmPacketsPerSec + (temp * 2f) + (smPackets - prevSmPackets)) / 4f;
			if (smpacks_history != null) {
				smpacks_history.addItem(smPacketsPerSec);
			}
			prevSmPacketsPerSec = temp;
			prevSmPackets = smPackets;
			clientConnections =
					allStats.getCompConnections(C2S_COMP) + allStats.getCompConnections(BOSH_COMP);
			if (conns_history != null) {
				conns_history.addItem(clientConnections);
			}
			serverConnections = allStats.getCompConnections(S2S_COMP);
			if (server_conns_history != null) {
				server_conns_history.addItem(serverConnections);
			}
			clIOQueue = allStats.getValue(CL_COMP, "Waiting to send", 0);
			clusterCache = allStats.getValue("cl-caching-strat", "Cached JIDs", 0);
			messagesNumber = allStats.getCompMsg(SM_COMP);
			temp = messagesPerSec;
			messagesPerSec =
					(prevMessagesPerSec + (temp * 2f) + (messagesNumber - prevMessagesNumber)) / 4f;
			prevMessagesPerSec = temp;
			prevMessagesNumber = messagesNumber;
			clusterNetworkBytesSent = allStats.getValue(CL_COMP, "Bytes sent", 0L);
			clusterNetworkBytesReceived = allStats.getValue(CL_COMP, "Bytes received", 0L);
			clusterNetworkBytes = clusterNetworkBytesSent + clusterNetworkBytesReceived;
			temp = clusterNetworkBytesPerSecond;
			clusterNetworkBytesPerSecond =
					(prevClusterNetworkBytesPerSecond + (temp * 2f) + (clusterNetworkBytes - prevClusterNetworkBytes)) / 4f;
			prevClusterNetworkBytesPerSecond = temp;
			prevClusterNetworkBytes = clusterNetworkBytes;

			long currPresencesReceived = allStats.getCompPresReceived(SM_COMP);
			long currPresencesSent = allStats.getCompPresSent(SM_COMP);

			presencesNumber = currPresencesReceived + currPresencesSent;
			temp = presencesPerSec;
			presencesPerSec =
					(prevPresencesPerSec + (temp * 2f) + (presencesNumber - prevPresencesNumber)) / 4f;
			prevPresencesPerSec = temp;
			prevPresencesNumber = presencesNumber;

			if (++cnt >= inter) {
				presences_sent_per_update = (currPresencesSent - lastPresencesSent) / 10;
				presences_received_per_update =
						(currPresencesReceived - lastPresencesReceived) / 10;
				lastPresencesSent = currPresencesSent;
				lastPresencesReceived = currPresencesReceived;
				cnt = 0;
			}

			queueSize = 0;
			queueOverflow = 0;
			smQueue = 0;
			clQueue = 0;
			largeQueues = "";

			for (StatRecord rec : allStats) {
				if ((rec.getDescription() == StatisticType.IN_QUEUE_OVERFLOW.getDescription())
						|| (rec.getDescription() == StatisticType.OUT_QUEUE_OVERFLOW.getDescription())) {
					queueOverflow += rec.getLongValue();
				}

				if ((rec.getDescription() == "Total In queues wait")
						|| (rec.getDescription() == "Total Out queues wait")) {
					queueSize += rec.getIntValue();

					if (rec.getComponent().equals(SM_COMP)) {
						smQueue += rec.getIntValue();
					}

					if (rec.getComponent().equals(CL_COMP)) {
						clQueue += rec.getIntValue();
					}

					if (rec.getIntValue() > 10000) {
						largeQueues +=
								rec.getComponent() + " - queue size: " + rec.getIntValue() + "\n";
					}
				}
			}

			// System.out.println("clusterPackets: " + clusterPackets +
			// ", smPackets: " + smPackets +
			// ", clientConnections: " + clientConnections);
		}

		private void updateSystemDetails() {
			StringBuilder sb = new StringBuilder();

			sb.append("Uptime: ").append(TigaseRuntime.getTigaseRuntime().getUptimeString());

			int cpu_temp = allStats.getValue("cpu-mon", "CPU temp", 0);

			if (cpu_temp > 0) {
				sb.append(",      Temp: ").append(cpu_temp).append(" C");
			}

			String cpu_freq = allStats.getValue("cpu-mon", "CPU freq", null);

			if (cpu_freq != null) {
				sb.append("\nFreq: ").append(cpu_freq);
			}

			String cpu_throt = allStats.getValue("cpu-mon", "CPU throt", null);

			if (cpu_throt != null) {
				sb.append("\nThrott: ").append(cpu_throt);
			}

			sb.append("\nTop threads:");

			String cpu_thread = allStats.getValue("cpu-mon", "1st max CPU thread", null);

			if (cpu_thread != null) {
				sb.append("\n   ").append(cpu_thread);
			}

			cpu_thread = allStats.getValue("cpu-mon", "2nd max CPU thread", null);

			if (cpu_thread != null) {
				sb.append("\n   ").append(cpu_thread);
			}

			cpu_thread = allStats.getValue("cpu-mon", "3rd max CPU thread", null);

			if (cpu_thread != null) {
				sb.append("\n   ").append(cpu_thread);
			}

			cpu_thread = allStats.getValue("cpu-mon", "4th max CPU thread", null);

			if (cpu_thread != null) {
				sb.append("\n   ").append(cpu_thread);
			}

			cpu_thread = allStats.getValue("cpu-mon", "5th max CPU thread", null);

			if (cpu_thread != null) {
				sb.append("\n   ").append(cpu_thread);
			}

			LinkedHashMap<String, StatRecord> compStats = allStats.getCompStats(SM_COMP);

			if (compStats != null) {
				for (StatRecord rec : compStats.values()) {
					if (rec.getDescription().startsWith("Processor:")) {
						sb.append("\n").append(rec.getDescription()).append(rec.getValue());
					}
				}
			}

			sb.append("\nSM presences rec Tot: ").append(lastPresencesReceived);
			sb.append(" / ").append(presences_received_per_update).append(" last sec");
			sb.append("\nSM presences sent Tot: ").append(lastPresencesSent);
			sb.append(" / ").append(presences_sent_per_update).append(" last sec");
			sb.append("\nCluster bytes/sec: ").append(clusterNetworkBytesPerSecond);
			sb.append(", compress: ").append(clusterCompressionRatio);
			sb.append("\nCluster bytes: [S] ").append(clusterNetworkBytesSent);
			sb.append(" / [R] ").append(clusterNetworkBytesReceived);
			sb.append("\nCluster packets: [S] ").append(clusterPacketsSent);
			sb.append(" / [R] ").append(clusterPacketsReceived);

			if (!largeQueues.isEmpty()) {
				sb.append("\n").append(largeQueues);
			}

			systemDetails = sb.toString();
		}
	}
}