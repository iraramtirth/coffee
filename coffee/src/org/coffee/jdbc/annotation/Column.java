package org.coffee.jdbc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作用于字段；Field级别  
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD) 
public @interface Column {
	/**
	 * 对应的数据表的列名
	 */
	public String value(); 
}
