package org.coffee.hibernate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  作用于类：Type级别
 *  被Entity注解的类，将会在程序运行时获取其中的信息
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.TYPE)
public @interface Entity {
	/**
	 * 实体名，预留功能；未实现 
	 */
	public String name() default "";
}
