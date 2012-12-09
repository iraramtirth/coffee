package coffee.spring.ioc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


//要想获取注解的信息  ； 自定义的annotation必须 注解@Retention @Target
@Retention(RetentionPolicy.RUNTIME) 
@Target(ElementType.METHOD) // 目标是方法
public @interface Resource {
	public String name();
}
