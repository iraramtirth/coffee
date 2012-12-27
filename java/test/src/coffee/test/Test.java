package coffee.test;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		int i = -1;
		
		System.out.println(i++);
		
		String strs = "property name=\"filter_rule\"  id =\"20120425190009370\"&gt";
		
		   String id = RegexUtils.match(strs, "id =\"(\\d+?)\"");

		   System.out.println(id);
	}

}
