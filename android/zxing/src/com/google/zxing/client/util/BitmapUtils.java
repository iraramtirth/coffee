package com.google.zxing.client.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

/**
 * @author coffee
 */
public class BitmapUtils {
	
	private final static String CACHE_DIR = Environment.getExternalStorageDirectory() + "/cache"; 
	
	/**
	 * 缓存Bitmap
	 * 写入到本地
	 * @return : 返回本地缓存文件（完整）路径
	 * 		//没有存储卡 则返回null
	 */
	public static String cacheBitmap(Bitmap bitmap, String fileName){
		if(bitmap == null){
			return null;
		}
		String fileWholeName = getCachePath(fileName);
		if(fileWholeName == null){
			//没有存储卡
			return null;
		}
		File file = new File(fileWholeName);
		try {
			if(file.exists() == false){
				if(file.getParentFile().exists() == false){
					file.getParentFile().mkdirs();
				} 
			}
			FileOutputStream fout = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
			fout.flush();
			fout.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getPath();
	}

	/**
	 * 从本地读取文件 
	 */
	public static Bitmap loadBitmapFromLocal(String localPath){
		File file =  new File(localPath);
		//如果缓存图片存在，则返回
		if(file.exists()){
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		//否则返回null
		return null;
	}
	/**
	 * 获取缓存路径
	 */
	private static String getCachePath(String fileName){
		File file = null;
		String status = Environment.getExternalStorageState();
		//判断是否有sdcard
		if(status.equals(Environment.MEDIA_MOUNTED)){
			file = new File(CACHE_DIR, fileName); 
			return file.getAbsolutePath();
		}else{
			return null;// 没有sdcard
		}
		 
	}
	
//	/**
//	 * 异步加载图片
//	 * @param view ： 远程图片加载完成后所附的对象, 
//	 * @param imgNetUrl : 远程图片的链接地址
//	 * @return : 返回图片加载完成后保存在本地的图片地址
//	 */
//	public static String  asyncLoadImage(final View view, final String imgNetUrl){
//		
//		final String fileName = imgNetUrl.substring(imgNetUrl.lastIndexOf("/")+1);
//		final Object imgLocalUrl = null;
//		new AsyncTask<String, Void, Bitmap>() {
//			@SuppressWarnings("unused")
//			private Object tmp = imgLocalUrl;
//			/**
//			 * 传入远程图片的url
//			 */
//			@Override
//			protected Bitmap doInBackground(String... params) {
//				String netUrl = params[0];
//				//从远程加载
//				Bitmap bmp = BitmapUtils.loadBitmapFromNet(netUrl);
//				if(bmp != null){
//					//缓存到本地
//					tmp = BitmapUtils.cacheBitmap(bmp, fileName) + "";
//				}
//				return bmp;
//			}
//			@Override
//			protected void onPostExecute(Bitmap bitmap) {
//				if(bitmap != null){
//					if(view instanceof ImageView){
//						((ImageView)view).setImageBitmap(bitmap);
//					}else{
//						//
//						BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
//						view.setBackgroundDrawable(bitmapDrawable);
//					}
//				}
//			}
//		}.execute(imgNetUrl);
//		
//		return imgLocalUrl + "";
//	}
}
