package org.droid.util.sys;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.droid.util.http.HttpClient;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.RemoteViews;
import android.widget.Toast;
import coffee.seven.R;
import coffee.seven.SysConfig;

/**
 * 版本更新服务
 * 
 * @author wangtao
 */
public class VersionUpdateUtils {
	
	static int notification_id = 1917265656;
	static NotificationManager nm;
	static Notification notification;

	private static Context mContext;
	private static boolean manual = false ;
	private static String fr = ""; //渠道号
    public static void start(Context context, boolean manual, String from) {
        mContext = context;
        VersionUpdateUtils.manual = manual;
        CheckAsyncTask checkAsyncTask = new CheckAsyncTask();
        fr = from;
        checkAsyncTask.execute(fr);
    }

	/**
	 * 检查版本是否需要更新 如果需要更新，则启动一个Service下载apk文件
	 * 
	 * @author wangtao
	 */
	static class CheckAsyncTask extends AsyncTask<String, Void, Boolean> {
		//apk更新包的网络地址
		private String updateUrl = SysConfig.VERSION_UPDATE;
		private String apkUrl; // apk在服务器上的URL
		private float newCode; // 新版本编号
		private String newName; // 新版本名称
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				float vCurrent = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
				//vInfo 分别对象新版本编号，新版本名称、以及版本链接 
				//示例7 2.0.4 mmb_client.apk  
				updateUrl += "?fr=" + params[0];
				String vInfo = new HttpClient().get(updateUrl) + "";
				String[] infos = vInfo.split("\\s+");
				if("".equals(infos)){
					return false;
				}
				newCode = Float.valueOf(infos[0]);
				newName = infos[1];
				if (newCode > vCurrent) {
					apkUrl = updateUrl.substring(0,updateUrl.lastIndexOf("/")) 
						+ "/" + infos[2].trim();
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean isUpdate) {
			if(isUpdate) {
		        new AlertDialog.Builder(mContext)
                .setTitle("新版本")
                .setMessage(newName+"版本已经推出，是否更新？")
                .setPositiveButton("更新", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (nm == null) {
                            nm = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
                        }
                        notification = new Notification(R.drawable.icon, "下载中", System.currentTimeMillis());
                        notification.contentView = new RemoteViews(mContext.getPackageName(), 
                        		R.layout.notification_download);
                        // 使用notification.xml文件作VIEW
                        notification.contentView.setProgressBar(R.id.pb, 100, 0, false);
                        notification.contentIntent = PendingIntent.getActivity(mContext, 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
                        showNotification();
                        DownLoadAsyncTask task = new DownLoadAsyncTask();
                        task.execute(apkUrl);
                    }
                }).setNegativeButton("取消", null).show();
			}else{
			    if(manual){
			        Toast.makeText(mContext, "您当前的版本已是最新版", 1500).show();
			    }
			}
		}
	}

	public static void showNotification() {
		nm.notify(notification_id, notification);
	}

	public static String download(String apkUrl) throws Exception {
		if (apkUrl == null) {
			return null;
		}
		String fileName = apkUrl.substring(apkUrl.lastIndexOf("/") + 1);
		String filePath = Environment.getExternalStorageDirectory() + "/" + fileName;
		InputStream in = null;
		BufferedOutputStream bout = null;
		try {
			URLConnection uc = new URL(apkUrl).openConnection();
			in = uc.getInputStream();
			int filetotal = uc.getContentLength(); // 远程文件的总大小，单位 b
			int hasRead = 0;// 已经读取的总大小,单位b
			int len = 0;
			byte[] data = new byte[1024 * 10];
			File file = new File(filePath);
			if (file.exists() == false) {
				//FileUtils.createNewFileOrDir(file.getPath());
				if(file.getParentFile().exists() == false){
					file.getParentFile().mkdirs();
				}//java.io.IOException: Read-only file system
//				Runtime.getRuntime().exec("mount -o remount rw /");
				//Runtime.getRuntime().exec("chmod 777 " + Environment.getExternalStorageDirectory());
				file.createNewFile();
			}
			filePath=file.getPath();
			bout = new BufferedOutputStream(new FileOutputStream(file, false));
			while ((len = in.read(data)) != -1) {
				bout.write(data, 0, len);
				hasRead += len;
				notification.contentView.setProgressBar(R.id.pb, filetotal, hasRead, false);
				showNotification();
			}
			bout.flush();
			bout.close();
			in.close();
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(filePath)), "application/vnd.android.package-archive");
			notification.contentIntent = PendingIntent.getActivity(mContext, 1, intent, PendingIntent.FLAG_ONE_SHOT);
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			notification.tickerText="下载完成";
			notification.contentView.setTextViewText(R.id.down_tv, "下载完成");
			nm.cancel(notification_id);
			showNotification();
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (bout != null) {
					bout.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (Exception e) {
			} finally {
			}
		}
		return filePath;
	}

	/**
	 * 下载 apk文件，完成后执行安装
	 * 
	 * @author coffee
	 */
	static class DownLoadAsyncTask extends AsyncTask<String, Void, String> {
		private String localApkPath;// 下载完成后保存的本地URL
		// private VersionUpdateService service ;
		private String apkUrl;

		@Override
		protected String doInBackground(String... params) {
			try {
				// service = params[0];
				// localApkPath = service.download();
				apkUrl = params[0];
				localApkPath = download(apkUrl);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			return localApkPath;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result == null) {// 出现异常
				new AlertDialog.Builder(mContext).setTitle("下载失败。无存储卡或网络异常")
				.setPositiveButton("重试", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DownLoadAsyncTask asyncTask = new DownLoadAsyncTask();
						asyncTask.execute(apkUrl);
					}
				}).setNegativeButton("取消", null).show();
			} else {
				
			}
		}
	}
}
