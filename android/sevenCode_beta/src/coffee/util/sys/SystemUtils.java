package coffee.util.sys;

import coffee.seven.App;

import android.content.Context;
import android.telephony.TelephonyManager;


/**
 * 系统工具类
 * @author wangtao
 */
public class SystemUtils {
	
	/**
	 * 获取设备的唯一编号： 需要加入如下权限
	 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
	 * @param context
	 * @return : 返回IMEI 如果IMEI为null 则返回phone
	 */
	public static String getDeviceId(){
		TelephonyManager mTelephonyManager = (TelephonyManager) App.context.getApplicationContext()
			.getSystemService(Context.TELEPHONY_SERVICE);
		//*#06# IMEI
		String deviceId = mTelephonyManager.getDeviceId();
		if(deviceId == null){
			//phone
			deviceId = mTelephonyManager.getLine1Number();//
		}
		return deviceId;
	}
	
	public static String getPhoneNumber(){
		TelephonyManager mTelephonyManager = (TelephonyManager) App.context.getApplicationContext()
			.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNumber = mTelephonyManager.getLine1Number();
		if(phoneNumber == null){
			phoneNumber = mTelephonyManager.getSimSerialNumber();
		}
		return phoneNumber;
	}
	
}
