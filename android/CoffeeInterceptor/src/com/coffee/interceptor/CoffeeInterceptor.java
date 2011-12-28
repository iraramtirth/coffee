package com.coffee.interceptor;

import java.lang.reflect.Method;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.coffee.ContactsUtils;
import com.coffee.ScreenLock;
import com.coffee.adapter.ContactAdapter;

//import com.android.internal.telephony.ITelephony;

public class CoffeeInterceptor extends BroadcastReceiver {
	private final String TAG = "TAG_CoffeeInterceptor";

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isScreenOn = true;//屏幕唤醒状态
		String action = intent.getAction();
		// 拦截电话
		if ("android.intent.action.PHONE_STATE".equals(action)) {
			//获取屏幕当前状态
			isScreenOn = ScreenLock.isScreenOn(context);
			System.out.println("唤醒?"+isScreenOn);
			// 状态
			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			// 呼入的号码
			String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
			System.out.println(state + "\t" + phoneNumber);
			try {
				//黑名单
				if(ContactAdapter.phoneFilterMap.keySet().contains(phoneNumber)){
					//拦截
					//java.lang.RuntimeException: BroadcastReceiver trying to return result during a non-ordered broadcast
					//this.abortBroadcast();
					//挂断
					Method getITelephonyMethod = TelephonyManager.class.getDeclaredMethod("getITelephony", (Class[]) null);
					getITelephonyMethod.setAccessible(true);
					TelephonyManager mTelephonyManager = (TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE);
					Object mITelephony =  getITelephonyMethod.invoke(mTelephonyManager, (Object[]) null);
					//挂断 - mITelephony.endCall();
					mITelephony.getClass().getMethod("endCall", new Class[] { })
							.invoke(mITelephony, new Object[] {});
					//取消通知 - mITelephony.cancelMissedCallsNotification()
					mITelephony.getClass().getMethod("cancelMissedCallsNotification", new Class[] { })
						.invoke(mITelephony, new Object[] {});
					
					//删除来电记录
					ContactsUtils.deleteCallLog(context, phoneNumber);
					//屏幕休眠-则恢复休眠
					if(isScreenOn == false){
						//休眠
						ScreenLock.sleep(context);
						//锁屏
						ScreenLock.lockKeyguard(context);
					}
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}
		}
		// 连接短信
		if ("android.provider.Telephony.SMS_RECEIVED".equals(action)) {
			Bundle bundle = intent.getExtras();
			Object[] msgs = (Object[]) bundle.get("pdus");
			if (msgs != null) {
				for (Object msg : msgs) {
					SmsMessage smsMsg = SmsMessage.createFromPdu((byte[]) msg);
					String phoneNumber = smsMsg.getOriginatingAddress();
					//去除之前的+86等
					if(phoneNumber.length()  > 11){
						phoneNumber = phoneNumber.substring(phoneNumber.length() - 11);
					}
					//黑名单
					if (ContactAdapter.messageFilterMap.keySet().contains(phoneNumber)) {
						this.abortBroadcast();// 终止传播--拦截
					}
					System.out.println(smsMsg.getOriginatingAddress() + " "
							+ smsMsg.getMessageBody() + " "
							+ smsMsg.getIndexOnIcc());
				}
			}
		}
	}

}