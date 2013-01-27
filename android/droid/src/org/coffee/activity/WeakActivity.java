package org.coffee.activity;

import java.util.WeakHashMap;

import android.app.Activity;
import android.os.Bundle;

public class WeakActivity extends Activity {
	private WeakHashMap<String, Object> map = new WeakHashMap<String, Object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String key = new String("abc");
		Object val = new Object();
		map.put(key, val);
		System.out.println(map.size());
		try {
			key = null;
			System.gc();
			//Thread.sleep(1000 * 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(map.containsValue(val));
	}
}

