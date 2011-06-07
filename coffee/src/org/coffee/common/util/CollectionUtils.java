package org.coffee.common.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CollectionUtils {

	/**
	 * 给sort按照 value排序 
	 */
	public static Map<String,Integer> sortMapByValue(final Map<String,Integer> map){
		List<String> keyList = new LinkedList<String>(map.keySet());
		//System.out.println("排序前：key"+keyList);
		Collections.sort(keyList, new Comparator<String>() {
			 /**
			  * @return 负值：k1 < k2
			  */
			 public int compare(String k1, String k2){
				 return -(map.get(k1) - map.get(k2));
			 }
		});
		//System.out.println("排序后："+keyList);
		Map<String,Integer> newMap = new LinkedHashMap<String, Integer>();
		for(String key : keyList){
			newMap.put(key, map.get(key));
		}
		//System.out.println("排序前：---: "+map);
		//System.out.println("排序后：---: "+newMap);
		return map;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new HashMap<String,Integer>();
		map.put("aaa", 5);
		map.put("bbb", 2);
		map.put("ccc", 4444);
		
		CollectionUtils.sortMapByValue(map);
	}

}
