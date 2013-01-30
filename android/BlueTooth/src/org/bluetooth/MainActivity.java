package org.bluetooth;

import org.bluetooth.action.BluetoothService;
import org.bluetooth.adapter.ChatListAdapter;
import org.bluetooth.adapter.bean.ChatItemBean;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 主界面
 * 
 * @author coffee
 */
public class MainActivity extends Activity implements IActivity {

	private MainActivity context = this;

	private BluetoothAdapter mBtAdapter;

	private ChatListAdapter mChatListAdapter;

	public static BluetoothService btService;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		btService = new BluetoothService(this, mSocketHandler);

		mListView = (ListView) this.findViewById(R.id.chat_list);
		mChatListAdapter = new ChatListAdapter(this);
		mListView.setAdapter(mChatListAdapter);

		final EditText chatWords = (EditText) this
				.findViewById(R.id.chat_words);
		Button chatSend = (Button) this.findViewById(R.id.chat_send);
		chatSend.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String content = chatWords.getText().toString();
				if (content.length() > 0) {
					btService.sendChatMessage(content + "\n");
					mChatListAdapter
							.addChatItem(new ChatItemBean("Me", content));
					chatWords.setText("");
					mChatListAdapter.notifyDataSetChanged();
					mListView.setSelection(mChatListAdapter.getCount() - 1);
				}
			}
		});

		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction("android.bluetooth.device.action.PAIRING_REQUEST");
		this.registerReceiver(mReceiver, intentFilter);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 监听远程发起配对请求 -- //BluetoothDevice.ACTION_PAIRING_REQUEST
			if ("android.bluetooth.device.action.PAIRING_REQUEST"
					.equals(action)) {
				// Notification notification = new Notification(
				// android.R.drawable.stat_sys_data_bluetooth,
				// "xxxx",
				// System.currentTimeMillis());
				// notification.setLatestEventInfo(context, "contentTitle",
				// "contentText", null);
				// NotificationManager manager = (NotificationManager)
				// context.getSystemService(Context.NOTIFICATION_SERVICE);
				// manager.notify(111, notification);
				BluetoothDevice btDevice = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				btService.setPin(btDevice, "0000");
				btService.cancelPairingUserInput(btDevice);
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
				// AlertDialog.Builder builder = new
				// AlertDialog.Builder(context);
				// builder.setTitle("连接请求");
				// builder.setMessage("有远程设备向您发送文件，是否接收?");
				// builder.setPositiveButton("接收", new OnClickListener() {
				// public void onClick(DialogInterface dialog, int which) {
				if (msg.obj != null) {
					ChatItemBean item = (ChatItemBean) msg.obj;
					mChatListAdapter.addChatItem(item);
					mChatListAdapter.notifyDataSetChanged();
					mListView.setSelection(mChatListAdapter.getCount() - 1);
				}
				// }
				// });
				// builder.setNegativeButton("拒绝", null);
				// builder.show();
			}
		}
	}

	/**
	 * 菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.option_menu, menu);
		return true;
	}

	/**
	 * 菜单操作
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.scan_device: // 扫描设备
			Intent intent = new Intent();
			intent.setClass(this, ScanDeviceActivity.class);
			this.startActivity(intent);
			return true;
		case R.id.make_discoverable:// 使设备可见[即；该设备充当服务器]
			btService.requestDeviceDiscoverable();
			return true;
		case R.id.open_bluetooth:// 开启蓝牙
			if (mBtAdapter.getState() == BluetoothAdapter.STATE_OFF) {
				btService.requestDeviceOpen();
			} else {
				Toast.makeText(context, "蓝牙设备已经打开", Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_MAKE_DISCOVERABLE: // 使蓝牙可见
			btService.startServer(mBtAdapter); // 开启本机蓝牙，作为服务器
			break;
		}
	}

	/**
	 * 释放资源
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		btService.releaseAll();
	}
}
