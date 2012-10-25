package org.coffee.util.sys;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

/**
 * logcat [<option>] ... [<filter-spec>] ...
 * 注意：filter-spec必须放在最后
 * <uses-permission android:name="android.permission.READ_LOGS" />
 * @author coffee
 */
public class LogService extends Service {
	private static final String TAG = "LogService";

	private String LOG_FILE; //log文件的路径

	private LogOutputThread logThread;
	private Process proc;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		//挂载了内存卡
		if(Environment.getExternalStorageState().contains(Environment.MEDIA_MOUNTED)){
			LOG_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/log.txt"; 
		}else{
			LOG_FILE = getDir("log", 0).getAbsolutePath();
		}
		//logcat time -f /sdcard/log.txt *:E //过滤规则放到参数的最后
		String cmdLine = "logcat -v time *:W"; //-v 是格式化，意思是输出时间   *:E *代表任意TAG， E指的是过滤级别
		try{
			proc = Runtime.getRuntime().exec(cmdLine);
			logThread = new LogOutputThread(proc.getInputStream());
			logThread.start();
			//proc.waitFor();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	 
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (proc != null) {
			proc.destroy();
		}
		logThread.setOver(true);//结束log线程
	}
	/**
	 * log线程
	 * 负责从stdout流中读取log
	 * @author coffee
	 */
	private class LogOutputThread extends Thread {
		private BufferedReader reader;
		private BufferedWriter writer;
		private boolean isOver = false;
		private String msg;
		private LogOutputThread(InputStream in){
			try{
				reader = new BufferedReader(new InputStreamReader(in));
				File file = new File(LOG_FILE);
				if(file.exists() == false){
					if(file.getParentFile().exists() == false){
						file.getParentFile().mkdirs();
					}
					file.createNewFile();
				}
				writer = new BufferedWriter(new FileWriter(file));
			}catch(Exception e){
				msg = e.getMessage();
				Log.e(TAG, msg);
			}
		}
		
		@Override
		public void run() {
			String line = null;
			try{
				while(writer != null){
					line = reader.readLine();
					if(line != null){
						writer.write(line);
						writer.write("\n");
						writer.flush();
					}
					if(isOver){
						break;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				try {//最后一定要关闭流
					if(writer != null){
						writer.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		public void setOver(boolean isOver) {
			this.isOver = isOver;
		}
	}

}