package coffee.frame.bean;

/**
 * 蓝牙设备信息
 * 
 * @author coffee<br>
 *         2013下午1:40:37
 */
public class ContactBean {
	/**
	 * ID
	 */
	private int id;
	/**
	 * 联系人姓名
	 */
	private String name;
	/**
	 * 蓝牙的mac地址
	 */
	private String mac;
	
	//
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
}
