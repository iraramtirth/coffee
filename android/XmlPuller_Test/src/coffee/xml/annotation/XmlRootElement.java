package coffee.xml.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;

/**
 *  注意：除了xml最顶级的节点
 *  
 *  其他元素都用  @see XmlElement 
 */
@Retention(RUNTIME)
@Target({TYPE})
public @interface XmlRootElement {
   
    String name() default "##default";
}
