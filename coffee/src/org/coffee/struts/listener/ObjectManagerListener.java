package org.coffee.struts.listener;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import org.coffee.spring.ObjectManager;

/**
 * 监听器
 * 通过AOP创建对象 
 * @author wangtao
 */
@WebListener
public class ObjectManagerListener implements
		javax.servlet.ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent event) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}
}
