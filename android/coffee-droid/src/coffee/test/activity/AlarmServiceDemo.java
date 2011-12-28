package coffee.test.activity;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class AlarmServiceDemo extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		Log.e("SERVICE", "后台服务已经启动....");
//		String basePath = Environment.getExternalStorageDirectory().toString();
		String path = "/sdcard/alarms/GreatestHits.mp3";
		try {
			path = URLDecoder.decode(path, "UTF-8");
			FileInputStream fis = new FileInputStream(new File(path));
			/**
			 * getBaseContext().openFileInput(path);
			 * 该路径只能搜索应用程序的安装路径下的目录 -- （data/package_name/...)
			 * 如果读取以外的路径竟会报异常
			 * File xxx contains a path separator
			 */
			final MediaPlayer mp =  new MediaPlayer();
			//mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.setDataSource(fis.getFD());
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			Log.e("xxxxx", e.getMessage());
		} 
	}

	@Override
	public void onStart(Intent intent, int startId) {
		
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.e("xxx", "启动");
		return null;
	}

}
