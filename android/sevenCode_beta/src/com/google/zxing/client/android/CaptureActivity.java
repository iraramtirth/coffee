package com.google.zxing.client.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import coffee.seven.R;

import com.google.zxing.Result;
import com.google.zxing.client.android.camera.CUtils;
import com.google.zxing.client.android.result.ResultHandler;
import com.google.zxing.client.android.result.TextResultHandler;
import com.google.zxing.client.result.ResultParser;

/**
 * The barcode reader activity itself. This is loosely based on the
 * CameraPreview example included in the Android SDK.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends Activity implements
		SurfaceHolder.Callback {

	public static final String TAG = CaptureActivity.class.getSimpleName();

	public static CaptureActivity context;

	 
	private SurfaceView surfaceView;
	private ViewfinderView viewfinderView;
	private View resultView;

	private boolean hasSurface;

	
	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

 
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		context = this;
		setContentView(R.layout.scan_decode);

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		resultView = findViewById(R.id.result_view);
		resultView.setVisibility(View.GONE);
		
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		hasSurface = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (resultView.getVisibility() == View.VISIBLE) {
				resultView.setVisibility(View.GONE);
				viewfinderView.setVisibility(View.VISIBLE);
				CUtils.mHandler.sendEmptyMessage(R.id.restart_preview);
				return true;
			}else{
				CUtils.stopPreview();
				this.finish();
			}
		} 
		return super.onKeyDown(keyCode, event);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			//开启并初始化相关参数
			CUtils.openAndInitCamera(holder);
			CUtils.requestAutoFocusAndPreview();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
		CUtils.releaseCamera();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		CUtils.releaseCamera();
	}
	
	/**
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		ResultHandler resultHandler = new TextResultHandler(this,
				ResultParser.parseResult(rawResult), rawResult);
		 
		viewfinderView.setVisibility(View.GONE);
		resultView.setVisibility(View.VISIBLE);

		TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
		CharSequence displayContents = resultHandler.getDisplayContents();
		contentsTextView.setText(displayContents);
		// Crudely scale betweeen 22 and 32 -- bigger font for shorter text
		int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
		contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

	}

}
