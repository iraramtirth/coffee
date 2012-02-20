package coffee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
public class ActivityCycle_B extends Activity {
	private String TAG = "___ActivityCycle___B";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(TAG, "B_onCreate");
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Log.e(TAG, "B_onStart");
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.e(TAG, "B_onSaveInstanceState");
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.e(TAG, "B_onResume");
	}
	@Override
	protected void onPause(){
		super.onPause();
		Log.e(TAG, "B_onPause");
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		Log.e(TAG, "B_onRestart");
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.e(TAG, "B_onStop");
	}
    
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Log.e(TAG, "B_onDestroy");
	}
	
}
