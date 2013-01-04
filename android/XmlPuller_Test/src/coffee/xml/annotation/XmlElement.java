package coffee.xml.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import coffee.xml.parser.TXmlUtils;
/**
 * 
 * 注意：Bean中的所有属性， 即使不加该属性， 也会进行反射的匹配
 * 
 * 匹配规则详见：{@link TXmlUtils#isElement(Class, String)}
 * 
 */
@Retention(RUNTIME)
@Target({ FIELD, METHOD })
public @interface XmlElement {
	String name() default "##default";

	boolean required() default false;

	String defaultValue() default "\u0000";

	Class<?> type() default DEFAULT.class;

	static final class DEFAULT {
	}
}
