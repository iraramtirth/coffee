package org.coffee.util.framework;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.text.format.Formatter;

/**
 * 系统工具类
 * @author coffee
 */
public class SystemUtils {
	/**
	 * 获取系统可用内存 
	 */
	public static String getAvailMemory(Activity context) {// 获取android当前可用内存大小
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		// mi.availMem; 当前系统的可用内存

		return Formatter.formatFileSize(context.getBaseContext(), mi.availMem);// 将获取的内存大小规格化
	}
	/**
	 * 获取总内存 
	 * 通过指令 cat /proc/meminfo查看到的文件内容如下
		MemTotal:          94172 kB
		MemFree:            2440 kB
		Buffers:             276 kB
		Cached:            39488 kB
		SwapCached:            0 kB
		Active:            44356 kB
		Inactive:          37416 kB
		......
	 */
	public static String getTotalMemory(Activity context) {
		String meminfoFile = "/proc/meminfo";// 系统内存信息文件
		long initial_memory = 0;

		try {
			FileReader localFileReader = new FileReader(meminfoFile);
			BufferedReader reader = new BufferedReader(localFileReader, 8192);
			String line = reader.readLine();// 读取meminfo第一行，系统总内存大小
			
			String total = "";
			Matcher matcher = Pattern.compile("\\s(\\d+)\\s").matcher(line);
			if (matcher.find()) {
				total = matcher.group(1);
			}
			initial_memory = Integer.valueOf(total).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			reader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(context.getBaseContext(),
				initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

}
