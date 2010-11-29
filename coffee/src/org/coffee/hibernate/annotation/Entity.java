package org.coffee.hibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface Entity {
	/**
	 *  实体名称 
	 */
	public String name() default "";	
	/**
	 *  序列名称
	 *  适用于Oracle数据库
	 */
	public String sequence() default ""; 
}
