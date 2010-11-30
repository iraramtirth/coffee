package org.coffee.struts.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface Path {
	/**
	 * 适用于Servlet中分派的方法 
	 */
	public String value();
}
