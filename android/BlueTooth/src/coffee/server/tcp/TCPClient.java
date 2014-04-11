package coffee.server.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import coffee.server.Online;
import coffee.server.tcp.base.MessageParser;

/**
 * 
 * @author coffee <br>
 *         2014年4月11日下午2:41:52
 */
public class TCPClient extends MessageParser {

	private Socket clientSocket;
	// 客户端输入流
	private BufferedInputStream clientInput;
	// 客户端输出流
	private BufferedOutputStream clientOutput;

	public TCPClient() {
		clientSocket = new Socket();
	}

	public TCPClient(Socket socket) {
		clientSocket = socket;
	}

	/**
	 * 连接远程服务器
	 * 
	 * @param hostname
	 *            传入主机名、域名、或者IP<br>
	 *            例如localhost、192.168.1.7
	 * @param port
	 *            服务器端口号
	 */
	public void connectServer(final String hostname, final int port) {
		try {
			clientSocket.connect(new InetSocketAddress(hostname, port));
			clientOutput = new BufferedOutputStream(clientSocket.getOutputStream());
			clientInput = new BufferedInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听服务器端发回的数据
	 */
	public void listenServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("开始监听服务器发回的数据:");
				byte[] data = new byte[128];
				int len = -1;
				try {
					while ((len = clientInput.read(data)) != -1) {
						String message = new String(data, 0, len);
						String fromUser = getUserFrom(message);
						// 收到好友的上线通知
						if (message.startsWith(MessageParser.Action.ONLINE)) {
							String state = getOnlineState(message);
							if ("1".equals(state)) {
								Online.reg(fromUser, null, 0, 1);
								System.out.println("msg:online " + fromUser + "上线");
								sendMessage(getOnlineAck(fromUser, 1));
							} else if ("-1".equals(state)) {
								Online.unReg(fromUser, 1);
							}
						} else if (message.startsWith(MessageParser.Action.MESSAGE)) {
							System.out.println("msg:message " + message);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("连接关闭");
			}
		}).start();
	}

	/**
	 * 发送文本消息到服务器端
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		System.out.println("message:send " + message);
		byte[] data = message.getBytes();
		try {
			if (clientOutput == null) {
				clientOutput = new BufferedOutputStream(clientSocket.getOutputStream());
			}
			clientOutput.write(data, 0, data.length);
			// 注意flush, 否则对方可能收不到消息
			clientOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送在线状态
	 * 
	 * @param state
	 */
	public void sendMessageOnline(int onlineState) {
		String message = getOnlineToServer(onlineState);
		sendMessage(message);
	}

	/**
	 * java.net.SocketException: Connection reset
	 */
	public void close() {
		try {
			if (clientInput != null) {
				clientInput.close();
			}
			if (clientOutput != null) {
				clientOutput.close();
			}
			if (clientSocket != null) {
				clientSocket.close();
			}
			System.out.println("连接关闭 >>> " + clientSocket.getInetAddress() + ":" + clientSocket.getPort());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TCPClient client = new TCPClient();
		try {
			client.connectServer("localhost", 8888);
			client.listenServer();
			String line = null;
			while ((line = client.readLine()) != null) {
				client.sendMessage(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
	}

	private String readLine() throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		return reader.readLine();
	}

	public Socket getSocket() {
		return this.clientSocket;
	}

	public String getInfo() {
		String info = getSocket().getInetAddress() + ":" + getSocket().getPort();
		return info;
	}
}
