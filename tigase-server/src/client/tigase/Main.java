package client.tigase;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

public class Main {
	public static void main(String[] args) {
		// ConnectionConfiguration config = new
		// ConnectionConfiguration("127.0.0.1",5222);
		Connection connection = new XMPPConnection("android");

		try {
			connection.connect();
			// conn.getAccountManager().createAccount("admin", "admin");
			connection.login("admin", "admin");

			Chat chat = connection.getChatManager().createChat("admin@android",
					new MessageListener() {
						public void processMessage(Chat chat,
								org.jivesoftware.smack.packet.Message message) {
							System.out.println("Received message: " + message);
							System.out.println(message.getBody());
						}
					});

			chat.sendMessage("Hi buddy!");

			connection.addPacketListener(new PacketListener() {

				public void processPacket(Packet pack) {
					System.out.println("pack: " + pack.toXML());
					System.out.println(pack.getProperty("body"));
				}

			}, new PacketFilter() {
				public boolean accept(Packet arg0) {
					return true;
				}
			});

			System.out.println("ok");

			Thread.sleep(1000 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.disconnect();
		}
	}
}
