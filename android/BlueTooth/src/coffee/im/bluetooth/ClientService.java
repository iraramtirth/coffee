package coffee.im.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import coffee.im.bluetooth.activity.base.HandlerMgr;
import coffee.im.bluetooth.constant.ConstMsg;
import coffee.server.Config;
import coffee.server.tcp.TCPClient;

public class ClientService extends Service {

	private TCPClient tcpClient;
	private String username;

	private static ClientService instance;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		Log.d("thread-client-service", Thread.currentThread().getId() + "");
	}

	public static ClientService getInstance() {
		return instance;
	}

	/**
	 * 登录到Tcp服务器
	 */
	private void loginTcpServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (tcpClient != null) {
					tcpClient.close();
					tcpClient = null;
				}
				try {
					tcpClient = new TCPClient();
					tcpClient.connectServer(Config.getServerHost(), Config.PORT_TCP);
					if (tcpClient.getSocket().isConnected()) {
						tcpClient.setUsername(username);
						// 通知在线
						tcpClient.sendMessageOnline(1);
						HandlerMgr.sendMessage(ConstMsg.LOGIN_TCP, null);
					} else {
						HandlerMgr.sendMessage(ConstMsg.LOGIN_TCP, "失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public void sendMessage(final String message) {
		tcpClient.sendMessage(message);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			username = intent.getStringExtra("username");
			loginTcpServer();
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("client-service", "destroy");
		if (tcpClient != null) {
			tcpClient.close();
			tcpClient = null;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
