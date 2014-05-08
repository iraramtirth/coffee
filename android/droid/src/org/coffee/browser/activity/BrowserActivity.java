package org.coffee.browser.activity;

import org.coffee.browser.WebViewUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 浏览器父类
 * 
 * @author coffee
 */
@SuppressLint("SetJavaScriptEnabled")
public class BrowserActivity extends Activity {

	protected BrowserActivity context;

	protected WebView mWebView;
	protected WebSettings mWebSettings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_PROGRESS);
		this.setProgressBarVisibility(true);

		this.mWebView = createWebView();

		mWebSettings = mWebView.getSettings();
		// 开启js支持， 否则无法单击网页中的button。 即无法提交表单等操作
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		mWebSettings.setSupportZoom(true);
		mWebSettings.setBuiltInZoomControls(true);
		mWebSettings.setUseWideViewPort(true);// 设置无限缩放、
		mWebSettings.setDefaultTextEncodingName("gbk");
		mWebView.setInitialScale(50);
		// mWebView.addJavascriptInterface(obj, interfaceName)

		String url = "http://wap.baidu.com";
		url = "http://a.hiphotos.baidu.com/album/w%3D2048/sign=0a938b00d53f8794d3ff4f2ee6230cf4/faedab64034f78f06fe0f24b78310a55b2191c9a.jpg";
		url = "file:///android_asset/2048/index.html";
//		url = "file:///android_asset/test.html";
		loadUrl(url);

	}

	protected WebView createWebView() {
		WebView webView = new WebView(this);
		this.setContentView(webView);
		return webView;
	}

	/**
	 * 后退
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		switch (event.getKeyCode()) {
		case KeyEvent.KEYCODE_BACK:
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
				if (mWebView.canGoBack()) {
					mWebView.goBack();
				} else {// 退出系统
					this.setResult(0);
					onBackPressed();
				}
				return false;
			}
		case KeyEvent.ACTION_UP:
			break;
		}
		return true;
	}

	public WebView getWebView() {
		return mWebView;
	}

	protected void loadUrl(String url) {
		WebViewUtils.loadUrl(context, url);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				context.setProgress(progress * 100);
				if (progress == 100) {
					// context.setTitle(".....");
				}
			}
		});

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, final String url) {
				if (url.startsWith("http") == false) {
					//mWebView.loadUrl(url);
					return super.shouldOverrideUrlLoading(view, url);
				} else {
					// 异步加载网页
					new AsyncTask<Void, Void, Void>() {
						@Override
						protected Void doInBackground(Void... params) {
							WebViewUtils.loadUrl(context, url);
							return null;
						}
					}.execute();
					return true;
				}
			}
			/**
			 * page加载完成后，设置web.requestFocus(); 否则webView中的控件无法获取焦点
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				/**
				 * 如果页面加载失败， 将不会调用该方法。
				 */
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
	}

	/**
	 * 销魂对象 如果页面加载不成功的话， 将不会调用 {@link WebViewClient#onPageFinished}
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
