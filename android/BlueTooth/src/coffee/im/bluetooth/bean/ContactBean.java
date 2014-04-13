package coffee.im.bluetooth.bean;

/**
 * 联系人
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
	 * 蓝牙的mac地址/IP地址
	 */
	private String address;
	
	private int port;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
