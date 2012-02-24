package com.ebinf.bluetooth.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.ebinf.bluetooth.IBluetooth;
import com.ebinf.bluetooth.MainActivity.SocketHandler;

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
		context.startActivityForResult(discoverIntent, IBluetooth.REQUEST_MAKE_DISCOVERABLE);
	}
	/**
	 * 请求打开蓝牙
	 * @param isRequest true : 询问用户     false : 强制打开
	 * 
	 */
	public void requestDeviceOpen(boolean isRequest) {
		if(isRequest){
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			context.startActivityForResult(enableIntent, IBluetooth.REQUEST_ENABLE);
		}else{
			BluetoothAdapter.getDefaultAdapter().enable();
		}
	}
	/**
	 * 请求配对
	 * 当调用createBond时，请求[android.bluetooth.device.action.PAIRING_REQUEST]被响应
	 * @return true : 配对成功   false 失败
	 */
	public boolean requestPairing(BluetoothDevice remoteDevice, Activity context){
		try {
			String methodName = "createBond";
			//BluetoothDevice. android.bluetooth.device.action.PAIRING_REQUEST
			Intent enableIntent = new Intent("android.bluetooth.device.action.PAIRING_REQUEST");
			context.startActivityForResult(enableIntent, IBluetooth.REQUEST_CODE_PAIR);
			Method createBond = remoteDevice.getClass().getMethod(methodName, new Class[]{});
			//false on immediate error, true if bonding will begin
			boolean result = (Boolean)createBond.invoke(remoteDevice, new Object[]{});
			context.setResult(result == true ? 1:0);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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
	
	/**
	 * 打开蓝牙服务。 监听客户端请求
	 */
	public void startServer(final BluetoothAdapter mBtAdapter) {
		new AsyncTask<Void, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				try {
					serverSocket = mBtAdapter.listenUsingRfcommWithServiceRecord(
							IBluetooth.SDP_RECORD_NAME, IBluetooth.uuid);
					// 开启服务. 如果设备不可见。 则客户端无法连接
					socket = serverSocket.accept();
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					//接收客户端发送过来的文件
					readContent();
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
	private  void readContent(){
		try{
			String line = null;
			//A line is represented by zero or more characters followed by '\n', '\r', "\r\n" or the end of the reader.
			//注意line必须 以'\n', '\r', "\r\n"结尾
			Outer.setPath(Environment.getExternalStorageDirectory().getPath() + "/test.vcard", true, false);
			while( (line = in.readLine()) != null){
				Message msg = mSocketHandler.obtainMessage();
				msg.obj = line;
				mSocketHandler.sendMessage(msg);
				if(line.endsWith(IBluetooth.FLAG_FILE_END)){
					readContent();
					break;//结束该while循环。 并且重新in.readLine
				}
				Outer.pl(line);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * @param remoteDevice
	 * @return ： 连接成功返回true
	 */
	public boolean connectionServer(final BluetoothDevice remoteDevice) {
		//连接远程服务
		try {
			//该行代码很重要。 如果不取消Discovery。将会无法connect设备
			//mBtAdapter.getRemoteDevice(remoteDevice.getAddress());
			socket = remoteDevice.createRfcommSocketToServiceRecord(IBluetooth.uuid);
			socket.connect();
			
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			new AsyncTask<Void, Void, Void>(){
				@Override
				protected Void doInBackground(Void... params) {
					//接收从服务器端发送的消息
					readContent();
					return null;
				}
			}.execute();
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Bluetooth", e.getMessage());
			return false;
		} 
		return true;
	}

	/**
	 * 发送聊天内容
	 */
	public void sendMessage(String content){
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
