package org.coffee.util.framework;

import java.io.BufferedReader;
import java.io.FileReader;

import org.coffee.util.log.Log;

import android.os.Process;

public class CpuUtils {

	private static final String TAG = "CpuUtils";

	private static long workT, totalT, workAMT;
	private static long total, totalBefore, work, workBefore, workAM, workAMBefore;

	public static void info() {
		BufferedReader readStream = null;

		try {
			readStream = new BufferedReader(new FileReader("/proc/stat"));
			String[] a = readStream.readLine().split("[ ]+", 9);
			long work = Long.parseLong(a[1]) + Long.parseLong(a[2]) + Long.parseLong(a[3]);
			long total = work + Long.parseLong(a[4]) + Long.parseLong(a[5]) + Long.parseLong(a[6])
					+ Long.parseLong(a[7]);

			readStream = new BufferedReader(new FileReader("/proc/" + Process.myPid() + "/stat"));
			a = readStream.readLine().split("[ ]+", 18);
			long workAM = Long.parseLong(a[13]) + Long.parseLong(a[14]) + Long.parseLong(a[15]) + Long.parseLong(a[16]);

			workT = work - workBefore;
			totalT = total - totalBefore;
			workAMT = workAM - workAMBefore;

			Log.i(TAG, "--------------");
			Log.i(TAG, String.valueOf(workT * 100 / (float) totalT));

			Log.i(TAG, String.valueOf(workAMT * 100 / (float) totalT));

			Log.i(TAG, String.valueOf((workT - workAMT) * 100 / (float) totalT));

			workBefore = work;
			totalBefore = total;
			workAMBefore = workAM;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (readStream != null) {
					readStream.close();
				}
			} catch (Exception e) {
				Log.e(TAG, "", e);
			}

		}

	}
}
