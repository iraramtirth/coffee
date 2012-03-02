package coffee.seven;

import coffee.seven.activity.base.IActivity;
import coffee.seven.service.SubRemindService;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;


/**
 * 系统的全局配置
 */
public class SysConfig {
	                      
	public static final String FROM = "20149"; //渠道号
	
	public static final String DB_NAME = "mmb.db";
	public static final String SERVER_URL = "http://qudao.ebinf.com/mmb_server_android/android/";
//	public static final String SERVER_URL = "http://192.168.3.120/mmb_server/android/";
//	public static final String SERVER_URL = "http://10.0.2.2/mmb_server/android/";
//	public static final String SERVER_URL = "http://192.168.56.1:8080/mmb_android_server/android/";
	
	public static final long REMAIN_COUNT_PERIOD = 1000 * 60 * 10; //获取商品剩余量的时间间隔
	public static String CACHE_DIR;	//图片的缓存目录
	
	public static final int GALLERY_SMALL_HEIGHT = 305; //dip
	public static final int GALLERY_BIG_HEIGHT = 410;	//dip
	
	//该字段主要用于测试
	public static final boolean ENABLE_VERSION_UPDATE = true;// 开启版本更新
	public static boolean ENABLE_TIMER_NOW = false;	  	 	 // 开启计时器 
	public static boolean ENABLE_TIMER_NEXT = false;	   
	
	//mmb主站链接
	public static String MMB_HOST = "http://www.mmb.cn/?fr="+FROM;
	//帮助页面的三个连接
	public static String HELP = "http://www.mmb.cn/help.jsp?fr="+FROM;
	//版本更新的url
	public static String VERSION_UPDATE = "http://qudao.ebinf.com/mmb_server_android/version_update.jsp";
	
	public static final int REQUEST_CODE_MMBSITE = 0;		//MMB主站跳转
	public static final int REQUEST_CODE_NOTIFICATION = 1;	//单击通知后跳转
	public static final int NOTITY_PERIOD = 4;				//顶部通知间隔
	public static long phone400 = 4008864966L;				//400电话
	public static boolean ENABLE_TIMER = true;
	
	//系统通知信息
	public static String notiLoginTitle = "好优限时抢提醒";
	public static String notiLoginContent = "又有新的抢购活动啦！快去看看吧";
	public static String notiSubTitle = "好优限时抢提醒";
	public static String notiSubContent = "抢购开始啦！快去看看吧";
	
	static{
		CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	public static long getNowTime() {
		return SubRemindService.nowTime;
	}
	
	/**
	 * 设置最后访问时间 [相对于本地时间]
	 */
	public static void setLastVisitTime(){
		SharedPreferences prefs = App.context.getSharedPreferences("mmb", Context.MODE_WORLD_WRITEABLE);
		prefs.edit().putLong(IActivity.PREF_LAST_VISIT_TIME, System.currentTimeMillis())
		.commit();
	}
	/**
	 * 获取最后访问时间
	 */
	public static long getLastVisitTime(){
		SharedPreferences prefs = App.context.getSharedPreferences("mmb", Context.MODE_WORLD_READABLE);
		long last = prefs.getLong(IActivity.PREF_LAST_VISIT_TIME, System.currentTimeMillis());
		return last;
	}
}
