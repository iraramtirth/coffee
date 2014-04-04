package coffee.server.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import coffee.server.Config;

/**
 * 在端口监听{@link Config.PORT_UDP_NAT} 监听用户收到的广播
 * 
 * @author coffee <br>
 *         2014年4月3日下午5:57:45
 */
public class UDPServerNat {

	private DatagramSocket natSocket;
	private boolean isRunning = false;

	public UDPServerNat() {
		try {
			natSocket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听 {@link Config#PORT_UDP}端口
	 */
	private void start() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 接收数据包
				byte[] data = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(data, data.length);
				System.out.println("startinig to receive packet");
				while (isRunning) {
					try {
						natSocket.receive(receivePacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String hostIp = receivePacket.getAddress().toString();
					int hostPort = receivePacket.getPort();
					System.out.println("新消息：" + hostIp + "端口：" + hostPort);
					try {
						String strData = new String(receivePacket.getData(), 0, receivePacket.getLength(), "utf-8");
						System.out.println(">>> " + strData);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	public static void main(String[] args) {
		UDPServerNat server = new UDPServerNat();
		server.start();
	}
}