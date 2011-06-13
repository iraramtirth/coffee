package org.coffee.common.util;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.formula.functions.T;

public class CollectionUtils {

	/**
	 * 给sort按照 value排序  从大到小
	 */
	public static Map<String,Integer> sortMapByValue(final Map<String,Integer> map,final SortType sortType){
		List<String> keyList = new LinkedList<String>(map.keySet());
		Collections.sort(keyList, new Comparator<String>() {
			 /**
			  * @return 负值：k1 < k2
			  */
			 public int compare(String k1, String k2){
				 if(sortType == SortType.BigToSmall){
					 return -(map.get(k1) - map.get(k2));
				 }else{//从小到大
					 return map.get(k1) - map.get(k2);
				 }
			 }
		});
		Map<String,Integer> newMap = new LinkedHashMap<String, Integer>();
		for(String key : keyList){
			newMap.put(key, map.get(key));
		}
		return newMap;
	}
	
	//排序方式
	private enum SortType{
		SmallToBig,
		BigToSmall
	}
	
	/**
	 * key按照中文排序 
	 */
	public static Map<String,String> sortMapByChinese(final Map<String,String> map){
		List<String> keyList = new LinkedList<String>(map.keySet());
		Collections.sort(keyList, new Comparator<String>() {
			  Collator collator = Collator.getInstance();
			  public int compare(String elem1, String elem2) {
				  CollationKey key1 = collator.getCollationKey(elem1.toString().toLowerCase());
				  CollationKey key2 = collator.getCollationKey(elem2.toString().toLowerCase());
				  return key1.compareTo(key2);
			  }
		});
		Map<String,String> newMap = new LinkedHashMap<String, String>();
		for(String key : keyList){
			newMap.put(key, map.get(key));
		}
		return newMap;
	}
	
	public class CollatorComparator implements Comparator<T> {
		  Collator collator = Collator.getInstance();
		  public int compare(T element1, T element2) {
		    CollationKey key1 = collator.getCollationKey(element1.toString().toLowerCase());
		    CollationKey key2 = collator.getCollationKey(element2.toString().toLowerCase());
		    return key1.compareTo(key2);
		  }
		}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String,String>();
			map.put("北京", "5");
			map.put("bbb", "2");
			map.put("中国", "4444");
		
		//System.out.println(CollectionUtils.sortMapByValue(map, SortType.BigToSmall));
		System.out.println(CollectionUtils.sortMapByChinese(map));
	}

}
