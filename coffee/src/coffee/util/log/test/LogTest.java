package coffee.util.log.test;

import coffee.util.log.Log;

public class LogTest {

	//Can't set level for java.util.logging.ConsoleHandler
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Log.info(LogTest.class, "dfdf");
	}

}
