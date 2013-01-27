package org.coffee.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
/**
 * 程序启动：   onCreate -> onStart -> onResume
 * HOME键:  onSaveInstanceState -> onPause -> onStop
 * BACK键： finish -> onPause -> onStop -> onDestory
 * 再次执行：   onRestart -> onStart -> onResume
 * 横屏<-->竖屏(切换)
 * onSaveInstanceState -> onPause -> onStop --> onDestory --> onCreate -> onStart -> onResume
 * 
 * 调用finish onPause -> onStop -> onDestroy  (程序结束)
 * 再次执行       onCreate -> onStart -> onResume (重新启动)
 * killProcess的时候系统不执行onDestroy;直接杀死
 */
public class ActivityCycle_A extends Activity {
	private String TAG = "___ActivityCycle___A";
	
	private ActivityCycle_A context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		Log.e(TAG, "A_onCreate");
		
		Button btn = new Button(this);
		btn.setText("跳转到B");
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(context, ActivityCycle_B.class);
				context.startActivity(i);
			}
		});
		this.setContentView(btn);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.e(TAG, "A_onStart");
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.e(TAG, "A_onSaveInstanceState");
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "A_onResume");
	}
	@Override
	protected void onPause(){
		super.onPause();
		Log.e(TAG, "A_onPause");
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		Log.e(TAG, "A_onRestart");
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.e(TAG, "A_onStop");
	}
    
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Log.e(TAG, "A_onDestroy");
	}
	
}
