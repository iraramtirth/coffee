package com.google.zxing.client.android.camera;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import coffee.code.R;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.PlanarYUVLuminanceSource;
import com.google.zxing.client.util.BitmapUtils;
import com.google.zxing.common.HybridBinarizer;

/**
 * 管理各个对象
 * 
 * @author coffee
 */
public class CUtils {

	private static final String TAG = CUtils.class.getSimpleName();
	
	private static final String BARCODE_BITMAP = "barcode_bitmap";
	
	public static Camera mCamera;
	public static Handler mHandler;
	public static CameraConfigurationManager configManager;
	
	public static AutoFocusCallback autoFocusCallback;
	public static PreviewCallback previewCallback;

	public static Rect framingRect = null;
	public static Rect framingRectInPreview = null;

	public static MultiFormatReader multiFormatReader;
	public static Hashtable<DecodeHintType, Object> hints;
	    
	private static final int MIN_FRAME_WIDTH = 120;
	private static final int MIN_FRAME_HEIGHT = 120;
	private static final int MAX_FRAME_WIDTH = 120;
	private static final int MAX_FRAME_HEIGHT = 120;

	public static boolean previewing = false;	//正在预览
	public static boolean canEncoding = true;	//是否允许解码
	
	static {
		configManager = new CameraConfigurationManager(CaptureActivity.context);
		mHandler = new Handler() {
			@Override
			public void handleMessage(final Message message) {
				switch (message.what) {
				case R.id.auto_focus:
					canEncoding = true;
					requestAutoFocusAndPreview();
					break;
				case R.id.restart_preview:
					canEncoding = true;
					requestAutoFocusAndPreview();
					break;
				case R.id.decode:
					canEncoding = false;
					decode((byte[]) message.obj, message.arg1, message.arg2);
					canEncoding = true;
				    break;
				case R.id.decode_succeeded:
					canEncoding = false;
					Bundle bundle = message.getData();
					Bitmap barcode = bundle == null ? null : (Bitmap) bundle
							.getParcelable(BARCODE_BITMAP);
					CaptureActivity.context.handleDecode((Result) message.obj,
							barcode);
					break;
				case R.id.decode_failed:
					//setOneShotPreviewCallback();
					break;
				}
			}
		};

		previewCallback = new PreviewCallback(configManager);
		autoFocusCallback = new AutoFocusCallback();

		multiFormatReader = new MultiFormatReader();
		hints = new Hashtable<DecodeHintType, Object>(3);

		Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
		decodeFormats.add(BarcodeFormat.QR_CODE);
	    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
	    
	    hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");  
	}
	 
	/**
	 * 开启相机并初始化相关参数
	 * 
	 * @param holder
	 */
	public static void openAndInitCamera(SurfaceHolder holder) {
		try {
			mCamera = Camera.open();
			mCamera.setPreviewDisplay(holder);
			configManager.initFromCameraParameters(mCamera);
			configManager.setDesiredCameraParameters(mCamera);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭相机
	 */
	public static void releaseCamera() {
		stopPreview();
		if(mCamera != null){
			mCamera.release();
		}
	}

	/**
	 * 开启预览
	 */
	public static void startPreview() {
		if (mCamera != null && !previewing) {
			mCamera.startPreview();
			previewing = true;
		}
	}

	/**
	 * 停止预览
	 */
	public static void stopPreview() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera = null;
		}
		previewing = false;
	}

	/**
	 * 请求聚焦
	 */
	public static void requestAutoFocusAndPreview() {
		try{
			if (mCamera != null) {
				if(!previewing){
					mCamera.startPreview();
					previewing = true;
				}
				mCamera.autoFocus(autoFocusCallback);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void setOneShotPreviewCallback() {
		if(previewing){
			mCamera.setOneShotPreviewCallback(previewCallback);
		}
	}

	/**
	 * Calculates the framing rect which the UI should draw to show the user
	 * where to place the barcode. This target helps with alignment as well as
	 * forces the user to hold the device far enough away to ensure the image
	 * will be in focus.
	 * 
	 * @return The rectangle to draw on screen in window coordinates.
	 */
	public static Rect getFramingRect() {
		if (framingRect == null) {
			if (mCamera == null) {
				return null;
			}
			Point screenResolution = configManager.getScreenResolution();
			int width = screenResolution.x * 3 / 4;
			if (width < MIN_FRAME_WIDTH) {
				width = MIN_FRAME_WIDTH;
			} else if (width > MAX_FRAME_WIDTH) {
				width = MAX_FRAME_WIDTH;
			}
			int height = screenResolution.y * 3 / 4;
			if (height < MIN_FRAME_HEIGHT) {
				height = MIN_FRAME_HEIGHT;
			} else if (height > MAX_FRAME_HEIGHT) {
				height = MAX_FRAME_HEIGHT;
			}
			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			framingRect = new Rect(leftOffset, topOffset, leftOffset + width,
					topOffset + height);
		}
		return framingRect;
	}

	/**
	 * Like {@link #getFramingRect} but coordinates are in terms of the preview
	 * frame, not UI / screen.
	 */
	public static Rect getFramingRectInPreview() {
		if (framingRectInPreview == null) {
			Rect rect = new Rect(getFramingRect());
			Point cameraResolution = configManager.getCameraResolution();
			Point screenResolution = configManager.getScreenResolution();
			rect.left = rect.left * cameraResolution.x / screenResolution.x;
			rect.right = rect.right * cameraResolution.x / screenResolution.x;
			rect.top = rect.top * cameraResolution.y / screenResolution.y;
			rect.bottom = rect.bottom * cameraResolution.y / screenResolution.y;
			framingRectInPreview = rect;
		}
		return framingRectInPreview;
	}

	public static PlanarYUVLuminanceSource buildLuminanceSource(byte[] data,
			int width, int height) {
		Rect rect = getFramingRectInPreview();
		int previewFormat = configManager.getPreviewFormat();
		String previewFormatString = configManager.getPreviewFormatString();

		switch (previewFormat) {
		case PixelFormat.YCbCr_420_SP:
		case PixelFormat.YCbCr_422_SP:
			//获取一个指定大小的灰度图
			return new PlanarYUVLuminanceSource(data, width, height, rect.left,
					rect.top, rect.width(), rect.height(), false);
		default:
			if ("yuv420p".equals(previewFormatString)) {
				return new PlanarYUVLuminanceSource(data, width, height,
						rect.left, rect.top, rect.width(), rect.height(), false);
			}
		}
		throw new IllegalArgumentException("Unsupported picture format: "
				+ previewFormat + '/' + previewFormatString);
	}

	/**
	 * @param data ： 图像数据
	 * @param width : 宽度  320 [注意此参数指的是langscape模式下的值]
	 * @param height ： 高度 240 
	 */
	private static void decode(byte[] data, int width, int height) {
		System.out.println("正在解码....");
	    long start = System.currentTimeMillis();
	    Result rawResult = null;
	    PlanarYUVLuminanceSource source = CUtils.buildLuminanceSource(data, width, height);
	    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
	    
	    try {
	      rawResult = multiFormatReader.decode(bitmap, hints);
	      //解码正确
	      BitmapUtils.cacheBitmap(source.renderCroppedGreyscaleBitmap(), 
		    		width + "x" + height + "-" +  System.currentTimeMillis());
	    } catch (ReaderException re) {
	      // continue
	    } finally {
	      multiFormatReader.reset();
	    }
	    System.out.println("解码结束....");
	    if (rawResult != null) {
	      // Don't log the barcode contents for security.
	      long end = System.currentTimeMillis();
	      Log.d(TAG, "Found barcode in " + (end - start) + " ms");
	      Message message = Message.obtain(CUtils.mHandler, R.id.decode_succeeded, rawResult);
	      Bundle bundle = new Bundle();
	      bundle.putParcelable(BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
	      message.setData(bundle);
	      message.sendToTarget();
	    } else {
	      Message message = Message.obtain(CUtils.mHandler, R.id.decode_failed);
	      message.sendToTarget();
	    }
	  }

}
