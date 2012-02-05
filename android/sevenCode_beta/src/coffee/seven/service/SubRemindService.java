package coffee.seven.service;

import java.io.File;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import coffee.seven.App;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.NotificationUtils;
import coffee.seven.activity.base.IActivity;
import coffee.seven.bean.SaleBean;
import coffee.seven.service.remote.impl.RemoteService;
/**
 * 订阅提醒
 * 常驻内存。
 * @author wangtao
 */
public class SubRemindService extends Service {
	public final String TAG = SubRemindService.class.getSimpleName();

	/**
	 * 订阅列表 
	 * k:时间  v:该时间点得列表
	 */
	private Map<Long,List<SaleBean>> subMap;
	
	private Timer timer;
	public static long nowTime = -1;
	private static long lastVisitTime = -1; //最后访问时间
	
	private ActivityManager activityManager; 
	private String packageName = ""; 
	private List<RunningTaskInfo> tasksInfo = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	//通知Intent
	@Override
	public void onCreate() {
		super.onCreate();
	 
	}
	
	//每次单击订阅的时候， 链接一下该服务。然后重新从数据库获取数据
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.e(TAG, "onStart 正在启动服务....");
		//获取
		//subMap = saleService.getSubMap();
		//发送订阅提醒
		//setSubRemind();
	}
	
	 
	
	private TimerTask visitRemindTimerTask = new TimerTask() {
		
		@Override
		public void run() {
			nowTime += 1000;//当前时间 +1000
			if(lastVisitTime == -1){
				lastVisitTime = SysConfig.getLastVisitTime();;
				return;
			}
			//在线不提醒
			if(isTopActivity()){
				lastVisitTime = System.currentTimeMillis();//重置
				return;
			}
			if(System.currentTimeMillis() - lastVisitTime > 1000 * 60 * 60 * 24 * 7){//一分钟
				lastVisitTime = System.currentTimeMillis();//重置
				//发送提醒
				NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				Notification noti = new Notification(R.drawable.icon, 
						SysConfig.notiLoginTitle,	System.currentTimeMillis() + 3000);//延迟三秒中再提醒 
				//启动程序【Intent为单机notification的动作】
				noti.setLatestEventInfo(getApplicationContext(), SysConfig.notiLoginTitle, 
						SysConfig.notiLoginContent, NotificationUtils.pendingIntent);
				//震动
				noti.vibrate = new long[]{1000 , 1000, 1000};
				Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
				noti.sound = alert;
				noti.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;//自动取消
				nManager.notify(IActivity.ID_NOTI_REMIND_LOGIN, noti);
			}
		}
	};
	
	private boolean isTopActivity(){
		tasksInfo = activityManager.getRunningTasks(1);
		if(tasksInfo.size() > 0){
			//应用程序位于堆栈的顶层
			if(packageName.equals(tasksInfo.get(0).topActivity.getPackageName())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "服务销毁....");
		if(timer != null){
			timer.cancel();
		}
	}

}
