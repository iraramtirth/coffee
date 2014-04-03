package coffee.server.tcp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

	private Socket clientSocket;
	// 客户端输入流
	private BufferedInputStream clientInput;
	// 客户端输出流
	private BufferedOutputStream clientOutput;

	public Client() {
		clientSocket = new Socket();
	}

	public Client(Socket socket) {
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
	public void connectServer(String hostname, int port) {
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
	private void listenServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("开始监听服务器发回的数据:");
				byte[] data = new byte[128];
				int len = -1;
				try {
					while ((len = clientInput.read(data)) != -1) {
						System.out.println("Server: " + new String(data, 0, len));
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
		byte[] data = message.getBytes();
		try {
			if (clientOutput == null) {
				clientOutput = new BufferedOutputStream(clientSocket.getOutputStream());
			}
			clientOutput.write(data, 0, data.length);
			clientOutput.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		Client client = new Client();
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
