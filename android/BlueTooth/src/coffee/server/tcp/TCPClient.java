package coffee.server.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import coffee.im.bluetooth.activity.base.HandlerMgr;
import coffee.im.bluetooth.constant.ConstMsg;
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
	//
	private Thread sender, receiver;
	private List<String> messages;
	private boolean isRunning = true;

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
			start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开启收发消息的线程
	 */
	public void start() {
		messages = new ArrayList<String>();
		// 发送消息的线程
		sender = new Thread(new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					if (messages.isEmpty()) {
						try {
							System.out.println("wait-sendMessage");
							synchronized (sender) {
								sender.wait();
							}
							System.out.println("send-ok");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} else {
						String message = messages.get(0);
						send(message);
						messages.remove(message);
					}
				}
			}
		});
		receiver = new Thread(new Runnable() {
			@Override
			public void run() {
				listenServer();
			}
		});
		receiver.start();
		sender.start();
	}

	/**
	 * 监听服务器端发回的数据
	 */
	private void listenServer() {
		System.out.println("msg:开始监听服务器发回的数据:");
		byte[] data = new byte[128];
		int len = -1;
		try {
			while ((len = clientInput.read(data)) != -1) {
				String message = new String(data, 0, len, "UTF-8");
				//
				HandlerMgr.sendMessage(ConstMsg.IM_MESSAGE_RECV, message);
				String fromUser = getUserFrom(message);
				String action = getMessageAction(message);
				// 收到好友的上线通知
				if (Action.ONLINE.equals(action) || Action.ONLINE_ACK.equals(action)) {
					String[] state = getOnlineState(message);
					if ("1".equals(state[0])) {
						Online.reg(fromUser, state[1], state[2], 1);
						System.out.println("msg:online " + fromUser + "上线");
					} else if ("-1".equals(state)) {
						Online.unReg(fromUser, 1);
					}
					if (Action.ONLINE.equals(action)) {
						sendMessageOnlineAck(fromUser, 1);
					}
				} else if (Action.MESSAGE.equals(action)) {
					System.out.println("msg:message " + message);
				} else {
					// 客户端可以接收到消息。
					System.out.println("收:" + getSocket().getRemoteSocketAddress() + "--" + message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("listenServer-stop");
		}
	}

	/**
	 * 真正发送数据的地方
	 * 
	 * @param message
	 */
	private void send(String message) {
		try {
			byte[] data = message.getBytes("UTF-8");
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
	 * 发送文本消息到服务器端
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		System.out.println("msg:send " + message);
		HandlerMgr.sendMessage(ConstMsg.IM_MESSAGE_SEND, message);
		if (messages != null) {// 运行在客户端
			messages.add(message);
			synchronized (sender) {
				sender.notify();
			}
		} else {
			// 运行在服务器端
			send(message);
		}
	}

	/**
	 * 发送在线状态
	 * 
	 * @param state
	 */
	public void sendMessageOnline(int onlineState) {
		String localHost = getSocket().getLocalAddress().getHostAddress();
		int localPort = getSocket().getLocalPort();
		String onlineBody = onlineState + "," + localHost + "," + localPort;
		String message = getOnlineToServer(onlineBody);
		sendMessage(message);
	}

	public void sendMessageOnlineAck(String userTo, int onlineState) {
		String localHost = getSocket().getLocalAddress().getHostAddress();
		int localPort = getSocket().getLocalPort();
		String onlineBody = onlineState + "," + localHost + "," + localPort;
		String message = getOnlineAck(userTo, onlineBody);
		sendMessage(message);
	}

	/**
	 * java.net.SocketException: Connection reset
	 */
	public void close() {
		try {
			isRunning = false;
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
		} finally {
			Online.unReg(getUsername(), 1);
		}
	}

	public static void main(String[] args) {
		TCPClient client = new TCPClient();
		try {
			client.connectServer("localhost", 8888);
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

	@Override
	public String toString() {
		String info = getSocket().getInetAddress() + ":" + getSocket().getPort();
		return info;
	}
}
