package coffee.server.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.os.AsyncTask;
import coffee.server.Config;

/**
 * 
 * @author coffee <br>
 *         2014年4月3日下午3:32:09
 */
public class Broadcast {

	private final String host = "255.255.255.255";
	private final int portTarget = Config.PORT_UDP;

	/**
	 * 发送上线、离线通知 <br>
	 * udp协议规定:发送如下报文广播到局域网 <br>
	 * syn=1,reg <br>
	 * 下线之前发送报文 <br>
	 * syn=0,unreg <br>
	 * 
	 * @param regOrUn
	 *            true 表示通知用户上线, false 用户下线
	 */
	public void sendBroadcast(final boolean regOrUn) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				DatagramSocket socket = null;
				try {
					// 创建用来发送数据报包的套接字
					socket = new DatagramSocket();
					String dataStr = regOrUn ? "syn=1,reg" : "syn=0,unreg";
					byte[] data = dataStr.getBytes();
					DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(host), portTarget);
					//
					socket.send(dp);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if(socket != null){
						socket.close();
					}
				}
				return null;
			}
		}.execute();
	}

	public void sendRegMessage(final String targetHost) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				DatagramSocket socket = null;
				try {
					// 创建用来发送数据报包的套接字
					socket = new DatagramSocket();
					String dataStr = "syn=0,reg";
					byte[] data = dataStr.getBytes();
					DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getByName(targetHost), portTarget);
					//
					socket.send(dp);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					socket.close();
				}
				return null;
			}
		}.execute();
	}

	/**
	 * 新上线的用户,广播给全部在线用户
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isNewReg(String data) {
		return "syn=1,reg".equals(data);
	}

	/**
	 * 接收到广播以后, 通知新上线的用户，我也在线
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isReg(String data) {
		return "syn=0,reg".equals(data);
	}

	/**
	 * 广播全部在线用户,我已上线
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isUnReg(String data) {
		return "syn=1,unreg".equals(data);
	}
}
