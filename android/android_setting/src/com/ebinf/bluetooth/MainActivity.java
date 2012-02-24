package com.ebinf.bluetooth;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.ebinf.bluetooth.action.BluetoothService;
import com.ebinf.bluetooth.adapter.bean.DeviceInfoBean;

/**
 * 主界面 
 * @author coffee
 */
public class MainActivity extends Activity implements DialogInterface.OnClickListener, IBluetooth{

	private MainActivity context = this;

	private BluetoothAdapter mBtAdapter;
	public static BluetoothService mBtService;
	
	private ArrayList<DeviceInfoBean> deviceList = new ArrayList<DeviceInfoBean>();
	
	private ProgressDialog scanDeviceProgressDialog; //扫描设备进度对话框
	private TextView contentView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		mBtService = new BluetoothService(this, mSocketHandler);
		
		contentView = new TextView(this);
		this.setContentView(contentView);
		
		super.onStart();
		dialogMain = new AlertDialog.Builder(this).create();
		dialogMain.setButton("用蓝牙发送文件",this);
		dialogMain.setButton2("准备接收文件", this);
		dialogMain.show();
	}
	private AlertDialog dialogMain;
//	@Override
//	protected void onResume() {
//		super.onStart();
//		dialogMain = new AlertDialog.Builder(this).create();
//		dialogMain.setButton("用蓝牙发送文件",this);
//		dialogMain.setButton2("准备接收文件", this);
//		dialogMain.show();
//	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		//蓝牙未打开
		if(mBtAdapter.getState() == BluetoothAdapter.STATE_OFF){
			//打开蓝牙
			mBtService.requestDeviceOpen(false);
		}
		//设备正在打开
		while(mBtAdapter.getState() == BluetoothAdapter.STATE_TURNING_ON){
			try {
				Thread.sleep(2000 * 1);
				Toast.makeText(context, "正在启动蓝牙,请稍后", Toast.LENGTH_SHORT).show();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} //等待
		}
		if(mBtAdapter.getState() == BluetoothAdapter.STATE_OFF){
			Toast.makeText(context, "蓝牙未打开", Toast.LENGTH_SHORT).show();
		}else{ //蓝牙已打开
			if(which == DialogInterface.BUTTON_POSITIVE){ // -- 扫描设备
				if(mBtAdapter.getState() == BluetoothAdapter.STATE_ON){
					scanDeviceProgressDialog = new ProgressDialog(context);
					scanDeviceProgressDialog.setTitle("正在扫描设备");
					scanDeviceProgressDialog.show();
					//如果设备正在扫描则强制取消
					mBtAdapter.cancelDiscovery();
					//异步扫描设备
					mBtAdapter.startDiscovery();
					//发现设备
			        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			        this.registerReceiver(mReceiver, filter);
			        //扫描设备结束
			        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); 
			        this.registerReceiver(mReceiver, filter);
				}
			}
			if(which == DialogInterface.BUTTON_NEGATIVE){//接收文件
				//是蓝牙设备可见
				mBtService.requestDeviceDiscoverable();
				
			}
		}
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                
                DeviceInfoBean info = new DeviceInfoBean();
                //90:21:55:29:71:83
                if (remoteDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                	info.setPair(true);
                }else{
                	info.setPair(false);
                }
                info.setDeviceAddress(remoteDevice.getAddress());
                info.setDeviceName(remoteDevice.getName());
                
                deviceList.remove(info); //删除以前存在的。 避免重复
                deviceList.add(info);
            } 
            //扫描结束
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
            	scanDeviceProgressDialog.dismiss();
            	Intent intentTmp = new Intent();
            	intentTmp.setClass(context, DeviceListActivity.class);
            	intentTmp.putParcelableArrayListExtra(KEY_DEVICE_LIST, deviceList);
            	MainActivity.this.startActivityForResult(intentTmp,REQUEST_CODE_CONNECT);
            }
		}
	};
	
	private SocketHandler mSocketHandler = new SocketHandler();

	public class SocketHandler extends Handler {
		/**
		 * 处理客户端/服务端发送的消息
		 */
		@Override
		public void handleMessage(final Message msg) {
			if (msg.obj != null) {
				contentView.append(msg.obj + "");
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CODE_CONNECT: //设备连接
				//连接成功
				if(resultCode == REQUEST_RESULT_CONNECT_OK){
					//dialogMain.dismiss();
					AlertDialog.Builder builderInner = new AlertDialog.Builder(context);
					builderInner.setTitle("设备连接成功，确定发送?");
					builderInner.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//发送文件
							mBtService.sendMessage("33333333" + "\r\n");
							mBtService.sendMessage(IBluetooth.FLAG_FILE_END + "\r\n");
							//dialogMain.show();
						}
					});
					builderInner.setNegativeButton("取消", null);
					builderInner.show();
				}
				//连接失败，请重试
				if(resultCode == REQUEST_RESULT_CONNECT_FAILED){
					Toast.makeText(context, "远程设备连接失败,请重试", Toast.LENGTH_LONG).show();
				}
				break;
			case REQUEST_MAKE_DISCOVERABLE:
				if(resultCode == 0){
					Toast.makeText(context, "设备不可见,无法接受文件", Toast.LENGTH_LONG).show();
				}else{
					mBtService.startServer(mBtAdapter);
					Toast.makeText(context, "准备接收文件", Toast.LENGTH_LONG).show();
				}
				break;
		}
	}
	//释放资源
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
			this.unregisterReceiver(mReceiver); //注意一定要取消注册，释放资源
		}catch(Exception e){
			//e.printStackTrace();
		}
		mBtService.releaseAll();
	}
}
