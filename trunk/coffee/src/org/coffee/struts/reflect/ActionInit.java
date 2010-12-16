package org.coffee.struts.reflect;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.coffee.struts.Action;

/**
 * 初始化action中的字段 
 * @author wangtao
 */
public class ActionInit {
//	private String ss = "ssssssss";
//	
//	public String getSs() {
//		return ss;
//	}
//	public void setSs(String ss) {
//		this.ss = ss;
//	}
	public void init(Object thiz){
		try {
			BeanInfo bi = Introspector.getBeanInfo(thiz.getClass(), Action.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for(PropertyDescriptor prop : props){
				prop.getWriteMethod().invoke(thiz, new Object[]{null});
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	public static void main(String[] args) {
		ActionInit ai = new ActionInit();
		ai.init(ai);
	}
}
