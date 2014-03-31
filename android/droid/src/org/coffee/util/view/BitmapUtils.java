package org.coffee.util.view;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import coffee.frame.Config;

/**
 * 
 * @author coffee
 */
public class BitmapUtils {

	private final static String CACHE_DIR = Config.getCacheDir();

	/**
	 * 加载网络上的Bitmap
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap loadBitmapFromNet(String linkUrl) {
		if (linkUrl == null || "".equals(linkUrl.trim())) {
			return null;
		}
		Bitmap bitmap = null;
		try {
			linkUrl = linkUrl.trim();
			String fileName = getFileName(linkUrl);
			bitmap = loadBitmapFromLocal(fileName);
			if (bitmap != null) {
				return bitmap;
			}
			// StringBuilder fileData = new StringBuilder();
			// URL url = new URL(linkUrl);
			// URLConnection uc = url.openConnection();
			// uc.setConnectTimeout(1000 * 5);// 设置超时
			// 注意读取图片的时候 如果用BufferedReader 即字符流将会出现问题
			// BufferedInputStream in = new
			// BufferedInputStream(uc.getInputStream());
			// byte[] data = new byte[1024];
			// int len = 0;
			// while ((len = in.read(data) ) != -1) {
			// fileData.append(new String(data,0,len));
			// }
			// in.close();
			// byte[] allData = fileData.toString().getBytes();
			// bitmap = BitmapFactory.decodeByteArray(allData, 0,
			// allData.length);
			bitmap = BitmapFactory.decodeStream(new URL(linkUrl).openStream());
			// 缓存图片
			cacheBitmapToFile(bitmap, fileName);
		} catch (MalformedURLException e) {
			// e.printStackTrace();
		} catch (FileNotFoundException e) {
			Log.w("BitmapUtils", "FileNotFoundException" + e.getMessage(), null);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			Log.w("BitmapUtils", "", e);
		}
		return bitmap;
	}

	/**
	 * 缓存Bitmap 写入到本地
	 */
	public static String cacheBitmapToFile(Bitmap bitmap, String fileName) {
		String cacheFilepath = getCachePath(fileName);
		if (bitmap == null || cacheFilepath == null) {
			return null;
		}
		try {
			File file = new File(cacheFilepath);
			if (file.exists() == false) {
				if (file.getParentFile().exists()) {
					file.createNewFile();
				} else {
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
			}
			FileOutputStream fout = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
			fout.flush();
			fout.close();
			return file.getPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从本地读取文件
	 */
	public static Bitmap loadBitmapFromLocal(String fileName) {
		String cacheFilePath = getCachePath(fileName);
		if (cacheFilePath == null) {
			return null;
		}
		try {
			File file = new File(cacheFilePath);
			// 如果缓存图片存在，则返回
			if (file != null && file.exists()) {
				try {
					return BitmapFactory.decodeFile(file.getAbsolutePath());
				} catch (OutOfMemoryError oom) {
					Log.e("BitmapUtils", oom.getMessage(), oom);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 否则返回null
		return null;
	}

	public static void removeImageFromLocal(String uri) {
		String fileName = getFileName(uri);
		String cacheFilePath = getCachePath(fileName);
		if (cacheFilePath == null) {
			return;
		}
		try {
			File file = new File(cacheFilePath);
			// 如果缓存图片存在，则返回
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成的本地图片命名规则<br>
	 * http://....../--/xxx.jpg 只截取最后的部分 xxx.jpg
	 * 
	 * @param uri
	 * @return
	 */
	public static String getFileName(String uri) {
		if (uri == null) {
			return SystemClock.currentThreadTimeMillis() + "";
		}
		if (uri.contains("/") == false) {
			return Base64.encodeToString(uri.getBytes(), Base64.DEFAULT).trim();
		}
		String name = uri.substring(uri.lastIndexOf("/") + 1);
		return Base64.encodeToString(name.getBytes(), Base64.DEFAULT).trim();
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws NullPointerException
	 *             如果sd卡不可用, 返回null此时将会抛出异常<br>
	 */
	public static String getCachePath(String fileName) throws NullPointerException {
		File file = null;
		//String status = Environment.getExternalStorageState();
		// 判断是否有sdcard
		// if (status.equals(Environment.MEDIA_MOUNTED)) {
		file = new File(CACHE_DIR, fileName);
		return file.getAbsolutePath();
		// } else {
		// return null;// 没有sdcard
		// }
	}

	/**
	 * 
	 * @param bitmap
	 * @return
	 */
	public static String toBase64(Bitmap bitmap) {
		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return
	 */
	public static Bitmap base64ToBitmap(String base64Data) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 图片最大宽度
	 */
	private static final int IMAGE_WIDTH = 800;
	/**
	 * 图片最大高度
	 */
	private static final int IMAGE_HEIGHT = 480;

	public static SoftReference<Bitmap> getCompressedBitmapFromFile(String dstPath, int... widthAndHeight) {
		File dst = new File(dstPath);
		if (null != dst && dst.exists()) {
			int width = IMAGE_WIDTH;
			int height = IMAGE_HEIGHT;
			if (widthAndHeight.length == 2) {
				width = widthAndHeight[0];
				height = widthAndHeight[1];
			}
			BitmapFactory.Options opts = null;
			if (width > 0 && height > 0) {
				opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(dst.getPath(), opts);
				// 计算图片缩放比例
				final int minSideLength = Math.min(width, height);
				opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
				opts.inJustDecodeBounds = false;
				opts.inInputShareable = true;
				opts.inPurgeable = true;
			}
			try {
				SoftReference<Bitmap> bmpRef = new SoftReference<Bitmap>(BitmapFactory.decodeFile(dst.getPath(), opts));
				return bmpRef;
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}
		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/**
	 * 图像旋转
	 * 
	 * @param oriBmp
	 *            原始图片
	 * @param degree
	 *            正值为右旋转，负值为左旋转
	 * @return
	 */
	public static Bitmap toRotation(Bitmap oriBmp, float degree) {
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 缩放图片动作
		matrix.postScale(1.0F, 1.0F);
		// 旋转图片 动作
		matrix.postRotate(degree);
		// 创建新的图片
		oriBmp = Bitmap.createBitmap(oriBmp, 0, 0, oriBmp.getWidth(), oriBmp.getHeight(), matrix, true);

		// Bitmap bitmap = Bitmap.createBitmap(oriBmp.getWidth(),
		// oriBmp.getHeight(), android.graphics.Bitmap.Config.ARGB_8888); //
		// 背景图片
		// Canvas canvas = new Canvas(bitmap); // 新建画布
		// canvas.drawBitmap(oriBmp, 0, 0, null); // 画图片
		// canvas.save(Canvas.ALL_SAVE_FLAG); // 保存画布
		// canvas.restore();
		//
		// cacheBitmapToFile(bitmap, "111");
		return oriBmp;

	}
}
