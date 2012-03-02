package coffee.code.activity;



import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ViewSwitcher;
import coffee.seven.R;
import coffee.seven.activity.base.BrowserActivity;
import coffee.seven.activity.base.IActivity;
import coffee.util.RUtils;

public class MmbBrowserActivity extends BrowserActivity{

	
	@Override
	public void onCreate(Bundle mBundle) {
	
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, R.layout.mmb_browser);
		super.onCreate(bundle);
		
		final String url = this.getIntent().getStringExtra(IActivity.KEY_WEBVIEW_URL);
		
		mProgressHandler.postDelayed(mProgressTheread, 1000 * 1/3);
		
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				loadUrl(url);
				return null;
			}
		}.execute();
	}
	
	@Override
	protected void initProgress(Context baseContext) {
		super.mProgressImage = (ImageView) this.findViewById(R.id.browser_progress);
		
		mProgressDrawable = new int[16];
		for(int i=0; i<mProgressDrawable.length; i++){
			mProgressDrawable[i] = RUtils.getResId(baseContext.getPackageName()+".R$drawable", "load_"+(i+1)); 
		}
		mWebView = (WebView) context.findViewById(R.id.browser_webview);
		mViewSwitcher = (ViewSwitcher) super.context.findViewById(R.id.browser_switcher);
	}
	
}
