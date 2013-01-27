package org.coffee.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

public class SoftActivity extends Activity{
	String TAG = "SoftActivity"; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int i = 0;
		try{
			for(i = 0; i < 10000; i++){
				Bitmap bmp = createBitmap();
//				if(!bmp.isRecycled()){
//					//
//				}
				Log.e(TAG, "i == " + i);
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally{
			Log.e(TAG, "i == " + i);
		}
	}
	
	public Bitmap createBitmap(){
		Bitmap bmp = null;
		//bmp = BitmapFactory.decodeResource(getResources(), R.drawable.lin);
		//bmp = BitmapFactory.decodeStream(getResources().openRawResource(R.raw.lin));
		try {
//			bmp = BitmapFactory.decodeStream(getAssets().open("lin.jpg"));
			bmp = BitmapFactory.decodeStream(new FileInputStream(new File("/mnt/sdcard/test.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmp;
	}
}
