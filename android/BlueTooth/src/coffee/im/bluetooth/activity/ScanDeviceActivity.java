package coffee.im.bluetooth.activity;

import java.util.ArrayList;
import java.util.List;

import org.bluetooth.R;

import coffee.im.bluetooth.IActivity;
import coffee.im.bluetooth.adapter.DeviceInfoAdapter;
import coffee.im.bluetooth.adapter.bean.DeviceInfoBean;
import coffee.im.bluetooth.utils.BtUtils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * 扫描蓝牙设备
 * @author coffee
 */
public class ScanDeviceActivity extends Activity implements IActivity{
    
	private BluetoothAdapter mBtAdapter;
	
	private ListView mListView;
	
	private List<DeviceInfoBean> infoList = new ArrayList<DeviceInfoBean>();
	private DeviceInfoAdapter mListAdapter;
	
	private ProgressBar mProgressBar;
	private BtUtils btService;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_device);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        
        //发现设备
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        //扫描设备结束
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); 
        this.registerReceiver(mReceiver, filter);
        
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        //设备未开启，则直接退出
        if(mBtAdapter.getState() == BluetoothAdapter.STATE_OFF){
        	 Toast.makeText(this, "蓝牙设备未开启", Toast.LENGTH_SHORT).show();
        	 this.finish();
        }
        //开始搜索设备， 异步
        mBtAdapter.startDiscovery();
        
        mListView = (ListView) this.findViewById(R.id.device_list);
        mListAdapter = new DeviceInfoAdapter(this, infoList);
        mListView.setAdapter(mListAdapter);
        mProgressBar = (ProgressBar) this.findViewById(R.id.progress_bar);
        btService = MainActivity.btService; 
    }
    
    //选中的设备address
    private String selectedDeviceAddress;
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	int position = this.mListView.getPositionForView(v);
    	DeviceInfoBean info = this.infoList.get(position);
    	if(info.isPair()){
    		menu.add(0, 3, 1, "连接设备");
    		menu.add(0, 2, 2, "解除配对");
    	}
    	else{
    		menu.add(0, 3, 1, "连接设备");
    		menu.add(0, 1, 2, "配对");
    	}
    	selectedDeviceAddress = info.getDeviceAddress();
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	BluetoothDevice remoteDevice = mBtAdapter.getRemoteDevice(selectedDeviceAddress);
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
    		mBtAdapter.cancelDiscovery();
    		btService.connectionServer(remoteDevice);
    		this.finish();
    		break;
    	}
    	return super.onContextItemSelected(item);
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
	                
	                infoList.remove(info); //删除以前存在的。 避免重复
	                infoList.add(info);
	                mListAdapter.notifyDataSetChanged();
	            } 
	            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
	            	 mListAdapter.notifyDataSetChanged();
	            	 mProgressBar.setVisibility(View.GONE);
	            }
		}
	};
	
	@Override
	public void onBackPressed() {
		this.mBtAdapter.cancelDiscovery();
		if(mProgressBar.getVisibility() == View.VISIBLE){
			mProgressBar.setVisibility(View.GONE);
		}else{
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//配对请求--响应
		if(requestCode == REQUEST_CODE_PAIR){
			//bug
			mListAdapter.setPireState(true, selectedDeviceAddress);
			mListAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try{
			this.unregisterReceiver(mReceiver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}