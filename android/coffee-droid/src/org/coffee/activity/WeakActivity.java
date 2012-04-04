package org.coffee.activity;

import java.util.WeakHashMap;

import android.app.Activity;
import android.os.Bundle;

public class WeakActivity extends Activity {
	private WeakHashMap<Object, Object> map = new WeakHashMap<Object, Object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Object abc = new Object();
		map.put(abc, new Object());
		System.out.println(map.size());
		try {
			abc = null;
			System.gc();
			Thread.sleep(1000 * 1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(map.get(abc));
	}
}
