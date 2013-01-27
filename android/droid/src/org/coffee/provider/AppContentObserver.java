package org.coffee.provider;

import android.database.ContentObserver;
import android.os.Handler;

/**
 * contentObserver是一个提前通知，这时候只是通知cursor说，我的内容变化了。
 * datasetObserver是一个后置通知，只有通过requery() deactivate() close()方法的调用才能获得这个通知。
 * @author coffee
 */
public class AppContentObserver extends ContentObserver {

	public AppContentObserver(Handler handler) {
		super(handler);
	}

	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		if(selfChange){
			System.out.println("isChange....");
		}
	
	}
}
