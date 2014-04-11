package coffee.im.bluetooth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import coffee.im.bluetooth.activity.base.HandlerMgr;
import coffee.im.bluetooth.constant.ConstMsg;
import coffee.server.Config;
import coffee.server.tcp.TCPClient;

public class ClientService extends Service {

	private TCPClient tcpClient;
	private String username;

	@Override
	public void onCreate() {
		super.onCreate();
		loginTcpServer();
	}

	/**
	 * 登录到Tcp服务器
	 */
	private void loginTcpServer() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				tcpClient = new TCPClient();
				tcpClient.connectServer(Config.SERVER_TCP, Config.PORT_TCP);
				if (tcpClient.getSocket().isConnected()) {
					tcpClient.setUsername(username);
					tcpClient.listenServer();
					// 通知在线
					tcpClient.sendMessageOnline(1);
					HandlerMgr.sendMessage(ConstMsg.LOGIN_TCP, null);
				}
			}
		}).start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		username = intent.getStringExtra("username");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
