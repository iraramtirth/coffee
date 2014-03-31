package coffee.im.bluetooth;


public interface IActivity {

	public final int REQUEST_ENABLE = 0; // 请求打开蓝牙
	public final int REQUEST_MAKE_DISCOVERABLE = 1; // 请求使设备可见
	public final int REQUEST_CODE_PAIR = 2; // 请求与远程设备配对
}
