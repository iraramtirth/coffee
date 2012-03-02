package org.coffee.seven.activity;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;
import coffee.seven.R;
import coffee.seven.service.SubRemindService;
import coffee.util.http.HttpClient;
/**
 * 程序启动界面
 * @author wangtao
 */
public class StartupActivity extends Activity{
	
	public static StartupActivity context;
	
	protected void onCreate(Bundle savedInstanceState){
		context = this;
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.startup);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		//网络不可用
		if(HttpClient.checkNetworkStatus(context) == false){
			 Toast.makeText(context, "网络不可用", Toast.LENGTH_LONG).show();
			 setNetwork();
		}else{
			HttpClient.setNetworkType(this);
			new AsyncTask<Void, Void, Void>() {
				@Override
				protected Void doInBackground(Void... params) {
					//更新
					//saleService.updateSaleAll(context);
					//开启服务
					Intent intent = new Intent();
					intent.setClass(context, SubRemindService.class);
					context.startService(intent);
					//跳转
					intent = new Intent();
					intent.setClass(context, MainTabActivity.class);
//					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					context.startActivity(intent);
					//context.finish();
					return null;
				}
			}.execute();
		}
	}
	
	
	private void setNetwork(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle("没有可用的网络");
		builder.setMessage("是否对网络进行设置？");
		builder.setPositiveButton("是",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int whichButton) {
					Intent mIntent = new Intent("/");
					ComponentName comp = new ComponentName(
							"com.android.settings",
							"com.android.settings.WirelessSettings");
					mIntent.setComponent(comp);
					mIntent.setAction(Intent.ACTION_VIEW);
					context.startActivityForResult(mIntent, 0); // 如果在设置完成后需要再次进行操作，可以重写操作代码，在这里不再重写
				}
		});
		builder.setNeutralButton("否", null);
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//设置完成
		if(requestCode == 0){
			this.onStart();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.finish();
	}
}
