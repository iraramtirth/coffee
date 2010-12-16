package org.coffee.struts.reflect;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import javax.servlet.http.HttpServletRequest;

import org.coffee.struts.Action;

public class RequestUtils{
	/**
	 * 将Action中的属性以及其值保存到
	 * @param thiz : 当前正在执行的action对象
	 * @param request : action中【当前】的request请求
	 */
	public static void setAttribute(Action thiz,HttpServletRequest request){
		try {
			BeanInfo bi = Introspector.getBeanInfo(thiz.getClass());
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for(PropertyDescriptor prop : props){
				try {
					Object value = prop.getReadMethod().invoke(thiz, new Object[]{});
					request.setAttribute(prop.getName(), value);
				}  catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
		} catch (IntrospectionException e1) {
			e1.printStackTrace();
		}
	}
}
