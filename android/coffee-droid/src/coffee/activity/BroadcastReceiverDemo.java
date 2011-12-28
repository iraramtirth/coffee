package coffee.activity;

import org.droid.coffee.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiverDemo extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		//创建通知管理器
		NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);
		//创建通知
		Notification notification = new Notification(R.drawable.icon,"hello",System.currentTimeMillis());
		//重新定义该方法中的intent参数对象
		//该Intent使得当用户点击该通知后发出这个Intent
//		intent = new Intent(context,AlarmDemo.class);
		//请注意，如果要以该Intent启动一个Activity，一定要设置 Intent.FLAG_ACTIVITY_NEW_TASK 标记
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// PendingIntent.FLAG_UPDATE_CURRENT表示：如果描述的PendingIntent已存在，则改变已存在的PendingIntent的Extra数据为新的PendingIntent的Extra数据
		PendingIntent contentIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, "xxx", "helloworld", contentIntent);
		//FLAG_ONGOING_EVENT使得通知出现在"正在运行"栏目中
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		nm.notify(0x0, notification);
	}

}
