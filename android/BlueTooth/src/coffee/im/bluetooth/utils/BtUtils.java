package coffee.im.bluetooth.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.logging.SocketHandler;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;
import coffee.im.bluetooth.IActivity;

/**
 * 蓝牙工具类
 * 
 * @author coffee<br>
 *         2013上午11:09:03
 */
public class BtUtils {
	private Activity context;

	private BluetoothServerSocket serverSocket;

	private BluetoothSocket socket;
	private BufferedReader in;
	private BufferedWriter out;

	private final SocketHandler mSocketHandler;

	public BtUtils(Activity context, SocketHandler mSocketHandler) {
		this.context = context;
		this.mSocketHandler = mSocketHandler;
	}

	/**
	 * 请求Local蓝牙设备可见
	 */
	public void requestDeviceDiscoverable() {
		Intent discoverIntent = new Intent(
				BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,
				300);
		context.startActivityForResult(discoverIntent,
				IActivity.REQUEST_MAKE_DISCOVERABLE);
		// Settings.System.putInt(context.getContentResolver(),Settings.System.BLUETOOTH_DISCOVERABILITY,
		// 2);
		// Settings.System.putInt(context.getContentResolver(),Settings.System.BLUETOOTH_DISCOVERABILITY_TIMEOUT,120);
	}

	/**
	 * 请求打开Local蓝牙设备
	 */
	public void requestDeviceOpen() {
		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		context.startActivityForResult(enableIntent, IActivity.REQUEST_ENABLE);
	}

	/**
	 * 请求与remote蓝牙设备配对
	 * 当调用createBond时，请求[android.bluetooth.device.action.PAIRING_REQUEST]被响应
	 */
	public void requestPairing(BluetoothDevice remoteDevice, Activity activity) {
		String methodName = "createBond";
		// BluetoothDevice. android.bluetooth.device.action.PAIRING_REQUEST
		Intent enableIntent = new Intent(
				"android.bluetooth.device.action.PAIRING_REQUEST");
		activity.startActivityForResult(enableIntent,
				IActivity.REQUEST_CODE_PAIR);
		try {
			Method createBond = remoteDevice.getClass().getMethod(methodName,
					new Class[] {});
			createBond.invoke(remoteDevice, new Object[] {});
			// 设置pin码
			// setPin(remoteDevice, "0000");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createBond(BluetoothDevice remoteDevice) {
		try {
			Method createBond = remoteDevice.getClass().getMethod("createBond",
					new Class[] {});
			createBond.invoke(remoteDevice, new Object[] {});
			// 设置pin码
			// setPin(remoteDevice, "0000");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setPairingConfirmation(BluetoothDevice device, boolean bool) {
		// device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(
		// BluetoothAdapter.getDefaultAdapter().getAddress());
		try {
			Method setPairingConfirmation = device.getClass().getMethod(
					"setPairingConfirmation", new Class[] { boolean.class });
			setPairingConfirmation.invoke(device, new Object[] { bool });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 远程设置收到配对请求后， 会弹出一个框，该方法会取消提示框
	 * 
	 * @param device
	 * @return
	 */
	public boolean cancelPairingUserInput(BluetoothDevice device) {
		try {
			// device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(
			// BluetoothAdapter.getDefaultAdapter().getAddress());
			Method createBondMethod = device.getClass().getMethod(
					"cancelPairingUserInput");
			boolean result = (Boolean) createBondMethod.invoke(device);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 请求取消与remote蓝牙设备的配对
	 */
	public void requestCancelPairing(BluetoothDevice remoteDevice) {
		try {
			String methodName = "removeBond";
			Method createBond = remoteDevice.getClass().getMethod(methodName,
					new Class[] {});
			createBond.invoke(remoteDevice, new Object[] {});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param remoteDevice
	 * @param pinCode
	 * @return
	 */
	public boolean setPin(BluetoothDevice remoteDevice, String pinCode) {
		try {
			Method setPin = remoteDevice.getClass().getDeclaredMethod("setPin",
					new Class[] { byte[].class });
			Boolean result = (Boolean) setPin.invoke(remoteDevice,
					new Object[] { pinCode.getBytes() });
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	/**
	 * 打开蓝牙服务。 监听客户端请求
	 */
	public void startServer(final BluetoothAdapter mBtAdapter) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					serverSocket = mBtAdapter
							.listenUsingRfcommWithServiceRecord(IActivity.SDP,
									IActivity.uuid);
					// 开启服务. 如果设备不可见。 则客户端无法连接
					socket = serverSocket.accept();
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(
							socket.getOutputStream()));
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
				System.out.println(line);
				//Message msg = mSocketHandler.obtainMessage();
				//msg.obj = new ChatItemBean(remoteDeviceName, line);
				//
//				mSocketHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 连接远程服务
	public void connectionServer(final BluetoothDevice remoteDevice) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					// 该行代码很重要。 如果不取消Discovery。将会无法connect设备
					// mBtAdapter.getRemoteDevice(remoteDevice.getAddress());
					socket = remoteDevice
							.createInsecureRfcommSocketToServiceRecord(IActivity.uuid);
					// .createRfcommSocketToServiceRecord(IActivity.uuid);
					socket.connect();

					out = new BufferedWriter(new OutputStreamWriter(
							socket.getOutputStream()));
					in = new BufferedReader(new InputStreamReader(
							socket.getInputStream()));
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
				Toast.makeText(context, "未连接远程设备", Toast.LENGTH_SHORT).show();
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
