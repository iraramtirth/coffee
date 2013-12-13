package coffee.frame;

import android.app.Application;
import android.content.Context;

/**
 * 
 * @author coffee
 * 
 *         2013年12月13日下午4:48:36
 */
public class App extends Application {
	private static Context context;

	@Override
	public void onCreate() {
		context = this;
	}
	
	public static Context getContext(){
		return context;
	}
}
