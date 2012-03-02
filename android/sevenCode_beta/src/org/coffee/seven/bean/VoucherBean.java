package org.coffee.seven.bean;

import java.util.ArrayList;
import java.util.List;

import coffee.util.sqlite.annotation.Bean;
import coffee.util.sqlite.annotation.Id;
import coffee.util.sqlite.annotation.Transient;

/**
 * 优惠劵
 * @author coffee
 */
@Bean(name="coffee_voucher")
public class VoucherBean {
	@Id
	private int id;
	private String name;
	private String imgae;
	private int pid;
	@Transient
	private List<VoucherBean> children = new ArrayList<VoucherBean>();
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
	public List<VoucherBean> getChildren() {
		return children;
	}
	public void setChildren(List<VoucherBean> children) {
		this.children = children;
	}
}
