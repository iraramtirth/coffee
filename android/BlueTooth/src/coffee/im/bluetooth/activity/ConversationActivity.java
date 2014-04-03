package coffee.im.bluetooth.activity;

import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import coffee.im.bluetooth.R;
import coffee.im.bluetooth.activity.base.BaseActivity;
import coffee.server.Config;
import coffee.server.Online;
import coffee.server.udp.Client;
import coffee.util.media.AudioRecorder;
import coffee.util.media.AudioTracker;

/**
 * 
 * @author coffee<br>
 *         2013上午11:57:05
 */
public class ConversationActivity extends BaseActivity {

	private Client client;

	// 192.168.137.18 -- note2
	// 192.168.137.2 -- note3
	private int localPort = Config.PORT_UDP;
	private int targetPort = Config.PORT_UDP;
	private String targetHost = "192.168.137.2";

	//
	private AudioRecorder audioRecorder;
	private AudioTracker audioTracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.activityToMgr = false;
		super.layoutResource = R.layout.main_tab1;
		super.onCreate(savedInstanceState);
		//
		audioRecorder = new AudioRecorder();
		audioTracker = new AudioTracker();
		//
		client = new Client(localPort, new Client.MessageCallback() {
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
	}

	@Override
	public void doInitView() {

		View startRecord = findViewById(R.id.start_record);
		startRecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				audioRecorder.startRecord(true, audioRecorder.new Callback() {
					@Override
					public void execute(byte[] data) {
						client.sendMessage(data, targetHost, targetPort);
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
				onlineText.setText(Online.getItems().toString());
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.audioRecorder.stop();
		this.audioTracker.stop();
		this.client.close();
	}

}
