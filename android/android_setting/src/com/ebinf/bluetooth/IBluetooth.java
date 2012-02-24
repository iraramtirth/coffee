package com.ebinf.bluetooth;

import java.util.UUID;

/**
 * 蓝牙设备的相关接口 
 * @author wangtao
 */
public interface IBluetooth {
	public static String SDP_RECORD_NAME = "com.ebinf";
	public static UUID uuid = 
		UUID.fromString("0358b686-81b8-474a-af1f-38cbc9ae4754");
	
	public static String FLAG_FILE_END = "####"; //文件流传输完毕标志
	
	public final int REQUEST_ENABLE = 0;				//请求打开蓝牙
	public final int REQUEST_MAKE_DISCOVERABLE = 1;		//请求使设备可见
	public final int REQUEST_CODE_PAIR = 2;				//请求与远程设备配对
	
	public final int REQUEST_CODE_CONNECT = 3;
	public final int REQUEST_RESULT_CONNECT_OK = 4;   	//与远程设备连接成功
	public final int REQUEST_RESULT_CONNECT_FAILED = 5;	//。。。。连接失败
	public final String KEY_DEVICE_LIST = "KEY_DEVICE_LIST";  //跳转到设备列表。传递bundle的K名
}
