package com.test;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class LiuLiangDemoActivity extends Activity {
	/** Called when the activity is first created. */
	static final String[] mobile_iface_list = new String[] { "rmnet0",
			"rmnet1", "rmnet2", "rmnet3", "cdma_rmnet4", "ppp0", };
	TextView[] mTextView = new TextView[20];
	int[] id;
//	int[] id = new int[] { R.id.txt1, R.id.txt2, R.id.txt3, R.id.txt4,
//			R.id.txt5, R.id.txt6, R.id.txt7, R.id.txt8, R.id.txt9, R.id.txt10,
//			R.id.txt11, R.id.txt12, R.id.txt13, R.id.txt14, R.id.txt15,
//			R.id.txt16, R.id.txt17, R.id.txt18, R.id.txt19, R.id.txt20 };
	Button btn;
	Button btnSelf;
	int uid = -1;

	enum Tx_Rx {
		TX, RX
	};

	enum Tcp_Udp {
		TCP, UDP, TCP_AND_UDP
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.main);
//		PackageManager pm = getPackageManager();
//		ApplicationInfo ai = null;
//		try {
//			ai = pm.getApplicationInfo("com.demo.shen.liuliang", 0);
//			uid = ai.uid;
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		for (int i = 0; i < 20; i++) {
//			mTextView[i] = (TextView) findViewById(id[i]);
//		}
//		//btn = (Button) findViewById(R.id.btn);
//		//btnSelf = (Button) findViewById(R.id.btn_self);
//		btnSelf.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				setLiuLiangByMyMethod();
//			}
//		});
//		btn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				setLiuLiangBySysMethod();
//			}
//		});
	}

	/**
	 * 2.2�汾��ǰ��ͨ���Լ�ѧ�ķ���ȡ������
	 * */
	private void setLiuLiangByMyMethod() {
		mTextView[10].setText("MobileRxBytes : " + getMobileRxBytes());
		mTextView[11].setText("MobileRxPackets : " + getMobileRxPackets());
		mTextView[12].setText("MobileTxBytes : " + getMobileTxBytes());
		mTextView[13].setText("MobileTxPackets : " + getMobileTxPackets());
		mTextView[14].setText("TotalRxBytes : " + getTotalRxBytes());
		mTextView[15].setText("TotalRxPackets : " + getTotalRxPackets());
		mTextView[16].setText("TotalTxBytes : " + getTotalTxBytes());
		mTextView[17].setText("TotalTxPackets : " + getTotalTxPackets());
		mTextView[18].setText("UidRxBytes : " + getUidRxBytes(uid));
		mTextView[19].setText("UidTxBytes : " + getUidTxBytes(uid));
	}

	
	/**
	 * 2.2�汾�Ժ���TrafficStatsֱ��ȡ������
	 * */
	private void setLiuLiangBySysMethod() {
//		mTextView[0].setText("MobileRxBytes : "
//				+ TrafficStats.getMobileRxBytes());
//		mTextView[1].setText("MobileRxPackets : "
//				+ TrafficStats.getMobileRxPackets());
//		mTextView[2].setText("MobileTxBytes : "
//				+ TrafficStats.getMobileTxBytes());
//		mTextView[3].setText("MobileTxPackets : "
//				+ TrafficStats.getMobileTxPackets());
//		mTextView[4]
//				.setText("TotalRxBytes : " + TrafficStats.getTotalRxBytes());
//		mTextView[5].setText("TotalRxPackets : "
//				+ TrafficStats.getTotalRxPackets());
//		mTextView[6]
//				.setText("TotalTxBytes : " + TrafficStats.getTotalTxBytes());
//		mTextView[7].setText("TotalTxPackets : "
//				+ TrafficStats.getTotalTxPackets());
//		mTextView[8].setText("UidRxBytes : " + TrafficStats.getUidRxBytes(uid));
//		mTextView[9].setText("UidTxBytes : " + TrafficStats.getUidTxBytes(uid));
	}

	private long getMobileRxBytes() {
		return getAll(mobile_iface_list, "rx_bytes");
	}

	private long getMobileRxPackets() {
		return getAll(mobile_iface_list, "rx_packets");
	}

	private long getMobileTxBytes() {
		return getAll(mobile_iface_list, "tx_bytes");
	}

	private long getMobileTxPackets() {
		return getAll(mobile_iface_list, "tx_packets");
	}

	private long getTotalRxBytes() {
		return readTotal("/statistics/rx_bytes");
	}

	private long getTotalRxPackets() {
		return readTotal("/statistics/rx_packets");
	}

	private long getTotalTxBytes() {
		return readTotal("/statistics/tx_bytes");
	}

	private long getTotalTxPackets() {
		return readTotal("/statistics/tx_packets");
	}

	private long getUidRxBytes(int uid) {
		return getUidBytes(uid, Tx_Rx.RX, Tcp_Udp.TCP_AND_UDP);
	}

	private long getUidTxBytes(int uid) {
		return getUidBytes(uid, Tx_Rx.TX, Tcp_Udp.TCP_AND_UDP);

	}

	static long getAll(String[] aaa, String bbb) {

		boolean supported = false;

		long total = 0;

		String filename = "";

		for (int i = 0; i < mobile_iface_list.length; i++) {

			filename = "/sys/class/net/" + aaa[i] + "/statistics/" + bbb;

			long number = readNumber(filename);
			if (number >= 0) {

				supported = true;

				total += number;
			}
		}
		
		if (supported)
			
			return total;
		
		return -1;
	}

	static long readNumber(String filename) {
		String number = "";
		File file = new File(filename);
		Reader reader = null;
		try {
			reader = new InputStreamReader(new FileInputStream(file));
			int tempchar;
			while ((tempchar = reader.read()) != -1) {
				if (((char) tempchar) != '\r' && ((char) tempchar) != '\n') {
					number += (char) tempchar;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (number == null || number.equals("")) {
			return 0;
		}
		return Long.parseLong(number);
	}

	static long getUidBytes(int uid, Tx_Rx tx_or_rx, Tcp_Udp tcp_or_udp) {
		String tcp_filename, udp_filename;
		long tcp_bytes = -1, udp_bytes = -1, total_bytes = 0;

		switch (tx_or_rx) {
		case TX:
			tcp_filename = "/proc/uid_stat/" + uid + "/tcp_snd";
			udp_filename = "/proc/uid_stat/" + uid + "/udp_snd";
			break;
		case RX:
			tcp_filename = "/proc/uid_stat/" + uid + "/tcp_rcv";
			udp_filename = "/proc/uid_stat/" + uid + "/udp_rcv";
			break;
		default:
			return -1;
		}

		switch (tcp_or_udp) {
		case TCP:
			tcp_bytes = readNumber(tcp_filename);
			total_bytes = (tcp_bytes >= 0) ? tcp_bytes : -1;
			break;
		case UDP:
			udp_bytes = readNumber(udp_filename);
			total_bytes = (udp_bytes >= 0) ? udp_bytes : -1;
			break;
		case TCP_AND_UDP:
			tcp_bytes = readNumber(tcp_filename);
			total_bytes += (tcp_bytes >= 0 ? tcp_bytes : 0);

			udp_bytes = readNumber(udp_filename);
			total_bytes += (udp_bytes >= 0 ? udp_bytes : 0);
			break;
		default:
			return -1;
		}
		return total_bytes;
	}

	public long readTotal(String suffix) {
		String dirName = "/sys/class/net/";
		File dir = new File(dirName);
		if (!dir.exists()) {
			return -1;
		}
		File[] files = dir.listFiles();
		long total = -1;
		for (File file : files) {
			if (!(file.getName().startsWith("lo") || file.getName().startsWith(
					"."))) {
				String filename = dirName + file.getName() + suffix;
				long num = readNumber(filename);
				if (num >= 0)
					total = total < 0 ? num : total + num;
			}
		}
		return total;
	}

}