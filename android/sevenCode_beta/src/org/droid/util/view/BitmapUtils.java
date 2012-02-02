package org.droid.util.view;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.droid.util.http.HttpClient;
import org.droid.util.lang.FileUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import coffee.seven.App;
import coffee.seven.action.BitmapManager;

/**
 * @author wangtao
 */
public class BitmapUtils {
	private final static String TAG = BitmapUtils.class.getSimpleName();

	/**
	 * 加载网络上的Bitmap 调用该方法前：先检测该图片是否存在. //注意读取图片的时候 如果用BufferedReader
	 * 即字符流将会出现问题(改为字节流问题解决) //BufferedInputStream in = new
	 * BufferedInputStream(uc.getInputStream());
	 * @param url
	 */
 
	
	public static Bitmap loadBitmapFromNet(String linkUrl){
		Bitmap bmp = null;
		try {
			Log.d(TAG, "【远程】加载..." + linkUrl);
			linkUrl = linkUrl.trim();
			URL url = new URL(linkUrl);
			if (url != null) {
				//将URL下载到本地
				String fileName = linkUrl.substring((linkUrl.lastIndexOf("/")+1));
				String fileWholePath = getCachePath(fileName);
				BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(new File(fileWholePath)));
				byte[] data = (byte[]) new HttpClient().get(linkUrl, 1);
				if(data != null){
					fout.write(data);
					fout.flush(); //放在while循环的外面
					fout.close();
					bmp = loadBitmapFromLocal(fileWholePath, false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return bmp;
	}

	 
	/**
	 * 从本地读取文件 param isCache : 是否缓存bitmap到BitmapManager中
	 * 
	 * @return : 如果本地图片不存在。则返回null
	 */
	public static Bitmap loadBitmapFromLocal(String localPath, boolean isCache) {
		if(localPath == null){
			return null;
		}
		File file = new File(localPath);
		// 如果缓存图片存在，则返回
		if (file.exists()) {
			Log.d(TAG, "【本地】加载..." + localPath);
			if (BitmapManager.containsKey(localPath)) {
				return BitmapManager.get(localPath);
			} else {
				try {
					String path = file.getAbsolutePath();
					Bitmap bmp = BitmapFactory.decodeFile(path);
					if (isCache && bmp != null) {
						BitmapManager.put(localPath, bmp);
					}
					return bmp;
				} catch (OutOfMemoryError err) {
					err.printStackTrace();
				}
			}
		}
		return null;
	}

	 
	/**
	 * 缓存Bitmap 写入到本地
	 * 
	 * @return : 返回本地缓存文件（完整）路径 //没有存储卡 则返回null
	 */
	public static String cacheBitmap(Bitmap bitmap, String fileName) {
		if (bitmap == null) {
			return null;
		}
		String fileWholeName = getCachePath(fileName);
		if (fileWholeName == null) {
			// 没有存储卡
			return null;
		}
		File file = new File(fileWholeName);
		try {
			if (file.exists() == false) {
				if (file.getParentFile().exists()) {
					file.createNewFile();
				} else {
					FileUtils.createNewFileOrDir(file.getPath());
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
	 * 获取缓存路径
	 */
	public static String getCachePath(String fileName) {
		return getCacheDir() + "/" + fileName; 
	}

	public static String getCacheDir() {
		/**
		 * cacheDir的返回值 /data/data/com.mmb.shop/app_cache
		 */
		File cacheDir = App.context.getDir("cache", 0);
		String path = "/data/data/com.mmb.shop/cache";
		try {
			path = cacheDir.getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	/**
	 * 加水印
	 * 
	 * @param src
	 * @param attach
	 * @return
	 */
	public static Bitmap createBitmap(Bitmap src, Bitmap attach) {
		if (src == null) {
			return null;
		}
		int w = src.getWidth();
		int h = src.getHeight();
		// create the new blank bitmap
		Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图

		Canvas cv = new Canvas(newb);
		// draw src into
		cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
		// draw watermark into
		if (attach != null) {
			cv.drawBitmap(attach, w - attach.getWidth(),
					h - attach.getHeight(), null);// 在src的右下角画入水印
		}
		// save all clip
		cv.save(Canvas.ALL_SAVE_FLAG);// 保存
		// store
		cv.restore();// 存储
		return newb;
	}

	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public static byte[] Bitmap2Bytes(Drawable drawable) {
		Bitmap bm = drawable2Bitmap(drawable);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}
}
