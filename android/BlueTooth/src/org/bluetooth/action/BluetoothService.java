package org.bluetooth.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

import org.bluetooth.IActivity;
import org.bluetooth.MainActivity.SocketHandler;
import org.bluetooth.adapter.bean.ChatItemBean;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.widget.Toast;

public class BluetoothService {
	private Activity context;
	
	private BluetoothServerSocket serverSocket;
	
	private BluetoothSocket socket;
	private BufferedReader in;
	private BufferedWriter out;	
	
	private final SocketHandler mSocketHandler;
	
	public BluetoothService(Activity context, SocketHandler mSocketHandler) {
		this.context = context;
		this.mSocketHandler = mSocketHandler;
	}
	
	/**
	 * 请求设备可见
	 */
	public void requestDeviceDiscoverable(){
		Intent discoverIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
		context.startActivityForResult(discoverIntent, IActivity.REQUEST_MAKE_DISCOVERABLE);
	}
	/**
	 * 请求打开蓝牙
	 */
	public void requestDeviceOpen() {
		Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		context.startActivityForResult(enableIntent, IActivity.REQUEST_ENABLE);
	}
	/**
	 * 请求配对
	 * 当调用createBond时，请求[android.bluetooth.device.action.PAIRING_REQUEST]被响应
	 */
	public void requestPairing(BluetoothDevice remoteDevice, Activity activity){
		String methodName = "createBond";
		//BluetoothDevice. android.bluetooth.device.action.PAIRING_REQUEST
		Intent enableIntent = new Intent("android.bluetooth.device.action.PAIRING_REQUEST");
		activity.startActivityForResult(enableIntent, IActivity.REQUEST_CODE_PAIR);
		try {
			Method createBond = remoteDevice.getClass().getMethod(methodName, new Class[]{});
			createBond.invoke(remoteDevice, new Object[]{});
			//setPin(remoteDevice, "000076");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 请求取消配对
	 */
	public void requestCancelPairing(BluetoothDevice remoteDevice){
		 try{
			String methodName = "removeBond"; 
        	Method createBond = remoteDevice.getClass().getMethod(methodName, new Class[]{});
        	createBond.invoke(remoteDevice, new Object[]{});
        }catch(Exception e){
        	e.printStackTrace();
        }
	}
	
	@SuppressWarnings("unused")
	private boolean setPin(BluetoothDevice remoteDevice,  
            String pinCode) throws Exception {  
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
						serverSocket = mBtAdapter.listenUsingRfcommWithServiceRecord(IActivity.SDP ,IActivity.uuid);
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
	 * 从远程设备读取内容
	 * ：注意该方法需要运行在后台线程中，如：AsyncTask
	 */
	private  void readContent(String remoteDeviceName){
		try{
			String line = null;
			//A line is represented by zero or more characters followed by '\n', '\r', "\r\n" or the end of the reader.
			//注意line必须 以'\n', '\r', "\r\n"结尾
			while( (line = in.readLine()) != null){
				System.out.println(line);
				Message msg = mSocketHandler.obtainMessage();
				msg.obj = new ChatItemBean(remoteDeviceName, line);;
				mSocketHandler.sendMessage(msg);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	//连接远程服务
	public void connectionServer(final BluetoothDevice remoteDevice) {
		new AsyncTask<Void, Void, Void>(){
			@Override
			protected Void doInBackground(Void... params) {
				try {
					//该行代码很重要。 如果不取消Discovery。将会无法connect设备
					//mBtAdapter.getRemoteDevice(remoteDevice.getAddress());
					socket = remoteDevice.createRfcommSocketToServiceRecord(IActivity.uuid);
					socket.connect();
					
					out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					//接收服务端传输过来的数据
					readContent(remoteDevice.getName());
				} catch (Exception e) {
					//java.io.IOException: Connection refused
					e.printStackTrace();
				} 
				return null;
			}
		}.execute();
	}

	/**
	 * 发送聊天内容
	 */
	public void sendChatMessage(String content){
		try {
			if(out != null){
				out.write(content);
				out.flush();
			}else{
				Toast.makeText(context, "未连接远程设备", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 释放所有资源
	 */
	public void releaseAll(){
		try {
			if(this.out != null){
				this.out.close();
			}
			if(this.in != null){
				this.in.close();
			}
			if(this.socket != null){
				this.socket.close();
			}
			if(this.serverSocket != null){
				this.serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
