package coffee.job;

public class Params {

	public static void test(String ... params){
		System.out.println(params.length);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(args.length);
		test();
		test("hello");
	}

}
