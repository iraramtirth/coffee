package org.coffee.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  作用于Bean;类级别
 *  若一个Bean被Table注解了，则必须同时被Entity注解(该功能保留，但未实现，以后视情况扩展)
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface Table {
	/**
	 *  对应数据表的名称
	 */
	public String name();
	/**
	 *  对应数据表的序列；仅适用于Oracle数据库 
	 */
	public String sequence();
}
