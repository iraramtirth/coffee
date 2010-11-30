package org.coffee.hibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface Table {
	/**
	 * 数据表名 适用于所有的数据库
	 */
	public String name();
	/**
	 *  序列：适用于oracle数据库 
	 */
	public String sequence() default "";
}
