package coffee.seven.bean;

import org.droid.util.sqlite.annotation.Bean;

/**
 * 优惠劵
 * @author coffee
 */
@Bean(name="coffee_voucher")
public class VoucherBean {
	private int id;
	private String name;
	private String imgae;
	private int pid;
	
	///
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
	public String getImgae() {
		return imgae;
	}
	public void setImgae(String imgae) {
		this.imgae = imgae;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
}
