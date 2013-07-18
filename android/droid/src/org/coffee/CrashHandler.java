package org.coffee;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Properties;

import org.coffee.util.framework.MailUtil;
import org.coffee.util.log.Log;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;
import coffee.frame.fragment.MainFragment;
import coffee.frame.utils.ActivityMgr;

/**
 * 全局未捕获的异常
 * 
 * @author coffee<br>
 *         2013-1-11上午11:08:54
 */
public class CrashHandler implements UncaughtExceptionHandler {

	private Context mContext;

	/**
	 * @param ctx
	 */
	public CrashHandler(Context ctx) {
		mContext = ctx;
	}

	private static final String TAG = CrashHandler.class.getCanonicalName();

	private static String[] EMAIL_SENDER = { "iipopp001@126.com", "iipopp002@126.com",
			"iipopp003@126.com", "iipopp004@126.com", "iipopp005@126.com",
			"iipopp006@126.com", "iipopp007@126.com", "iipopp008@126.com",
			"iipopp009@126.com" };
	private static final String SMTP_HOST = "smtp.126.com";
	private static final String PWD = "iipopp";
	// 786792111@qq.com
	private static final String[] EMAIL_RECEIVER = new String[] { "786792111@qq.com" };

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		ex.printStackTrace();
		new Thread() {
			public void run() {
				Looper.prepare();
				showCrashTip(ex);
				Looper.loop();
			}
		}.start();
	}

	public static void sendCrashReports(final String errorMsg) {

		// 跑线程, 将报错信息发送到指定的邮箱
		Thread sendMailThread = new Thread() {
			@Override
			public void run() {
				try {
					String subject = "Crash Report: AppVersion: "
							+ App.getContext()
									.getPackageManager()
									.getPackageInfo(
											App.getContext().getPackageName(),
											Context.MODE_PRIVATE).versionName;

					Properties props = new Properties();
					props.put("mail.smtp.host", SMTP_HOST);
					props.put("mail.smtp.auth", "true");
					MailUtil.MailInfo mailInfo = new MailUtil.MailInfo();
					mailInfo.setFrom(EMAIL_SENDER[(int) (Math.floor(Math
							.random() * EMAIL_SENDER.length))]);
					mailInfo.setPassword(PWD);
					mailInfo.setSmtpHost(SMTP_HOST);
					mailInfo.setNeedAuth(true);
					mailInfo.setToList(EMAIL_RECEIVER);
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

		sendMailThread.start();
	}

	public void showCrashTip(final Throwable ex) {
		String defaultMsg = "程序崩溃";
		Activity curActivity = MainFragment.getMainFragment();
		if (curActivity == null) {
			curActivity = ActivityMgr.peek();
			if (curActivity == null) {
				Toast.makeText(App.getContext(), defaultMsg, Toast.LENGTH_LONG)
						.show();
				return;
			} else {
				// continue;
			}
		}

		try {
			AlertDialog.Builder builder = new AlertDialog.Builder(curActivity);
			builder.setMessage("是否提交该错误信息给Fetion？");
			builder.setTitle("程序崩溃");
			builder.setPositiveButton("发送", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// 收集设备信息
					String deviceInfo = getDeviceInfo(mContext);
					// 保存错误报告文件
					String errorInfo = getErrorInfo(ex);
					// 发送错误报告到服务器
					sendCrashReports(errorInfo + "_" + deviceInfo);
					Toast.makeText(App.getContext(), "3秒钟后程序将退出",
							Toast.LENGTH_LONG).show();
					new Handler() {
						public void handleMessage(Message msg) {
							System.exit(0);
						};
					}.sendMessageDelayed(Message.obtain(), 3000);
				}
			});
			builder.setNegativeButton("退出", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					System.exit(-1);
				}
			});
			builder.create().show();
		} catch (Exception e) {
			// AndroidUtil.toast(msg, true);
			e.printStackTrace();
		}
	}

	private static final String LINE = System.getProperty("line.separator");

	/**
	 * 收集程序崩溃的设备信息
	 * 
	 * @param ctx
	 */
	public String getDeviceInfo(Context ctx) {
		StringBuilder sb = new StringBuilder();
		sb.append("CLIENT-INFO").append(LINE);
		sb.append("Id: ").append(Build.ID).append(LINE);
		sb.append("Display: ").append(Build.DISPLAY).append(LINE);
		sb.append("Product: ").append(Build.PRODUCT).append(LINE);
		sb.append("Device: ").append(Build.DEVICE).append(LINE);
		sb.append("Board: ").append(Build.BOARD).append(LINE);
		sb.append("CpuAbility: ").append(Build.CPU_ABI).append(LINE);
		sb.append("Manufacturer: ").append(Build.MANUFACTURER).append(LINE);
		sb.append("Brand: ").append(Build.BRAND).append(LINE);
		sb.append("Model: ").append(Build.MODEL).append(LINE);
		sb.append("Type: ").append(Build.TYPE).append(LINE);
		sb.append("Tags: ").append(Build.TAGS).append(LINE);
		sb.append("FingerPrint: ").append(Build.FINGERPRINT).append(LINE);

		sb.append("Version.Incremental: ").append(Build.VERSION.INCREMENTAL)
				.append(LINE);
		sb.append("Version.Release: ").append(Build.VERSION.RELEASE)
				.append(LINE);
		sb.append("SDK: ").append(Build.VERSION.SDK).append(LINE);
		sb.append("SDKInt: ").append(Build.VERSION.SDK_INT).append(LINE);
		sb.append("Version.CodeName: ").append(Build.VERSION.CODENAME)
				.append(LINE);

		String deviceInfo = sb.toString();
		sb.delete(0, sb.length());
		return deviceInfo;
	}

	/**
	 * 异常信息
	 */
	public String getErrorInfo(Throwable ex) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		ex.printStackTrace(ps);
		final String errorMsg = new String(baos.toByteArray());
		return errorMsg;
	}
}
