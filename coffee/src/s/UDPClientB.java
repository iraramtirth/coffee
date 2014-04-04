package s;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

public class UDPClientB {
	// 服务器端IP和端口
	static String serverIp = "42.120.17.79";
	static int serverPort = 8801;

	public static void main(String args[]) {
		try {

			DatagramSocket sendSocket = new DatagramSocket(40001);

			// 接收数据包
			byte recvBuf[] = new byte[1000];
			DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
			String strData;
			String clientAIp = "";
			int clientAPort = 0;

			// 发送数据包
			String strSend = "ClientB";
			String strSendToA = "this msg is from B";
			byte[] sendBuf = new byte[100];
			sendBuf = strSend.getBytes();

			DatagramPacket sendPacket = new DatagramPacket(sendBuf, strSend.length(), InetAddress.getByName(serverIp), serverPort);
			sendSocket.send(sendPacket);
			System.out.println("send the data: 'ClientB' to server.");
			while (true) {
				sendSocket.receive(receivePacket);
				strData = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println(new Date() + " ---- receive data from server:" + strData + " host ip:" + receivePacket.getAddress().toString());
				if (strData.indexOf("-") != -1) {
					String[] clientA = strData.split("-");
					clientAIp = clientA[0];
					clientAPort = Integer.valueOf(clientA[1]).intValue();
				}

				if (!clientAIp.equals("")) {
					DatagramPacket sendPkA = new DatagramPacket(strSendToA.getBytes(), strSendToA.length(), InetAddress.getByName(clientAIp), clientAPort);
					sendSocket.send(sendPkA);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					sendSocket.send(sendPkA);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
						e.printStackTrace();
					}
					sendSocket.send(sendPkA);

					try {
						Thread.sleep(3000);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SocketException e) {
			System.out.println("不能打开数据报Socket，或数据报Socket无法与指定端口连接!");
		} catch (Exception ioe) {
			System.out.println("网络通信出现错误，问题在" + ioe.toString());
		}
	}
}