package coffee.frame.activity;

import java.util.ArrayList;

import org.coffee.R;
import org.coffee.browser.activity.BrowserActivity;
import org.coffee.util.lang.ObjectSerialize;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;

public class Game2048Activity extends BrowserActivity {

	// 保存进度, 读取历史
	private Button saveBtn, loadBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		super.mWebView.addJavascriptInterface(new Object() {
			@SuppressWarnings({ "unused", "unchecked" })
			// @JavascriptInterface
			public void save(String gameData) {
				// 每一列的数据有,;分割
				// 4,4,4,4,;4,4,4,4,;0,0,0,0,;0,0,2,0,;
				ObjectSerialize.append(gameData);
				System.out.println(gameData);
				ArrayList<String> items = ObjectSerialize.read(ArrayList.class, String.class);
				for (String item : items) {
					System.out.println(items);
				}
			}
		}, "android");
	}

	protected WebView createWebView() {
		setContentView(R.layout.game2048);
		WebView webView = (WebView) findViewById(R.id.webview);
		this.saveBtn = (Button) findViewById(R.id.history_save);
		this.loadBtn = (Button) findViewById(R.id.history_load);
		this.saveBtn.setOnClickListener(clickListener);
		this.loadBtn.setOnClickListener(clickListener);
		return webView;
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.history_save:
				mWebView.loadUrl("javascript:saveHistory();");
				break;
			case R.id.history_load:
				Intent intent = new Intent(context, Game2048HistoryActivity.class);
				startActivityForResult(intent, 100);
				break;
			default:
				break;
			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && data != null) {
			String item = data.getStringExtra("data");
			//Alert.toast(item);
			mWebView.loadUrl("javascript:loadHistory('" + item + "');");
		}
	}
}
