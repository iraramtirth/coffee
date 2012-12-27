package coffee.test;

import java.util.HashMap;
import java.util.Map;

public class InnerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		System.out.println(map.get(""));
		
		if(map.get("dd") != true)
		{
			
		}
		
	}

}
