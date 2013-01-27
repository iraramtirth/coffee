package org.coffee;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

/**
 * 全局未捕获的异常
 * 
 * @author coffee
 */
public class GlobalUncaughtExceptionHandler implements UncaughtExceptionHandler {

	private final String TAG = GlobalUncaughtExceptionHandler.class
			.getCanonicalName();


	private String[] EMAIL_FROM_LIST = { "woyou01@yahoo.cn", "woyou002@yahoo.cn",
			"rcs3@yahoo.cn", "woyou04@yahoo.cn", "rcs5@yahoo.cn", "woyou06@yahoo.cn",
			"rcs7@yahoo.cn", "rcs8@yahoo.cn", "rcs9@yahoo.cn",
			"woyou10@yahoo.cn", "woyou11@yahoo.cn", "rcs12@yahoo.cn",
			"woyou013@yahoo.cn", "rcs14@yahoo.cn", "woyou15@yahoo.cn",
			"rcs16@yahoo.cn", "woyou017@yahoo.cn", "rcs18@yahoo.cn"};
	
	private static final String[] EMAIL_TO_LIST = new String[] {"786792111@qq.com" };

	
	private static final String SMTP_YAHOO_COM_CN = "smtp.mail.yahoo.com.cn";
	private static final String EMAIL_PASSWORD = "0123456789";

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(TAG, "Caught Global Exception", ex);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		ex.printStackTrace(ps);
		final String errorMsg = new String(baos.toByteArray());

		// 跑线程, 将报错信息发送到指定的邮箱
		Thread sendMailThread = new Thread() {
			@Override
			public void run() {
				try {
					String subject = "WoYou Crash Report"
							+ " ("
							+ "AppVersion: "
							+ App.getContext().getPackageManager()
									.getPackageInfo(App.getContext().getPackageName(),
											Context.MODE_PRIVATE).versionName;
					
					Properties props = new Properties();
					props.put("mail.smtp.host", SMTP_YAHOO_COM_CN);
					props.put("mail.smtp.auth", "true");
					MailUtil.MailInfo mailInfo = new MailUtil.MailInfo();
					mailInfo.setFrom(EMAIL_FROM_LIST[(int) (Math.floor(Math.random()
							* EMAIL_FROM_LIST.length))]);
					mailInfo.setPassword(EMAIL_PASSWORD);
					mailInfo.setSmtpHost(SMTP_YAHOO_COM_CN);
					mailInfo.setNeedAuth(true);
					mailInfo.setToList(EMAIL_TO_LIST);
					mailInfo.setSubject(subject);
					mailInfo.setContent(errorMsg);
					try {
						MailUtil.sendMail(mailInfo);
						Log.d(TAG, "Send mail successful");
					} catch (Exception e) {
						Log.w(TAG, "Send mail failed", e);
					}
				} catch (Exception e) {
					Log.e(TAG, "Get app info failed", e);
				}
			}
		};
		
		//sendMailThread.start();
	}

}
