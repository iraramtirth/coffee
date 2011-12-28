package org.coffee.common.util;

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
public class ActivityCycle extends Activity {
	@Override
	protected void onStart() {
		super.onStart();
		Log.e("___ActivityCycle___", "onStart");
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.e("___ActivityCycle___", "onSaveInstanceState");
	}
	@Override
	protected void onResume() {
		super.onResume();
		Log.e("___ActivityCycle___", "onResume");
	}
	@Override
	protected void onPause(){
		super.onPause();
		Log.e("___ActivityCycle___", "onPause");
	}
	@Override
	protected void onRestart(){
		super.onRestart();
		Log.e("___ActivityCycle___", "onRestart");
	}
	@Override
	protected void onStop() {
		super.onStop();
		Log.e("___ActivityCycle___", "onStop");
	}
    
	@Override
	protected void onDestroy(){
		super.onDestroy();
		Log.e("___ActivityCycle___", "onDestroy");
	}
	
}
