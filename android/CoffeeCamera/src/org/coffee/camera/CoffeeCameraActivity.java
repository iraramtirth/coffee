package org.coffee.camera;

import org.coffee.camera.view.MySurfaceView;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class CoffeeCameraActivity extends Activity{
	private int currentMediaVolume = 0;
	private AudioManager audioManager;
	
	private MySurfaceView mSurfaceView;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new MySurfaceView(this);
        mSurfaceView.setLayoutParams(new LinearLayout.LayoutParams(10, 10));
    	LinearLayout lnLayout = new LinearLayout(this);
		lnLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		lnLayout.setOrientation(LinearLayout.VERTICAL);
		lnLayout.addView(mSurfaceView);
		this.setContentView(lnLayout);
        //setContentView(new Button(this));
        
        //获取当前的系统音量
    	audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		currentMediaVolume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
		//静音
		this.setStreamVolume(0);
    }
	@Override
	public boolean onTrackballEvent(MotionEvent event) {
		switch (event.getAction()) {
			//轨迹球单击
			case MotionEvent.ACTION_DOWN:
				this.mSurfaceView.takePicture();
				break;
		}
		return false;
	}
	//返回
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {   
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
	    	if(mSurfaceView.isPreview()){
	    		this.finish();//退出
	    	}else{//非预览状态,则开启预览
	    		mSurfaceView.getCamera().startPreview();
	    		mSurfaceView.setPreview(true);
	    	}
	    	return true;   
	    }  
	   return super.onKeyDown(keyCode, event);   
	}  
	
	@Override
	protected void onStop() {
		super.onStop();
		if(this.mSurfaceView.isPreview()){
			Camera mCamera = this.mSurfaceView.getCamera();
			if(mCamera != null){
				mCamera.stopPreview();
			}
		}
		//恢复系统音量
		this.setStreamVolume(currentMediaVolume);
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		if(this.mSurfaceView.isPreview() == false){
			Camera mCamera = this.mSurfaceView.getCamera();{
				mCamera.startPreview();
			}
		}
		//静音
		this.setStreamVolume(0);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		this.setStreamVolume(currentMediaVolume);
	}
	
	//设置系统音量
	private void setStreamVolume(int index){
		audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, index,
				AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
	}
	
}
