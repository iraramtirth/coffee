package org.coffee.struts.reflect;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.coffee.util.DateUtils;
import org.coffee.util.StringManager;
import org.coffee.util.TypeUtils;

import cn.demo.bean.User;

/**
 * 参数反射 
 * 该类是一个工具类；
 * 在使用的过程中应该先执行 parserParameter 将从request获取的参数map进行解析
 * 然后调用 invoke 进行参数值的反射
 * @author wangtao
 */
public class ParamaterReflect {
	private String username;
	private User user ;
	
	/**
	 * 存放的是从表单获取的参数map
	 */
	private Map<String, Object> parameterMap = new HashMap<String, Object>();
	/**
	 * 存放的是Action中的属性以及其实例化对象
	 * 例如：
	 * private User madel;
	 * 那么targetMap.put("model",model.getClass().newInstance());
	 */
	private Map<String, Object> targetMap = new HashMap<String, Object>();
	
	/**
	 * 解析参数
	 * 初始化对象
	 */
	public void parserParameter(Map<String, Object> parameterMap,Object thiz){
		this.parameterMap = parameterMap;
		try {
			for(String key : this.parameterMap.keySet()){
				String[] params = key.split("\\.");
				if(params.length > 1){
					Class<?> clazz = thiz.getClass();
					String paramName = "";
					/**
					 * 剔除最后一个元素
					 */
					String[] newParams = Arrays.copyOf(params, params.length-1);
					for(int i=0; i<newParams.length; i++){
						paramName += newParams[i];
						Object paramInstance;
						paramInstance = clazz.getDeclaredField(newParams[i]).getType().newInstance();
						this.targetMap.put(paramName, paramInstance);
						clazz = paramInstance.getClass();
						if(i+1 < newParams.length){
							paramName += ".";
						}
					}
				}
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 执行参数反射
	 * @param target
	 * @param value
	 */
	public void invoke(Object action) throws Exception{
		if(this.parameterMap.size() == 0){
			return;
		}
		String paramName = "";
		try {
			for(String key : this.parameterMap.keySet()){
				Object base = action;
				if(key.lastIndexOf(".") == -1){
					Class<?> paramType = base.getClass().getDeclaredField(key).getType();
					Method method = base.getClass().getDeclaredMethod("set"+StringManager.toUpperCaseFirstChar(key), new Class[]{paramType});
					method.invoke(base, new Object[]{this.parameterMap.get(key)});
				}else{
					Class<?> clazz = base.getClass();
					paramName = "";
					Object objValue = null;
					int i = 0;
					for(String field : key.split("\\.")){
						i++;
						paramName += field;
						if(i < key.split("\\.").length){
							Method readMethod = clazz.getDeclaredMethod("get"+StringManager.toUpperCaseFirstChar(field), new Class[]{});
							objValue = readMethod.invoke(base, new Object[]{});
							if(objValue == null){
								objValue = targetMap.get(paramName);
								Method writeMethod = clazz.getDeclaredMethod("set"+StringManager.toUpperCaseFirstChar(field),
										new Class[]{objValue.getClass()});
								writeMethod.invoke(base, new Object[]{objValue});
								if(objValue == null){
									throw new Exception("出错了。。。。");
								}
							}
						}else{
							Class<?> paramType = clazz.getDeclaredField(field).getType();
							Method method = clazz.getDeclaredMethod("set"+StringManager.toUpperCaseFirstChar(field),
									new Class[]{paramType});
							Object paramValue;
							Object mapVal = this.parameterMap.get(key);
							System.out.println(key+"\t"+mapVal);
							if(mapVal == null){
								continue;
							}
							switch(TypeUtils.getMappedType(clazz.getDeclaredField(field))){
								case Integer :
									paramValue = Integer.valueOf(mapVal+"");
									break;
								case Long :
									paramValue = Long.valueOf(mapVal+"");
									break;
								case Date :
									paramValue = DateUtils.parse(mapVal);
									break;
								default:
									paramValue = mapVal;
									break;
							}
							method.invoke(base, new Object[]{paramValue});
						}
						clazz = objValue.getClass();
						paramName += ".";
						base = objValue;
					}
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().contains("NoSuchMethodException")){
				throw new Exception("属性"+paramName+"没有发现");
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("username", "111");
		map.put("user.username", "222");
		map.put("user.child.username", "333");
		System.out.println();
		ParamaterReflect pr = new ParamaterReflect();
		//先解析参数
		pr.parserParameter(map, pr);
		//
		pr.invoke(pr);
	}
	
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
