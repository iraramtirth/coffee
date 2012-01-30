package org.droid.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import coffee.seven.SysConfig;

//import org.coffee.util.io.FileUtils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
/**
 * 下载服务  
 * @author wangtao
 */
public class VersionUpdateService extends Service{

	private String apkUrl;
	
	private String downloadDir = Environment.getDownloadCacheDirectory().getAbsolutePath();
	
	public class DownloadBinder extends Binder{
		public VersionUpdateService getService(){
			return VersionUpdateService.this;
		}
	};
	private DownloadBinder downloadBinder;
	/**
	 * startService的时候调用该方法
	 */
	@Override
	public IBinder onBind(Intent intent) {
		downloadBinder = new DownloadBinder();
		//apkUrl = intent.getStringExtra("apkUrl");
		apkUrl = SysConfig.VERSION_UPDATE;
		return downloadBinder;
	}

	public String download() throws Exception{
		if(apkUrl == null){
			return null;
		}
		String fileName = apkUrl.substring(apkUrl.lastIndexOf("/")+1);
		String filePath = downloadDir + "/" + fileName;
		InputStream in = null;
		BufferedOutputStream bout = null;
		try{
			URLConnection uc = new URL(apkUrl).openConnection();
			in = uc.getInputStream();
			int len = 0;
			byte[] data = new byte[1024 * 10];
			File file = new File(filePath);
			if(file.exists() == false){
				if(file.getParentFile().exists() == false){
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			bout = new BufferedOutputStream(new FileOutputStream(file,false));
			while((len = in.read(data)) != -1){
				bout.write(data, 0, len);
			}
			bout.flush();
			bout.close();
			in.close();
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			try{
				if(bout != null){
					bout.close();
				}
				if(in != null){
					in.close();
				}
			} catch(Exception e){}finally{
			}
		}
		return filePath;
	}
	
}
