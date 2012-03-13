package coffee.code.activity.base;

import coffee.browser.MmbHttpUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

/**
 * 浏览器父类 
 * @author wangtao
 */
public abstract class BrowserActivity extends BaseActivity{
	
	protected BrowserActivity context;
	/**
	 * 页面加载动画图
	 */
	protected ImageView mProgressImage;
	/**
	 * 动画背景图
	 */
	protected int[] mProgressDrawable;
	/**
	 * 切换动画与mWebView的Switcher
	 */
	protected ViewSwitcher mViewSwitcher;
	
	/**
	 * 浏览器组件
	 */
	protected WebView mWebView;
	protected WebSettings webSettings;
	
	protected ProgressHandler mProgressHandler;
	protected ProgressTheread mProgressTheread;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		super.onCreate(savedInstanceState);
		mProgressHandler = new ProgressHandler();
		mProgressTheread = new ProgressTheread();
		initProgress(this);
	}
	/**
	 * 后退
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event){
		switch(event.getKeyCode()){
			case KeyEvent.KEYCODE_BACK:
				if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
					if(mWebView.canGoBack()){
						mWebView.goBack();
					}else{//退出系统
//						MmbSystem.exit(context);
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
	
	protected void loadUrl(String url) {
		webSettings = mWebView.getSettings();
		//开启js支持， 否则无法单击网页中的button。 即无法提交表单等操作
		webSettings.setJavaScriptEnabled(true);
		MmbHttpUtils.loadUrl(context, mWebView, url);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, final String url) {
				//异步加载网页
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						MmbHttpUtils.loadUrl(context, mWebView, url);
						return null;
					}
				}.execute();
				return true;
			}
			/**
			 * page加载完成后，设置web.requestFocus(); 
			 * 否则webView中的控件无法获取焦点
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				/**
				 * 如果页面加载失败， 将不会调用该方法。 故而必须要写 {@link BrowserActivity#onDestroy} 方法
				 */
				if(mProgressHandler != null && mProgressTheread != null){
					mProgressHandler.removeCallbacks(mProgressTheread);
					mProgressHandler = null;
					mProgressTheread = null;
				}
				//显示页面内容
				mViewSwitcher.setDisplayedChild(1);
			}
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
	}
	
	/**
	 * 初始化Progress相关数据
	 * {@link BrowserActivity#initProgress(Context)}
	 * 主要用来初始化： 
	 * mProgressImage、mProgressImage、mViewSwitcher三个数字段
	 */
	protected abstract void initProgress(Context baseContext);
	
	protected class ProgressHandler extends Handler{
		
		@Override
		public void handleMessage(Message msg) {
			mProgressImage.setImageResource(msg.what);
		}
	}
	
	protected class ProgressTheread implements Runnable {
		private int i;
		@Override
		public void run() {
			if(mProgressHandler != null){
				mProgressHandler.sendEmptyMessage(mProgressDrawable[i]);
				i++;
				if(i >= 16){
					i = 0;
				}
				//一毫秒更新一次
				mProgressHandler.postDelayed(this, 100);
			}
		}
	}; 
	
	/**
	 * 销魂对象
	 * 如果页面加载不成功的话， 将不会调用 {@link WebViewClient#onPageFinished} 
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mProgressHandler != null && mProgressTheread != null){
			mProgressHandler.removeCallbacks(mProgressTheread);
			mProgressHandler = null;
			mProgressTheread = null;
		}
	}
}
