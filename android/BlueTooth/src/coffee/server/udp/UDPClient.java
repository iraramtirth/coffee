package coffee.server.udp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;
import coffee.server.Config;
import coffee.server.Online;
import coffee.server.tcp.base.MessageParser;

/**
 * 
 * @author coffee <br>
 *         2014年4月2日上午10:02:19
 */
public class UDPClient extends MessageParser {
	private DatagramSocket clientSocket;

	private MessageCallback callback;

	private boolean isRunning = false;

	/**
	 * 消息回调<br>
	 * 主要用于监听接收到的消息
	 */
	public interface MessageCallback {
		public void execute(byte[] data);
	}

	public UDPClient(int port, MessageCallback callback) {
		try {
			this.callback = callback;
			clientSocket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public UDPClient(DatagramSocket clientSocket, MessageCallback callback) {
		this.callback = callback;
		this.clientSocket = clientSocket;
	}

	public void sendMessage(byte[] message, String host, int port) {
		try {
			DatagramPacket sendPacket;
			sendPacket = new DatagramPacket(message, 0, message.length, InetAddress.getByName(host), port);
			clientSocket.send(sendPacket);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void receiveMessage() {
		isRunning = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				byte[] data = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(data, data.length);
				System.out.println("startinig to receive packet");
				while (isRunning) {
					try {
						clientSocket.receive(receivePacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					String fromHost = receivePacket.getAddress().getHostAddress();
					int fromPort = receivePacket.getPort();
					System.out.println("新消息：" + fromHost + "端口：" + fromPort);
					try {
						String message = new String(receivePacket.getData(), 0, receivePacket.getLength(), "utf-8");
						System.out.println(">>> " + message);
						// 广播接口
						if (fromPort != Config.PORT_UDP) {
							String fromUser = getUserFrom(message);
							// 收到好友的上线通知
							if (message.startsWith(MessageParser.Action.ONLINE)) {
								Online.reg(fromUser, fromHost, Config.PORT_UDP, 0);
								System.out.println("msg:online " + fromUser + "上线");
								String onlineAck = getOnlineAck(fromUser, 1);
								new UDPBroadcast().sendRegMessage(onlineAck, fromHost);
							} else if (message.startsWith(MessageParser.Action.ONLINE_ACK)) {
								Log.d("reg", "通知在线: " + fromHost + ":" + fromPort);
								Online.reg(fromHost, fromHost, Config.PORT_UDP, 0);
							} else if (message.startsWith(MessageParser.Action.MESSAGE)) {
								System.out.println("msg:message " + fromUser + " " + message);
							}
						} else {
							if (callback != null) {
								callback.execute(receivePacket.getData());
							}
						}

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 注意该方法调用之前要执行<br>
	 * {@link #receiveMessage()}
	 * 
	 * @param regOrUn
	 */
	public void broadcast(boolean regOrUn) {
		new UDPBroadcast().sendBroadcast(regOrUn);
	}

	public void close() {
		isRunning = false;
		if (clientSocket != null) {
			broadcast(false);
			clientSocket.close();
		}
	}
}
