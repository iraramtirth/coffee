package org.coffee.camera.view;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.coffee.common.util.FileUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView {
	private Camera mCamera;
//	private Context context;
	//当前Camera状态：预览|非预览
	private boolean isPreview = false;
	public MySurfaceView(Context context) {
		super(context);
		this.getHolder().addCallback(new MyCallBack());
		// 表明该Surface不包含原生数据，Surface用到的数据由其他对象提供
		// 如果不设置, 或者设置成其他数据则不能预览
		this.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		this.context = context;
	}

	/**
	 * 当开始拍照时，会依次调用shutter的onShutter()方法
	 * raw的onPictureTaken方法,jpeg的onPictureTaken方法.
	 * 三个参数的作用是shutter--拍照瞬间调用，raw--获得没有压缩过的图片数据，jpeg---返回jpeg的图片数据
	 * 当你不需要对照片进行处理，可以直接用null代替.
	 */
	public void takePicture() {
		int i = 0;
		while(true){
			if(this.isPreview){
				//拍照
				this.mCamera.takePicture(null, null, jpeg);
				//接收完图片之后系统会执行stopPreview
				this.isPreview = false;
			}else{//开启预览
				this.mCamera.startPreview();
				this.isPreview = true;
			}
			try {
				Thread.sleep(1000 * 1);
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;//结束循环
			}
			i++;
			if(i == 10){
				break;
			}
		}
	}

	// 返回jpeg的图片数据
	private PictureCallback jpeg = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap srcBm = BitmapFactory.decodeByteArray(data, 0, data.length);
			try {
				//加水印
				Bitmap newBm = Bitmap.createBitmap(srcBm);
				Canvas c = new Canvas(newBm);
				Paint paint = new Paint();
				paint.setColor(Color.RED);	
				paint.setTextSize(15);
				c.drawText("Spring Coffee", newBm.getWidth()-145, newBm.getHeight()-45, paint);
				c.save( Canvas.ALL_SAVE_FLAG );//保存
				c.restore();//存储

				//将加水印的图片保存到/sdcard
				File file = new File("/sdcard/Carema/"
						+ System.currentTimeMillis() + ".jpg");
				if (file.exists() == false) {
					FileUtils.createNewFileOrDirectory(file);
				}
				BufferedOutputStream bos = new BufferedOutputStream(
						new FileOutputStream(file));
				newBm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
				bos.flush();
				bos.close();
			} catch (IOException e) {
				Log.e("xx", e.getMessage());
			}
		}
	};

	public Camera getCamera() {
		return mCamera;
	}

	public boolean isPreview(){
		return isPreview;
	}
	public void setPreview(Boolean isPreview){
		this.isPreview = isPreview;
	}
	
	class MyCallBack implements SurfaceHolder.Callback {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera = Camera.open();
				//设置预览面板
				mCamera.setPreviewDisplay(holder);
				//设置ErrorCallback
				mCamera.setErrorCallback(new ErrorCallback() {
					@Override
					public void onError(int error, Camera camera) {
						mCamera.release();
					}
				});
				//开启预览
				mCamera.startPreview();
				isPreview = true;
			} catch (IOException e) {
				Log.e("x", e.getMessage());
			}
		}

		/**
		 * This is called immediately after any structural changes (format or
		 * size) have been made to the surface.
		 */
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
				// 程序第一次启动的时候，调用该方法
				if(isPreview == false){
					
					isPreview = true;
				}
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			mCamera.stopPreview();// 停止预览
			mCamera.release(); // 释放资源
			mCamera = null;
		}
	}
}
