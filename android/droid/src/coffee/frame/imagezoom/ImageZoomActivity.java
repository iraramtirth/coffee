package coffee.frame.imagezoom;

import java.lang.ref.SoftReference;

import org.coffee.R;
import org.coffee.util.view.BitmapUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ZoomControls;
import coffee.frame.activity.base.BaseActivity;

/**
 * 图片(缩放)查看器
 * 
 * @author coffee<br>
 *         2013下午3:23:02
 */
public class ImageZoomActivity extends BaseActivity {

	private ImageZoomView mZoomView;
	private ImageZoomState mZoomState;
	private ImageZoomListener mZoomListener;

	private LinearLayout mDownloadNotice;
	// private TextView mProgressText;
	private ProgressBar mProgressBar;
	private ZoomControls zoomCrl;

	// 文件名()
	private String mFileName;
	// 文件路径
	private String mFilePath;

	/**
	 * 
	 * [查看图片]<BR>
	 * [功能详细描述]
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.common_image_zoom);
		Intent intent = getIntent();
		mFileName = intent.getStringExtra("");
		mFilePath = mFileName;
		// 显示下载进度
		loadFromLocalOrNet(mFilePath);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void findViewById() {
		mZoomView = (ImageZoomView) findViewById(R.id.zoom_image);
		mDownloadNotice = (LinearLayout) findViewById(R.id.zoom_download_notice);
		// mSymbol = (ImageView) findViewById(R.id.symbol);
		// mProgressText = (TextView) findViewById(R.id.progress);
		mProgressBar = (ProgressBar) findViewById(R.id.zoom_progress_bar);
		zoomCrl = (ZoomControls) findViewById(R.id.zoom_controls);
		zoomCrl.hide();
		mDownloadNotice.setVisibility(View.VISIBLE);
		mProgressBar.setProgress(0);
		// 保存图片
		findViewById(R.id.zoom_save).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String info = "";
				// 还在下载中
				if (mDownloadNotice.getVisibility() == View.VISIBLE) {
					info = "图片下载中";
				} else {
					info = "图片保存在" + mFilePath + ".jpg";
					///File file = new File(mFilePath);
					//file.renameTo(new File(mFilePath + ".jpg"));
					//MsgLogic.getInstance().updateImageSave(mFileName);
					//FileUtils.copy(mFilePath, mFilePath + ".jpg");
				}
				Toast.makeText(ImageZoomActivity.this, info, Toast.LENGTH_LONG)
						.show();
			}
		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}

	/**
	 * 相对路径
	 * 
	 * @param uri
	 */
	private void loadFromLocalOrNet(final String uri) {
		// 异步加载网页
		new AsyncTask<Void, Void, Bitmap>() {
			@Override
			protected Bitmap doInBackground(Void... params) {
				Bitmap bmp = null;
				if (uri.startsWith("http")) {
					bmp = BitmapUtils.loadBitmapFromNet(uri);
					mFilePath = BitmapUtils.getCachePath(uri.substring(uri
							.lastIndexOf("/") + 1));
				} else {
					bmp = BitmapUtils.loadBitmapFromLocal(uri);
					mFilePath = BitmapUtils.getCachePath(uri);
				}
				return bmp;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (result != null) {
					loadImage(result);
				}
			}
		}.execute();
	}

	private void loadImage(Bitmap bmp) {
		mDownloadNotice.setVisibility(View.GONE);
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPurgeable = true;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		if (bmp != null) {
			SoftReference<Bitmap> bmpRef = new SoftReference<Bitmap>(bmp);
			if (bmpRef != null && bmpRef.get() != null) {
				mZoomView.setImage(bmpRef.get());
			}
			mZoomState = new ImageZoomState();
			mZoomView.setZoomState(mZoomState);
			mZoomListener = new ImageZoomListener();
			mZoomListener.setZoomState(mZoomState);
			mZoomListener.setHideOrShow(zoomCrl);
			mZoomView.setOnTouchListener(mZoomListener);
			mZoomView.setVisibility(View.VISIBLE);
			zoomCrl.show();

			// 放大
			zoomCrl.setOnZoomInClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mZoomState != null) {
						mZoomState.setZoomInOnce();
					}
				}
			});
			zoomCrl.setOnZoomOutClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mZoomState != null) {
						mZoomState.setZoomOutOnce();
					}
				}
			});
			resetZoomState(bmp);
		} else {
			Toast.makeText(ImageZoomActivity.this, "图片加载失败", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void resetZoomState(Bitmap bitmap) {
		mZoomState.setPanX(0.5f);
		mZoomState.setPanY(0.5f);
		mZoomState.setZoom(1.0f);
		mZoomState.notifyObservers();
	}

}
