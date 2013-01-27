package org.coffee.util.framework;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
/**
 * 
 * @author coffee
 */
public class WifiUtils {
	private static String LOG_TAG = "WifiUtils";
	/**
	 * 打开wifi
	 * 返回打开是否成功 
	 */
	public static boolean open(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(wifi.isWifiEnabled() == false){
			wifi.setWifiEnabled(true);
			int i = 0;
			while(wifi.isWifiEnabled() == false){
				try {
					if(i++ == 10){
						return false;
					}
					Thread.sleep(1000 * 1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}//while
		}
		return true;
	}
	/**
	 *  关闭wifi 
	 */
	public static void close(Context context){
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(wifi.isWifiEnabled() == false){
			wifi.setWifiEnabled(false);
		}
	}
	
	public static String getIp(){
	    try {  
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {  
	            NetworkInterface intf = en.nextElement();  
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                InetAddress inetAddress = enumIpAddr.nextElement();  
	                if (!inetAddress.isLoopbackAddress()) {  
	                    return inetAddress.getHostAddress().toString();  
	                }  
	            }  
	        }  
	    } catch (SocketException ex) {  
	        Log.e(LOG_TAG, ex.toString());  
	    }  
	    return null;  
	}  
}
