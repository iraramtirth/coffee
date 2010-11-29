package org.coffee.struts;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.coffee.spring.ObjectManager;
import org.coffee.spring.ioc.annotation.Resource;
/**
 * action 的公共父类 
 * 总的控制器 
 * @author wangtao
 */
public abstract class Action extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		System.out.println("IOC..创建对象....");
		try {
			// IOC 创建对象(单例)
			ObjectManager.createObject(null);
			Map<String,Object> iocObjs = ObjectManager.getIocObject();
			// 保设置对象的生命周期：application
			for(String key : iocObjs.keySet()){
				super.getServletContext().setAttribute(key, iocObjs.get(key));
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
		super.init();
	}
	/**
	 * 该方法没有任何作用；运行时将会有其子类覆盖，并执行之
	 */
	public abstract String execute();
	/**
	 * get请求
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 参数映射
		this.paramsReflect(null, this.getClass(), request, response);
		// 请求转发
		String method = request.getParameter("method");
		if(method != null){
			
		}else{
			this.execute();
		}
	}
	// post 请求
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	/**
	 * 参数映射
	 * @param preName 参数前缀名：如  user.username 此时preName=user 
	 **/ 
	private Object paramsReflect(String preName, Class<?> clazz,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			BeanInfo bi = null;
			Object targetObj = null;
			// 处理 Servlet 里面的属性
			if (preName == null) {
				preName = "";
				bi = Introspector.getBeanInfo(clazz, Action.class);
				targetObj = this;
			} else {// 处理Model中的属性
				preName += ".";
				try {
					bi = Introspector.getBeanInfo(clazz, Object.class);
				} catch (Exception e) {
					//java.lang.Object not superclass of ....
					// 说明 Action 中的Field不需要映射
					return null;
				}
				targetObj = clazz.newInstance();
			}
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for (PropertyDescriptor prop : props) {
				// JavaBean的 Field 名
				String fieldName = preName + prop.getName();
				// JavaBean的 Field 类型
				String fieldType = prop.getPropertyType().getSimpleName().toLowerCase();
				// 参数值 ：String类型
				String paramValue = request.getParameter(fieldName);
				// 将参数值映射成适当的类型
				Object fieldValue = null;

				if (fieldType.contains("string")) {
					fieldValue = paramValue;
				} else if (fieldType.contains("int") || fieldType.contains("long")) {
					try {
						fieldValue = Integer.valueOf(paramValue);
					} catch (Exception e) {
						fieldValue = 0;
					}
				}//映射时间类型
				else if (fieldType.contains("date")) {
					SimpleDateFormat sdf = null;
					try {
						sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						fieldValue = sdf.parse(paramValue);
					} catch (Exception e) {
						try {// 如果不符合 yyyy-MM-dd HH:mm:ss 则重新parser字符串
							sdf = new SimpleDateFormat("yyyy-MM-dd");
							fieldValue = sdf.parse(paramValue);
						} catch (Exception e1) {
							sdf = new SimpleDateFormat("HH:mm:ss");
							fieldValue = sdf.parse(paramValue);
						}
					}
				} else {   /**
							*	type 自定义对象类型
							*	递归
						    **/	
					Class<?> fieldClazz = prop.getPropertyType();
					fieldValue = this.paramsReflect(fieldType, fieldClazz, request,
							response);
				}
				/**
				 *  当 fieldValue == null 的时候。 有可能会将通过IOC方式注入的对象重置为null
				 **/
				if(fieldValue != null){
					prop.getWriteMethod()
					.invoke(targetObj, new Object[] { fieldValue });
				}
			}
			return targetObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
