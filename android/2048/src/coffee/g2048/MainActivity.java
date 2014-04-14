package coffee.g2048;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends Activity {
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mWebView = new WebView(this);
		this.setContentView(mWebView);
		String url = "file:///android_asset/2048/index.html";
		mWebView.loadUrl(url);

		//
		WebSettings mWebSettings = mWebView.getSettings();
		// 开启js支持， 否则无法单击网页中的button。 即无法提交表单等操作
		mWebSettings.setJavaScriptEnabled(true);
	}

	@Override
	public void onBackPressed() {
		Intent home = new Intent(Intent.ACTION_MAIN);
		home.addCategory(Intent.CATEGORY_HOME);
		startActivity(home);
	}
}
