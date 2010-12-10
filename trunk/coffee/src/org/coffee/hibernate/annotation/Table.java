package org.coffee.hibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface Table {
	public String name();	//表明
	public String sequence();//数据表对应的序列 Oracle 数据库
}
