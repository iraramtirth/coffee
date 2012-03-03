package coffee.code.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import coffee.code.App;
import coffee.code.Intents;
import coffee.code.service.SubRemindService;

/**
 * 开机启动
 * 
 * @author wangtao
 */
public class ActionReceiver extends BroadcastReceiver {

	final String TAG = ActionReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		// 开机启动SubRemindService
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Intent i = new Intent();
			i.setClass(App.context, SubRemindService.class);
			App.context.startService(i);
		}
		// 订阅提醒
		else if (Intents.ACTION_SALE_SUB_REMIND.equals(intent.getAction())) {
			// Log.d(TAG, "【接收到广播】" + intent);
			// Map<Long,List<SaleBean>> subMap = saleService.getSubMap();
			// Log.d(TAG, subMap + "");
			// if(subMap == null){
			// return;
			// }
			// long startTime =
			// intent.getLongExtra(IActivity.KEY_EXTRA_SALE_STARTTIME, 0);
			// List<SaleBean> saleList = subMap.get(startTime);
			// if(saleList != null && startTime != 0){
			// //发送提醒
			// NotificationUtils.notify(SysConfig.notiSubTitle,
			// getNotifyInfo(startTime, subMap), IActivity.ID_NOTI_REMIND_SUB);
			// //删除订阅的sale
			// saleService.deleteSub(saleList);
			// }
		}
	}

}