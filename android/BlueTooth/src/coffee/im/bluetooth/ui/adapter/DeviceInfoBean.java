package coffee.im.bluetooth.ui.adapter;

public class DeviceInfoBean {
	
	private boolean isPair = false;	//是否配对
	
	private String deviceName ;			// 设备名称
	
	private String  deviceAddress;		//设备mac地址

	
	public boolean isPair() {
		return isPair;
	}

	public void setPair(boolean isPair) {
		this.isPair = isPair;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}
	
	@Override
	public String toString() {
		String infoText = (this.isPair() == true ? "[已配对]" : "[未配对]")
			+ " " + this.getDeviceName()
			+ " " + this.getDeviceAddress();
		return infoText;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceInfoBean other = (DeviceInfoBean) obj;
		//只要设备地址相等， 即返回true
		if (deviceAddress != null && deviceAddress.equals(other.deviceAddress)) {
			return true;
		}
		return false;
	}
	
	
}
