package coffee.code.action;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import coffee.seven.App;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.activity.StartupActivity;

public class NotificationUtils {
	private static final String TAG = NotificationUtils.class.getSimpleName();
	/**
	 * 启动客户端 PendingIntent
	 */
	public static PendingIntent pendingIntent;
	static{
		Intent notiIntent = new Intent();
		notiIntent.setAction(Intent.ACTION_MAIN);
		notiIntent.addCategory(Intent.CATEGORY_LAUNCHER); 
		notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);  
		notiIntent.setClass(App.context, StartupActivity.class);
		
		pendingIntent = PendingIntent.getActivity(App.context,
				SysConfig.REQUEST_CODE_NOTIFICATION, notiIntent, 0);
	}
	
	public static void notify(String title, String content, int notityId){
		Log.d(TAG, "【新通知】" + content);
		
		NotificationManager nManager = (NotificationManager) App.context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.icon, 
				SysConfig.notiSubTitle, System.currentTimeMillis());
		//启动程序【Intent为单机notification的动作】
		noti.setLatestEventInfo(App.context, title, content, pendingIntent);
		//震动
		noti.vibrate = new long[]{1000 , 1000, 1000};
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		noti.sound = alert;
		noti.flags = Notification.FLAG_AUTO_CANCEL;//自动取消
		nManager.notify(notityId, noti);
	}
}
