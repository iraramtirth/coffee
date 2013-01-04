package com.xml.puller.bean;

import coffee.xml.annotation.XmlElement;
import coffee.xml.annotation.XmlRootElement;

/**
 * 百度地图的搜索结果
 * 
 * @author coffee
 * 
 *         2013-1-4 上午8:32:09
 * 
 *         <result> 
 *         		<name>麦当劳(天通苑餐厅)</name> 
 *         		<location> 
 *         			<lat>40.080889</lat>
 *         			<lng>116.419102</lng> 
 *         		</location>
 *         		<address>昌平区昌平区天通苑东三区2号国泰百货内(近蜀国演义)</address>
 *         		<telephone>(010)84817930</telephone>
 *        		<uid>c6ba175b35f607d3cafa5fe4</uid>
 *         		<detail_url>http://api.map.baidu.com
 *         			/place/detail?uid=c6ba175b35f607d3cafa5fe4
 *         			&amp;amp;output=html&amp;amp;source=placeapi</detail_url>
 *         		<tag>快餐/简餐,天通苑</tag> 
 *         </result>
 */
@XmlRootElement(name="result")
public class BaiduMapResult {
	private String name;
	private String lat;
	private String lng;
	private String address;
	private String telephone;
	private String uid;
	@XmlElement(name = "detail_url")
	private String detailUrl;
	private String tag;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDetailUrl() {
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
