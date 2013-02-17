package coffee.im.bluetooth.utils;

import java.lang.reflect.Method;
import java.util.Set;

import android.app.Activity;
import android.app.ActivityGroup;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import coffee.im.bluetooth.IActivity;

/**
 * 蓝牙工具类
 * 
 * @author coffee<br>
 *         2013上午11:09:03
 */
public class BtUtils {

	/**
	 * 蓝牙是否打开 <br>
	 * 蓝牙状态详细见:<br>
	 * {@link BluetoothAdapter#STATE_OFF},<br>
	 * {@link BluetoothAdapter##STATE_TURNING_ON}, <br>
	 * {@BluetoothAdapter#link #STATE_ON} , <br>
	 * {@link BluetoothAdapter#STATE_TURNING_OFF}
	 */
	public static boolean isOpen() {
		if (BluetoothAdapter.getDefaultAdapter().getState() == BluetoothAdapter.STATE_ON) {
			return true;
		}
		return false;
	}

	public static String getLocalAddress() {
		return BluetoothAdapter.getDefaultAdapter().getAddress();
	}

	/**
	 * 获取所有的配对设备
	 * 
	 * @return
	 */
	public static Set<BluetoothDevice> getBondedDevices() {
		Set<BluetoothDevice> devices = null;
		try {
			devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return devices;
	}

	/**
	 * 请求Local蓝牙设备可见
	 * 
	 * @param context
	 *            注意该activity不能为{@link ActivityGroup}中的子Activity
	 */
	public static void requestDeviceDiscoverable(Activity context) {
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
	public static void requestDeviceOpen(Activity context) {
		if (context == null) {
			return;
		}
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

}
