package org.coffee.controller.introspector;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.coffee.controller.Action;

/**
 * BeanInfo 工具类
 * 主要用于操纵
 * BeanInfo Introspector PropertyDescriptor
 * @author wangtao
 */
public class BeanUtils {
	
	public static PropertyDescriptor[] getPropertyDescriptors(Object thiz){
		PropertyDescriptor[] props = null;
		try {
			BeanInfo bi = Introspector.getBeanInfo(thiz.getClass(),	Action.class);
			props = bi.getPropertyDescriptors();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return props;
	}
}
