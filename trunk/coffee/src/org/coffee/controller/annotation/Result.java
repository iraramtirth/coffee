package org.coffee.controller.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 适用于Servlet中分派的方法
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD)
public @interface Result {
	// 注意该属性值相对于WebRoot为根路径；开头不加 /
	public String page(); // 返回的页面 :

	public Type type() default Type.REDIRECT;	//请求转发的方式
	
	public enum Type{REDIRECT,DISPATCHER};
}


