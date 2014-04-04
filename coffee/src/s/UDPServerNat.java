package s;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServerNat {
	static public void main(String args[]) {
		try {
			DatagramSocket receiveSocket = new DatagramSocket(8801);
			// 接收数据包
			byte[] recvBuf = new byte[1000];
			DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
			// 发送数据包
			String strSend = "this msg is from server!";
			byte[] sendBuf = new byte[1000];
			sendBuf = strSend.getBytes();
			DatagramPacket sendPacket;
			// 来自客户端A的信息
			String clientAIp = "";
			int clientAPort = 0;
			// 来自客户端B的信息
			String clientBIp = "";
			int clientBPort = 0;

			String hostIp;
			int hostPort;
			String strData;

			System.out.println("startinig to receive packet");
			while (true) {
				receiveSocket.receive(receivePacket);

				hostIp = receivePacket.getAddress().toString().substring(1);
				hostPort = receivePacket.getPort();
				System.out.println("来自主机：" + hostIp + "端口：" + hostPort);
				strData = new String(receivePacket.getData(), 0, receivePacket.getLength());
				System.out.println("the received data: " + strData);
				if (strData.equals("ClientA")) {
					clientAIp = hostIp;
					clientAPort = hostPort;
				}

				if (strData.equals("ClientB")) {
					clientBIp = hostIp;
					clientBPort = hostPort;
				}

				if (!clientAIp.equals("") && !clientBIp.equals("")) {
					String msgToA = clientBIp + "-" + clientBPort;
					String msgToB = clientAIp + "-" + clientAPort;
					// 把B的IP和端口号发送给A
					sendPacket = new DatagramPacket(msgToA.getBytes(), msgToA.length(), InetAddress.getByName(clientAIp), clientAPort);
					receiveSocket.send(sendPacket);
					// 把A的IP和端口号发送给B
					sendPacket = new DatagramPacket(msgToB.getBytes(), msgToB.length(), InetAddress.getByName(clientBIp), clientBPort);
					receiveSocket.send(sendPacket);
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("网络通信出现错误，问题在" + e.toString());
		}
	}
}