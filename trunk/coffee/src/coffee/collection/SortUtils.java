package coffee.collection;

import java.text.CollationKey;
import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.record.formula.functions.T;

/**
 * 排序工具
 * @author 王涛
 */
public class SortUtils {

	/**
	 * key按照中文拼音首字母排序
	 */
	public static List<String> sortByPinyin(final List<String> strList) {
		List<String> keyList = new LinkedList<String>(strList);
		Collections.sort(keyList, new Comparator<String>() {
			Collator collator = Collator.getInstance();
			public int compare(String elem1, String elem2) {
				CollationKey key1 = collator.getCollationKey(elem1.toString()
						.toLowerCase());
				CollationKey key2 = collator.getCollationKey(elem2.toString()
						.toLowerCase());
				return key1.compareTo(key2);
			}
		});
		return keyList;
	}
	
	public static List<String> sortByPinyin(String ... str){
		List<String> items = sortByPinyin(Arrays.asList(str));
		return items;
	}

	public class CollatorComparator implements Comparator<T> {
		Collator collator = Collator.getInstance();
		public int compare(T element1, T element2) {
			CollationKey key1 = collator.getCollationKey(element1.toString()
					.toLowerCase());
			CollationKey key2 = collator.getCollationKey(element2.toString()
					.toLowerCase());
			return key1.compareTo(key2);
		}
	}
	
	public static void main(String[] args) {
		List<String> items = sortByPinyin("安全,综合,背景".split(","));
		System.out.println(items);
	}
}
