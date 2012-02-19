package coffee.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import coffee.browser.BrowserHistory;
import coffee.browser.MmbHttpUtils;
import coffee.util.window.TitleUtils;
/**
 * 需要
 * @author coffee
 */
public class BrowserActivity extends Activity implements OnClickListener, OnFocusChangeListener{

	private BrowserActivity thiz = this;
	private BrowserActivity context = this;
	
	private WebView mWebView;
	private WebSettings webSettings;
	private EditText searchEdit;
	private Button searchBtn;
	private static String HOST = "http://mmb.cn";
	private String title;
	private TextView navBack;	//导航， 后退
	private TextView navForward;//导航，前进
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		this.setContentView(R.layout.browser);
		searchEdit = (EditText) this.findViewById(R.id.home_edit_url);
		searchEdit.setOnClickListener(this);
		searchEdit.setOnFocusChangeListener(this);
		searchEdit.setVisibility(View.GONE);
		
		searchBtn = (Button) this.findViewById(R.id.home_btn_search);
		searchBtn.setOnClickListener(this);
		searchBtn.setVisibility(View.GONE);
		
		//创建对象
		mWebView = (WebView) this.findViewById(R.id.home_web);
		webSettings = mWebView.getSettings();
		
		navBack = (TextView) this.findViewById(R.id.home_nav_back);
		navForward = (TextView) this.findViewById(R.id.home_nav_forward);
		navBack.setOnClickListener(this);
		navForward.setOnClickListener(this);
		
		//开启js支持， 否则无法单击网页中的button。 即无法提交表单等操作
		webSettings.setJavaScriptEnabled(true);
		String url = "http://mmb.cn";
		title = MmbHttpUtils.loadUrl(context, HOST, url);
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, final String url) {
				/**
				 * 异步加载网页
				 */
				new AsyncTask<Void, Void, String>() {
					@Override
					protected String doInBackground(Void... params) {
						title = MmbHttpUtils.loadUrl(context, HOST, url);
						return title;
					}
					@Override
					protected void onPostExecute(String title) {
						thiz.setTitle(title);
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
				//mWebView.requestFocus();// 获取焦点即可
				TitleUtils.setTitleProgressHidden(context);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		//设置标题
//		this.setTitle(title);
	  //HandlerThread handlerThared = new HandlerThread("new Thread");  
	  //handlerThared.start();//开启一个新的线程，脱离UI(主)线程  
//		new Handler().postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				//requestWindowFeature(Window.FEATURE_PROGRESS);
//				int titleId = RUtils.getResId("com.android.internal.R", "id", "title");
//				TextView titleView = (TextView) thiz.getWindow().findViewById(titleId);
//				titleView.setText("xxx");
//				System.out.println(titleView);
//			}
//		}, 1000 * 5);
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.onClick(navBack);
		}
		if(keyCode == KeyEvent.KEYCODE_ENTER){//回车
			this.onClick(searchBtn);
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if(v == searchBtn){
			//访问指定url的链接
			String url = searchEdit.getText().toString();
			if(url.trim().length() > 0){
				title = MmbHttpUtils.loadUrl(context, url, url);
				HOST = url;//设置主机
				thiz.setTitle(title);
			}	
		}
		//导航
		if(v == navBack || v == navForward){
			String linkUrl = "";
			if(v == navBack){
				linkUrl = BrowserHistory.goBack();
			}else{
				linkUrl = BrowserHistory.goForward();
			}
			if(linkUrl == null){
				return;
			}
			String doc = BrowserHistory.browser(linkUrl);
			String mimeType = "text/html";
			if(linkUrl.matches(".+?\\.(jpg|gif|jpeg|png)+.*?")){
				mimeType = null;
			}
			this.mWebView.loadDataWithBaseURL(linkUrl, doc, mimeType, doc, linkUrl);
		}
	}
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if(v == searchEdit){
			if(hasFocus){
				searchEdit.selectAll();
			}
		}
	}
	
	//getter
	public WebView getWebView(){
		return this.mWebView;
	}

}
