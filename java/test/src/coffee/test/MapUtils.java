package coffee.test;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

	private static Map<String,String> map = new HashMap<String, String>();
	private static Map<String,String> map2 = new HashMap<String, String>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		map.put("1", "1");
		map.put("11", "11");
		
		map2.put("11", "11");
		map2.put("2", "2");
		map2.put("21", "21");
		
		map.remove(map2.keySet());
		
		System.out.println(map);
	}

}
