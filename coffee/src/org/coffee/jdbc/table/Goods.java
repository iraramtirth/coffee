package org.coffee.jdbc.table;

/**
 * 商品 
 * @author 王涛
 */
public class Goods {
	public String id;
	public String name;	// 商品名
	public String price;	// 价格
	public String spec;	// 规格
	public String brand;	// 品牌
	public String type;	// 型号
	public String unit;	// 单位
	public String imgUrl;	// 图片URL
	public String date;	// 采集时间
	public String mkind;	// 主分类
	public String ckind;	// 次分类
	public String remark;	// 参数/说明/描述
	public String linkUrl;	// 连接地址
	
	
	public final String tableName = "wcc_goods";
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMkind() {
		return mkind;
	}
	public void setMkind(String mkind) {
		this.mkind = mkind;
	}
	public String getCkind() {
		return ckind;
	}
	public void setCkind(String ckind) {
		this.ckind = ckind;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLinkUrl() {
		return linkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public static void main(String[] args) {
		System.out.println(14.0/31.0);
		double price = 4500 * 14/31 + 6000 * 17/31;	//5322.0
		System.out.println(price);
		price = 4500 * 15/31 + 6000 * 16/31;	//5273.0
		System.out.println(price);
		price = 4500 * 14/31 + 5500 * 17/31;	//5048.0
		System.out.println(price);
		price = 4500 * 15/31 + 5500 * 16/31;	//5015.0
		System.out.println(price);
		
		System.out.println(4500.0 / 0.8);
	}
}
