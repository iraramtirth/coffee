package com.example.trafficstats;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.coffee.util.sys.TrafficStatsUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.TextView;

/**
 * 每10秒钟 往www.baidu.com发送一次请求
 * 
 * @author wangtao
 */
public class MainActivity extends Activity {

	private final String TAG = "MainActivity";

	private TextView mTextView;

	private String firstTime;
	private String lastTime;

	private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");

	// 初始接收，发送 (Mobile + WiFi)
	private long originRx, originTx;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.mTextView = (TextView) this.findViewById(R.id.data_info);
		this.mTextView.setText("正在进行流量统计");

		originRx = TrafficStatsUtils.getUidRxBytes();
		originTx = TrafficStatsUtils.getUidTxBytes();

		firstTime = sdf.format(new Date());

		startToSendData();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg != null) {
				lastTime = sdf.format(new Date());
				String dataText = firstTime + "\n接收-发送 ::" + msg.obj + "\n"
						+ lastTime;
				mTextView.setText(dataText);
			} else {
				mTextView.setText("无数据");
			}
		}

	};

	public void startToSendData() {
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {

				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						// 每次获取1.5kb的数据
						getNetBitmap(null);
						return null;
					}
				}.execute();

				// 接收的
				long receive = TrafficStatsUtils.getUidRxBytes(Process.myUid())
						- originRx;
				// 发送的
				long send = TrafficStatsUtils.getUidTxBytes(Process.myUid())
						- originTx;

				if (receive < 0) {
					receive = 0;
				}
				if (send < 0) {
					send = 0;
				}

				Message msg = Message.obtain();
				msg.obj = receive + "_" + send;
				mHandler.sendMessage(msg);

				Log.i(TAG, msg.obj.toString());

				mHandler.postDelayed(this, 1000 * 10);
			}
		}, 1000 * 1);
	}

	public static Bitmap getNetBitmap(String strUrl) {
		Bitmap bitmap = null;
		try {
			// java.net.Proxy proxy = new java.net.Proxy(Proxy.Type.HTTP, new
			// InetSocketAddress("10.0.0.172", 80));
			// HttpURLConnection con = (HttpURLConnection)
			// url.openConnection(proxy);

			URL url = new URL("http://www.baidu.com/img/baidu_logo.gif");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.connect();
			InputStream in = con.getInputStream();
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		return bitmap;
	}
}
