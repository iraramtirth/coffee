package coffee.im.bluetooth.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.widget.Toast;
import coffee.im.bluetooth.App;
import coffee.im.bluetooth.constant.ConstMsg;
import coffee.im.bluetooth.logic.base.BaseLogic;

/**
 * 聊天业务
 * 
 * @author coffee<br>
 *         2013上午8:25:28
 */
public class ChatLogic extends BaseLogic {

	private static ChatLogic instance;

	private BluetoothServerSocket serverSocket;
	private BluetoothSocket socket;
	private BufferedReader in;
	private BufferedWriter out;

	public static UUID uuid = UUID.fromString("92411435-1a7e-400f-b09a-9d6f0d08382a");

	public static String SDP = uuid.toString();

	private ChatLogic() {

	}

	public static ChatLogic getInstance() {
		if (instance == null) {
			instance = new ChatLogic();
		}
		return instance;
	}

	/**
	 * 是否已经连接
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return this.out != null;
	}

	/**
	 * 打开蓝牙服务。 监听客户端请求
	 */
	public void startServer() {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					serverSocket = BluetoothAdapter.getDefaultAdapter().listenUsingInsecureRfcommWithServiceRecord(SDP, uuid);
					// 开启服务. 如果设备不可见。 则客户端无法连接
					socket = serverSocket.accept();
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					readContent(socket.getRemoteDevice().getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	/**
	 * 从远程设备读取内容 ：注意该方法需要运行在后台线程中，如：AsyncTask
	 */
	private void readContent(String remoteDeviceName) {
		try {
			String line = null;
			// A line is represented by zero or more characters followed by
			// '\n', '\r', "\r\n" or the end of the reader.
			// 注意line必须 以'\n', '\r', "\r\n"结尾
			while ((line = in.readLine()) != null) {
				sendMessage(ConstMsg.MSG_IM_RECV_MESSAGE, line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 连接远程服务
	public void connectionServer(final String remoteAddress) {
		final BluetoothDevice remoteDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(remoteAddress);
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					// 该行代码很重要。 如果不取消Discovery。将会无法connect设备
					// mBtAdapter.getRemoteDevice(remoteDevice.getAddress());
					socket = remoteDevice.createInsecureRfcommSocketToServiceRecord(uuid);
					// .createRfcommSocketToServiceRecord(IActivity.uuid);
					socket.connect();

					out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					// 接收服务端传输过来的数据
					readContent(remoteDevice.getName());
				} catch (Exception e) {
					// java.io.IOException: Connection refused
					e.printStackTrace();
				}
				return null;
			}
		}.execute();
	}

	/**
	 * 发送聊天内容
	 */
	public void sendChatMessage(String content) {
		try {
			if (out != null) {
				out.write(content);
				out.flush();
			} else {
				Toast.makeText(App.getContext(), "未连接远程设备", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放所有资源
	 */
	public void releaseAll() {
		try {
			if (this.out != null) {
				this.out.close();
			}
			if (this.in != null) {
				this.in.close();
			}
			if (this.socket != null) {
				this.socket.close();
			}
			if (this.serverSocket != null) {
				this.serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
