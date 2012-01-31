/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
		setContentView(R.layout.capture);

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

		ImageView barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view);
		if (barcode == null) {
			barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.launcher_icon));
		} else {
			barcodeImageView.setImageBitmap(barcode);
		}

		TextView formatTextView = (TextView) findViewById(R.id.format_text_view);
		formatTextView.setText(rawResult.getBarcodeFormat().toString());

		TextView typeTextView = (TextView) findViewById(R.id.type_text_view);
		typeTextView.setText(resultHandler.getType().toString());

		TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
		CharSequence displayContents = resultHandler.getDisplayContents();
		contentsTextView.setText(displayContents);
		// Crudely scale betweeen 22 and 32 -- bigger font for shorter text
		int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
		contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

	}

}
