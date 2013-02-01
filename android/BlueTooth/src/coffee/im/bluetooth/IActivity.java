package coffee.im.bluetooth;

import java.util.UUID;

public interface IActivity {
	
	public static UUID uuid = 
		UUID.fromString("92411435-1a7e-400f-b09a-9d6f0d08382a");
	
	public static String SDP = uuid.toString();
	
	public final int REQUEST_ENABLE = 0;			//请求打开蓝牙
	public final int REQUEST_MAKE_DISCOVERABLE = 1;	//请求使设备可见
	public final int REQUEST_CODE_PAIR = 2;			//请求与远程设备配对
}
