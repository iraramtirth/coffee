package org.coffee.util.activity;

import org.coffee.R;
import org.coffee.os.Log;

import android.content.res.Configuration;
import android.os.Bundle;

public class ActivityConfigChanged extends ActivityCycle{
	
	
	private final String TAG = "ActivityConfigChanged";
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.browser);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {    
	    super.onConfigurationChanged(newConfig);
	    // 检测屏幕的方向：纵向或横向
	    if (this.getResources().getConfiguration().orientation 
	            == Configuration.ORIENTATION_LANDSCAPE) {
	        //当前为横屏， 在此处添加额外的处理代码
	    	Log.info(TAG, "当前为横屏");
	    }
	    else if (this.getResources().getConfiguration().orientation 
	            == Configuration.ORIENTATION_PORTRAIT) {
	        //当前为竖屏， 在此处添加额外的处理代码
	    	Log.info(TAG, "当前为竖屏");
	    }
	    //检测实体键盘的状态：推出或者合上    
	    if (newConfig.hardKeyboardHidden 
	            == Configuration.HARDKEYBOARDHIDDEN_NO){ 
	        //实体键盘处于推出状态，在此处添加额外的处理代码
	    	Log.info(TAG, "实体键盘处于推出状态");
	    } 
	    else if (newConfig.hardKeyboardHidden
	            == Configuration.HARDKEYBOARDHIDDEN_YES){ 
	        //实体键盘处于合上状态，在此处添加额外的处理代码\
	    	Log.info(TAG, "实体键盘处于合上状态");
	    }
	}
	
}
