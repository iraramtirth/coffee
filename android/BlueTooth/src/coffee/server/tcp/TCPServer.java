package coffee.server.tcp;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 处理ServerSocket监听到的请求
 * 
 * @author coffee <br>
 *         2014年3月31日下午3:57:52
 */
public class TCPServer {

	private ServerSocket serverSocket;

	private Vector<TCPClient> clients;

	private TCPServer() {
		clients = new Vector<TCPClient>();
	}

	/**
	 * 启动服务
	 */
	private void start() {
		try {
			ExecutorService executors = Executors.newCachedThreadPool();
			serverSocket = new ServerSocket(8888);
			while (true) {
				Socket socket = serverSocket.accept();
				executors.execute(new SocketRunnable(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 分发消息<br>
	 * 处理Client请求
	 */
	private void dispatchMessage(TCPClient fromSocket, String message) {
		synchronized (clients) {
			// 一对一
			// 约定协议规范to:id:消息内容
			// 例如to:10086:hello
			if (message.startsWith("to:")) {
				String port = message.substring("to:".length(), message.indexOf(":", 3));
				for (TCPClient client : clients) {
					if (port.equals(client.getSocket().getPort() + "")) {
						message = fromSocket.getInfo() + ">>>" + message;
						client.sendMessage(message);
						break;
					}
				}
			}
			// 群聊
			else {
				for (TCPClient client : clients) {
					message = fromSocket.getInfo() + ">>>" + message;
					client.sendMessage(message);
				}
			}
		}
	}

	/**
	 * 响应请求
	 * 
	 * @param message
	 */
	public void responseRequest(TCPClient fromSocket, String message) {
		System.out.println(message);
		StringBuilder sb = new StringBuilder();
		// GET请求
		if (message.startsWith("GET")) {
			sb.append("HTTP/1.1 404 Not Found\r\n" + //
					"Date: Sat, 31 Dec 2005 23:59:59 GMT\r\n" + //
					"Content-Type: text/html;charset=ISO-8859-1\r\n" + //
//					"Content-Length: 122\n" + //
					"\r\n" ); //
//					"<html>" + //
//					"<head>" + //
//					"<title>Wrox Homepage</title>" + //
//					"</head>" + //
//					"<body>" + //
//					" hello world " + System.currentTimeMillis() + //
//					"</body>" + //
//					"</html>"
//);
		}
		fromSocket.sendMessage(sb.toString());
	}

	/**
	 * 处理客户端请求
	 */
	private class SocketRunnable implements Runnable {
		private TCPClient client;

		public SocketRunnable(Socket socket) {
			this.client = new TCPClient(socket);
		}

		@Override
		public void run() {
			clients.add(client);
			BufferedInputStream bin = null;
			try {
				System.out.println("监听到" + client.getInfo() + "发来的连接:");
				bin = new BufferedInputStream(client.getSocket().getInputStream());
				byte[] data = new byte[1024];
				int len = -1;
				// if (bin.available() > 0) {
				while ((len = bin.read(data)) != -1) {
					String message = new String(data, 0, len);
					// HTTP请求
					if (message.startsWith("GET")) {
						responseRequest(client, message);
						break;
					} else {
						dispatchMessage(client, message);
					}
				}
				// }
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println(e.getMessage() + "\n" + e.getCause());
			} finally {
				try {
					if (bin != null) {
						bin.close();
					}
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				clients.remove(client);
			}
		}

	}

	public static void main(String[] args) {
		TCPServer server = new TCPServer();
		server.start();
	}
}
