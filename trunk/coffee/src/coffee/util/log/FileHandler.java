package coffee.util.log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import coffee.util.log.Log.LogProps;

/**
 * 自定义FileHandler 该方法的源码取自 {@link java.util.logging.FileHandler} 主要修改了
 * 
 * @author coffee
 * 
 *         2013-1-6 上午11:30:14
 */
public class FileHandler extends StreamHandler {
	private String pattern;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private static Timer fileTimer;

	private void configure() {

		String cname = getClass().getName();

		pattern = LogProps
				.getStringProperty(cname + ".pattern", "~/logs%u.log");

		setLevel(LogProps.getLevelProperty(cname + ".level", Level.ALL));
		setFilter(LogProps.getFilterProperty(cname + ".filter", null));
		setFormatter(LogProps.getFormatterProperty(cname + ".formatter",
				new FileFormatter()));
		try {
			setEncoding(LogProps.getStringProperty(cname + ".encoding", null));
		} catch (Exception ex) {
			try {
				setEncoding(null);
			} catch (Exception ex2) {
				// doing a setEncoding with null should always work.
				// assert false;
			}
		}
	}

	/**
	 * 只保留一个构造方法, 其他配置全部从配置文件中读取
	 * 
	 * @throws IOException
	 * @throws SecurityException
	 */
	public FileHandler() {
		configure();
		// 设置out流
		setOutputStream();
		startFileTimer();
	}

	private void setOutputStream() {
		String fname = pattern.substring(0, pattern.lastIndexOf("%"));
		fname = fname + sdf.format(new Date()) + ".log";
		FileOutputStream fout = null;
		BufferedOutputStream bout = null;
		try {
			File logFile = new File(fname.toString());
			if (logFile.exists() == false) {
				if (logFile.getParentFile().exists() == false) {
					logFile.getParentFile().mkdirs();
				}
				logFile.createNewFile();
			}
			fout = new FileOutputStream(logFile, true);
			bout = new BufferedOutputStream(fout);
			setOutputStream(bout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开启计时器,每天00点开始生成一个新的文件
	 */
	private void startFileTimer() {
		if (fileTimer != null) {
			return;
		}
		fileTimer = new Timer();
		
		Log.info(this, "startFileTimer");
		
		Calendar calendar = Calendar.getInstance();
		// 当前时间的时分秒
		int curHour = calendar.get(Calendar.HOUR_OF_DAY);
		int curMinute = calendar.get(Calendar.MINUTE);
		int curSecond = calendar.get(Calendar.SECOND);
		int delay = (23 - curHour) * 60 * 60 + (60 * curMinute) * 60
				+ (60 - curSecond);// 秒

		fileTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				setOutputStream();
			}
		}, delay, 1000 * 60 * 60 * 24);
	}

	public static void cancelTimer() {
		if (fileTimer != null) {
			fileTimer.cancel();
			fileTimer = null;
		}
	}

	/**
	 * Format and publish a <tt>LogRecord</tt>.
	 * 
	 * @param record
	 *            description of the log event. A null record is silently
	 *            ignored and is not published
	 */
	public synchronized void publish(LogRecord record) {
		if (!isLoggable(record)) {
			return;
		}
		super.publish(record);
		flush();
	}

}
