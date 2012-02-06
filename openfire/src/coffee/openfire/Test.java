package coffee.openfire;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jivesoftware.openfire.XMPPServer;


public class Test {
	public static void main(String[] args) throws Exception {
		ResourceBundle bundle = ResourceBundle.getBundle("openfire_i18n", Locale.ENGLISH);
		System.out.println(bundle);
		XMPPServer server = new XMPPServer();
		server.start();
	}
}
