package coffee.util.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
	/**
	 * 访问网指定的url
	 * @param urlString
	 * @return ：返回指定url的内容
	 */
	public static String connectUrl(String urlString){
		StringBuilder sb = new StringBuilder();
		try {
			URL url = new URL(handleUrl(urlString));
			URLConnection conn = url.openConnection();
			/*
			 * 如果网络设置为cmwap则访问www网站的话会抛出如下异常
			 * Host is unresolved: www.baidu.com:80
			 */
			InputStream in = conn.getInputStream();
			int len = 0;
			byte[] data = new byte[1024];
			while((len = in.read(data)) != -1){
				sb.append(new String(data, 0, len));
			}// 
			return sb.toString();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;	
	}
	
	/**
	 * 处理url: 添加 http: 前缀
	 * @param url
	 * @return
	 */
	private static String handleUrl(String url){
		if(url.matches("^http://.+") == false){
			url = "http://" + url;
		}
		return url;
	}
	/**
	 * 获取网络类型
	 * @return : MOBILE 
	 * @throws Exception : 如果网络不可用则抛异常，提醒用户进行相关的设置
	 */
	public static String getNetworkType(Activity context) throws Exception{
		ConnectivityManager conn = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conn.getActiveNetworkInfo();
		if(info == null || info.isAvailable() == false){
			throw new Exception("移动网络不可用...");
		}
		/**
		 * 获取移动网络的类型
		 */
		String typeName = conn.getActiveNetworkInfo().getTypeName();
		return typeName;
	}
	
	
	public static void main(String[] args) {
		handleUrl("http://baodu.com");
	}
}
