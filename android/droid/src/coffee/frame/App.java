package coffee.frame;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 
 * @author coffee
 * 
 *         2013年12月13日下午4:48:36
 */
public class App extends Application {
	private static Context context;
	
	private static Handler mHandler;
	@Override
	public void onCreate() {
		context = this;
		mHandler = new Handler(){
			
		};
	}
	
	public static Context getContext(){
		return context;
	}
	
	public static Handler getHandler(){
		return mHandler;
	}
}
