package org.coffee.seven.bean;

import coffee.util.sqlite.annotation.Bean;
import coffee.util.sqlite.annotation.Id;

/**
 * 版本：主要用来检测版本更新 
 * @author wangtao
 */
@Bean(name="mmb_version")
public class VersionBean {
	@Id(isAuto=true)
	private int id;
	private long lastTime; //最后检测时间  [本地时间]
	
	public VersionBean(){
	}
	
	public VersionBean(long lastTime){
		this.lastTime = lastTime;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getLastTime() {
		return lastTime;
	}
	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
}
