package com.ebinf.bluetooth;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.android.settings.bluetooth.ConnectSpecificProfilesActivity;
import com.android.settings.bluetooth.LocalBluetoothManager;
import com.ebinf.R;
import com.ebinf.bluetooth.action.BluetoothService;
import com.ebinf.bluetooth.adapter.DeviceInfoAdapter;
import com.ebinf.bluetooth.adapter.bean.DeviceInfoBean;

/**
 * 扫描蓝牙设备
 * @author coffee
 */
public class DeviceListActivity extends Activity implements IBluetooth{
    
	private BluetoothAdapter mBtAdapter;
	
	private ListView mListView;
	
	private List<DeviceInfoBean> deviceList = new ArrayList<DeviceInfoBean>();
	private DeviceInfoAdapter mListAdapter;
	
	private BluetoothService btService;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_scan_device);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        
        //接收扫描到的设备列表
        deviceList = this.getIntent().getParcelableArrayListExtra(KEY_DEVICE_LIST);
        
        mListView = (ListView) this.findViewById(R.id.device_list);
        mListAdapter = new DeviceInfoAdapter(this, deviceList);
        mListView.setAdapter(mListAdapter);
        btService = MainActivity.mBtService; 
    }
    
    //选中的设备address
    private String selectedDeviceAddress;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	int position = this.mListView.getPositionForView(v);
    	DeviceInfoBean info = this.deviceList.get(position);
    	if(info.isPair()){
    		menu.add(0, 3, 1, "连接设备");
    		menu.add(0, 2, 2, "解除配对");
    	}
    	else{
    		menu.add(0, 1, 1, "配对");
    	}
    	selectedDeviceAddress = info.getDeviceAddress();
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	BluetoothDevice remoteDevice = mBtAdapter.getRemoteDevice(selectedDeviceAddress);;
    	switch(item.getItemId()){
    	case 1://配对
    		mListAdapter.setPireState(true, selectedDeviceAddress);
    		btService.requestPairing(remoteDevice, this);
    		break;
    	case 2://解除配对
    		mListAdapter.setPireState(false, selectedDeviceAddress);
    		btService.requestCancelPairing(remoteDevice);
	        mListAdapter.notifyDataSetChanged();
    		break;
    	case 3://连接设备
//    		mBtAdapter.cancelDiscovery();
//    		boolean connResult =  btService.connectionServer(remoteDevice);
//    		if(connResult == true){
//    			this.setResult(REQUEST_RESULT_CONNECT_OK);
//    		}else{
//    			this.setResult(REQUEST_RESULT_CONNECT_FAILED);
//    		}
//    		this.finish();
    		//
    		Intent intent = new Intent();
            // Need an activity context to open this in our task
    		LocalBluetoothManager mLocalManager = LocalBluetoothManager.getInstance(this);
            Context context = mLocalManager.getForegroundActivity();
            if (context == null) {
                // Fallback on application context, and open in a new task
                context = mLocalManager.getContext();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            intent.setClass(context, ConnectSpecificProfilesActivity.class);
            intent.putExtra(ConnectSpecificProfilesActivity.EXTRA_DEVICE, remoteDevice);
            context.startActivity(intent);
    		break;
    	}
    	return super.onContextItemSelected(item);
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//配对请求--响应
		if(requestCode == REQUEST_CODE_PAIR){
			boolean result = resultCode == 1 ? true : false;
			result = true;
			mListAdapter.setPireState(result, selectedDeviceAddress);
			mListAdapter.notifyDataSetChanged();
		}
	}
}