package org.coffee.struts.listener;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.coffee.spring.ObjectManager;
import org.coffee.spring.ioc.annotation.Resource;
import org.coffee.struts.Action;

/**
 * 监听器
 * 通过AOP创建对象 
 * @author wangtao
 */
@WebListener
public class ObjectManagerListener implements
		javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try { 
			System.out.println("IOC..创建对象....");
			//创建对象 
			ObjectManager.createObject(null);
			//IOC注入
			Map<String,Object> iocObjs = ObjectManager.getIocObject();
			// 保设置对象的生命周期：application
			for(String key : iocObjs.keySet()){
				event.getServletContext().setAttribute(key, iocObjs.get(key));
			}
			// 为Action提供注入对象
			BeanInfo bi = Introspector.getBeanInfo(this.getClass(), Action.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for (PropertyDescriptor prop : props) {
				Resource resource = prop.getWriteMethod().getAnnotation(Resource.class);
				if (resource != null) {
					prop.getWriteMethod().invoke(this, iocObjs.get(prop.getName()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
	}

}
