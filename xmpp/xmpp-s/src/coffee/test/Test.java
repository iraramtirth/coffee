package coffee.test;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.XMPPConnection;

import com.googlecode.xmpplib.SmackTest;

public class Test extends SmackTest{
	public static void main(String[] args) throws Exception {
		Test test = new Test();
		for(int i=0; i<10000; i++){
			test.testConnection();
		}
	}
	
	public void testConnection() throws Exception {
		
		try {
			ConnectionConfiguration configuration = createConfiguration();
			configuration.setSASLAuthenticationEnabled(true);
			configuration.setSecurityMode(SecurityMode.enabled);
			
			XMPPConnection connection = new XMPPConnection(configuration);
			connection.connect();
			System.out.println("connect success...");
			connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
		}
	}
	
}
