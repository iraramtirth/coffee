package org.coffee.util.xml.parser.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;


@Retention(RUNTIME) 
public @interface GenericType {
    
    Class<?> type();
 
}


