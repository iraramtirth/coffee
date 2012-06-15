package org.coffee.util.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * 
 * @author coffee
 */
public class BitmapUtils {
	
	private final static String CACHE_DIR = Environment.getExternalStorageDirectory() + "/cache"; 
	/**
	 * 加载网络上的Bitmap
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap loadBitmapFromNet(String linkUrl) {
		Bitmap bitmap = null;
		try {
			linkUrl = linkUrl.trim();
			String fileName = linkUrl.substring(linkUrl.lastIndexOf("/")+1);
			bitmap = loadBitmapFromLocal(fileName);
			if(bitmap != null){
				return bitmap;
			}
//			StringBuilder fileData = new StringBuilder();
//			URL url = new URL(linkUrl);
//			URLConnection uc = url.openConnection();
//			uc.setConnectTimeout(1000 * 5);// 设置超时
			//注意读取图片的时候 如果用BufferedReader 即字符流将会出现问题
//			BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
//			byte[] data = new byte[1024];
//			int len = 0;
//			while ((len = in.read(data) ) != -1) {
//				fileData.append(new String(data,0,len));
//			}
//			in.close();
//			byte[] allData = fileData.toString().getBytes();
//			bitmap = BitmapFactory.decodeByteArray(allData, 0, allData.length);
			bitmap = BitmapFactory.decodeStream(new URL(linkUrl).openStream());
			//缓存图片
			cacheBitmapToFile(bitmap, fileName);
		} catch (MalformedURLException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			Log.e("xxx", e.getMessage());
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	/**
	 * 缓存Bitmap
	 * 写入到本地
	 */
	public static void cacheBitmapToFile(Bitmap bitmap, String fileName){
		if(bitmap == null){
			return;
		}
		File file = new File(getCachePath(fileName));
		try {
			if(file.exists() == false){
				if(file.getParentFile().exists()){
					file.createNewFile();
				}else{
					file.getParentFile().mkdirs();
					file.createNewFile();
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
	}

	/**
	 * 从本地读取文件 
	 */
	public static Bitmap loadBitmapFromLocal(String fileName){
		File file =  new File(getCachePath(fileName));
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
}
