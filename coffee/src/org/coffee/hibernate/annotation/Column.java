package org.coffee.hibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.FIELD) // 适用于方法;作用于field
public @interface Column {
	public String value(); //数据库对应的数据表的列名
}
