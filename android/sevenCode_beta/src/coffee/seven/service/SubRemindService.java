package coffee.seven.service;

import static coffee.seven.action.SaleUtils.saleService;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.droid.util.lang.DateUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import coffee.seven.App;
import coffee.seven.Intents;
import coffee.seven.R;
import coffee.seven.SysConfig;
import coffee.seven.action.NotificationUtils;
import coffee.seven.activity.base.IActivity;
import coffee.seven.bean.SaleBean;
import coffee.seven.receiver.ActionReceiver;
import coffee.seven.service.remote.impl.MmbRemotelService;
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
		Log.e(TAG, "onCreate 正在启动服务....");
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		packageName = getPackageName();
		
		try {
			//获取服务器时间
			int i = 0;
			while(nowTime == -1  && i++ < 10){
				nowTime = new MmbRemotelService().getServerTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(timer == null){
			//最后访问时间计时器【7天未登录则提醒用户】
			timer = new Timer();
			//访问提醒
			timer.schedule(visitRemindTimerTask, 1000 * 10, 1000);
			//该操作跟onStart中的操作相同。
			subMap = saleService.getSubMap();
		}
		//从share
		lastVisitTime = SysConfig.getLastVisitTime();
		//删除缓存中7天以前的图片
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				File cacheDir = App.context.getDir("cache", 0);
				for(File file : cacheDir.listFiles()){
					if(file.lastModified() - System.currentTimeMillis() > 1000 * 60 * 60 * 24 * 7){
						file.delete();
					}
				}
				this.cancel();	//删除
			}
		},1000 * 60);
	}
	
	//每次单击订阅的时候， 链接一下该服务。然后重新从数据库获取数据
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Log.e(TAG, "onStart 正在启动服务....");
		//获取
		subMap = saleService.getSubMap();
		//发送订阅提醒
		setSubRemind();
	}
	
	private void setSubRemind(){
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		for(long startTime : subMap.keySet()){
			Intent intent =  new Intent(this, ActionReceiver.class);
			intent.setAction(Intents.ACTION_SALE_SUB_REMIND);
			intent.putExtra(IActivity.KEY_EXTRA_SALE_STARTTIME, startTime);
			PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);  
	        //重新设置  startTime - nowTime
			long triggerAtTime = System.currentTimeMillis() + startTime - nowTime;
			Log.d("ActionReceiver", startTime + " " + triggerAtTime + " " + DateUtils.format(new Date(triggerAtTime)));
			alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtTime , pi);
		}
	}
	
	private TimerTask visitRemindTimerTask = new TimerTask() {
		
		@Override
		public void run() {
			nowTime += 1000;//当前时间 +1000
			Log.d(TAG, subMap + "");
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
