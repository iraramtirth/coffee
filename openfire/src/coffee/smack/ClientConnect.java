package coffee.smack;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

/**
 * 
 * @author coffee
 */
public class ClientConnect {
	public static void main(String[] args) {
		try {
			ConnectionConfiguration connConfig = new ConnectionConfiguration(
					"localhost", 5222);
			// connConfig.setSASLAuthenticationEnabled(false);
			// connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);

			// SASL authentication failed using mechanism PLAIN
			connConfig.setSASLAuthenticationEnabled(true);// 不使用SASL验证
			connConfig.setCompressionEnabled(true);

			//SASLAuthentication.supportSASLMechanism("PLAIN", 0);
			XMPPConnection connection = new XMPPConnection(connConfig);

			connection.connect();// 连接
			
			//AccountManager amgr = connection.getAccountManager();
			//amgr.createAccount("admin", "admin");

			connection.login("admin", "admin");
			Chat chat = connection.getChatManager().createChat(
					"sui.dapeng@gmail.com", new MessageListener() {
						public void processMessage(Chat chat, Message message) {
							System.out.println("Received message: " + message);
							System.out.println(message.getBody());
						}
					});
			chat.sendMessage("Hi buddy!");

			connection.addPacketListener(new PacketListener() {

				public void processPacket(Packet pack) {
					System.out.println(pack.toXML());
					System.out.println(pack.getProperty("body"));
				}

			}, new PacketFilter() {
				public boolean accept(Packet arg0) {
					return true;
				}
			});
			Thread.sleep(1000 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}