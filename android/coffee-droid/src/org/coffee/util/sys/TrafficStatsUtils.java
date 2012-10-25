package org.coffee.util.sys;

import org.coffee.util.Exe;
import org.coffee.util.lang.Reader;

import android.os.Build;
import android.os.Process;

/**
 * 获取流量
 * 
 * @author coffee
 */
public class TrafficStatsUtils {

	private enum Tx_Rx {
		TX, RX
	};

	// private enum Tcp_Udp {
	// TCP, UDP, TCP_AND_UDP
	// };

	// /////////////////////////////

	private static int currentSdk = -1;
	private static final String TrafficStats_Class = "android.net.TrafficStats";
	static {
		currentSdk = Build.VERSION.SDK_INT;
	}

	/**
	 * 获取流量: 主要是调android.net.TrafficStats类里面执行的方法
	 * 
	 * @param methodName
	 * @param args
	 * @return
	 */
	private static long getTraffisStats(String methodName, Object... args) {
		Object result = Exe.run(TrafficStats_Class, methodName, args);
		try {
			return Long.valueOf(result + "");
		} catch (Exception e) {
			return 0;
		}
	}

	// //////**********************************************

	/**
	 * 
	 * @param uidOrPid
	 *            2.2以后的传入uid {@link android.os.Process#myUid()}; <br/>
	 *            2.1之前传入 pid {@link android.os.Process#myPid()}
	 * @return
	 */
	public static long getUidRxBytes(int... uidOrPid) {
		// 默认值：如果不指定参数， 默认获取当前app的
		if (uidOrPid.length == 0) {
			uidOrPid = new int[1];
		}
		if (currentSdk >= 8) {
			uidOrPid[0] = Process.myUid();
			return getTraffisStats("getUidRxBytes", uidOrPid[0]);
		} else {
			uidOrPid[0] = Process.myPid();
			return getUidBytes(uidOrPid[0], Tx_Rx.RX);
		}
	}

	/**
	 * 
	 * @param uidOrPid
	 *            2.2以后的传入uid {@link android.os.Process#myUid()}; <br/>
	 *            2.1之前传入 pid {@link android.os.Process#myPid()}
	 * @return
	 */
	public static long getUidTxBytes(int... uidOrPid) {
		// 默认值：如果不指定参数， 默认获取当前app的
		if (uidOrPid.length == 0) {
			uidOrPid = new int[1];
		}
		if (currentSdk >= 8) {
			uidOrPid[0] = Process.myUid();
			return getTraffisStats("getUidTxBytes", uidOrPid[0]);
		} else {
			uidOrPid[0] = Process.myPid();
			return getUidBytes(uidOrPid[0], Tx_Rx.TX);
		}
	}

	static long getUidBytes(int pid, Tx_Rx tx_rx) {
		String filePath = "/proc/" + pid + "/net/dev";
		String data = new Reader(filePath).readAll();

		// rmnet0: 54682 330 0 0 0 0 0 0 5002 541 0 0 0 0 0 0
		String rmnet = data.substring(data.indexOf("rmnet0"),
				data.indexOf("rmnet1")).trim();
		// tiwlan0:14163 4438 0 0 0 0 0 0 1151 588 9 0 0 0 0 0
		String tiwlan = data.substring(data.indexOf("wlan0")).trim();

		long bytes = 0;
		// 接收
		if (tx_rx == Tx_Rx.RX) {
			bytes += Long.valueOf(rmnet.split("\\s+")[1]);
			bytes += Long.valueOf(tiwlan.split("\\s+")[1]);
		} else {
			bytes += Long.valueOf(rmnet.split("\\s+")[9]);
			bytes += Long.valueOf(tiwlan.split("\\s+")[9]);
		}
		return bytes;
	}
}
