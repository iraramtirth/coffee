package coffee.im.bluetooth.activity;

import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.server.Config;
import coffee.server.Online;
import coffee.server.udp.UDPClient;
import coffee.util.media.AudioRecorder;
import coffee.util.media.AudioTracker;

/**
 * 
 * @author coffee<br>
 *         2013上午11:57:05
 */
public class ConversationActivity extends BaseActivity {

	private UDPClient client;

	// 192.168.137.153 -- note2
	// 192.168.137.188 -- note3
	private String targetHost = "192.168.137.188";
	private EditText mHost;
	//
	private AudioRecorder audioRecorder;
	private AudioTracker audioTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = false;
		if (getIntent() != null) {
			targetHost = getIntent().getStringExtra("address");
		}
		super.onCreate(savedInstanceState);
	}

	private void init() {
		//
		audioRecorder = new AudioRecorder();
		audioTracker = new AudioTracker();
		//
		client = new UDPClient(Config.PORT_UDP, new UDPClient.MessageCallback() {
			@Override
			public void execute(byte[] data) {
				try {
					Log.d("recv", new String(data, "UTf-8"));
					audioTracker.write(data);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		});
		client.receiveMessage();
		client.broadcast(true);
	}

	@Override
	public void findViewById() {
		setContentView(R.layout.main_tab1);
		mHost = (EditText) findViewById(R.id.remote_address);
		mHost.setText(targetHost);
		//
		View init = findViewById(R.id.init);
		init.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				init();
			}
		});
		View startRecord = findViewById(R.id.start_record);
		startRecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				targetHost = mHost.getText().toString();
				audioRecorder.startRecord(true, audioRecorder.new Callback() {
					@Override
					public void execute(byte[] data) {
						//int port = Online.getPort(targetHost, 0);
						client.sendMessage(data, targetHost, Config.PORT_UDP);
					}
				});
			}
		});
		View stop = findViewById(R.id.stop_record);
		stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				audioRecorder.stop();
			}
		});

		View online = findViewById(R.id.online);
		final TextView onlineText = (TextView) findViewById(R.id.online_text);
		online.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onlineText.setText(Online.getItems(0).toString());
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.audioRecorder != null) {
			this.audioRecorder.stop();
		}
		if (this.audioTracker != null) {
			this.audioTracker.stop();
		}
		if (this.client != null) {
			this.client.close();
		}
	}

}
