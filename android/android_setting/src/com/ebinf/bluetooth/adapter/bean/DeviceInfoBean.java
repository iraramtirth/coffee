package com.ebinf.bluetooth.adapter.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceInfoBean implements Parcelable{
	
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

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(this.isPair == true ? 1:0);  
		dest.writeString(this.deviceName);  
		dest.writeString(this.deviceAddress);
	}
	
	public static final Parcelable.Creator<DeviceInfoBean> CREATOR = new Parcelable.Creator<DeviceInfoBean>() {
		@Override
		public DeviceInfoBean createFromParcel(Parcel source) {  
			DeviceInfoBean deviceInfo = new DeviceInfoBean();  
//			source.readBooleanArray(new boolean[]{deviceInfo.isPair});
			int isPare = source.readInt();
			deviceInfo.isPair = isPare == 0 ? false : true; 
			deviceInfo.deviceName = source.readString();  
			deviceInfo.deviceAddress = source.readString();  
            return deviceInfo;  
        } 
		@Override
        public DeviceInfoBean[] newArray(int size) {  
            return new DeviceInfoBean[size];  
        }  
	};
}
	
