package org.coffee.seven.action;

import java.util.Hashtable;
import java.util.Map;

import android.view.View;
/**
 * 主要用于缓存 view 
 * @author wangtao
 */
public class ViewCacher {
	private static  Map<String, View> viewMap = new Hashtable<String, View>();
	
	public static void add(View view){
		if(view == null){
			return;
		}
		viewMap.put(view.toString(), view);
	}
	
	public static View getView(String key){
		return viewMap.get(key);
	}
	
	public static void remove(String value){
		viewMap.remove(value);
	}
	
	
}
