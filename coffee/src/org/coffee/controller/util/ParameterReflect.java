package org.coffee.controller.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.coffee.common.util.DateUtils;
import org.coffee.common.util.StringUtils;
import org.coffee.common.util.TypeUtils;

/**
 * 参数反射 
 * 该类是一个工具类；
 * 在使用的过程中应该先执行 invoke方法，传入俩参数： 将从request获取的参数map进行解析
 * 并进行参数值的反射
 * @author wangtao
 */
public class ParameterReflect {
	private Logger log = Logger.getLogger(this.toString());
	/**
	 * 存放的是从表单获取的参数map
	 */
	private Map<String, Object> parameterMap = new HashMap<String, Object>();
	/**
	 * 存放的是Action中的[非primitive]属性以及其实例化对象
	 * 例如：
	 * private User madel;
	 * 那么targetMap.put("model",model.getClass().newInstance());
	 */
	private Map<String, Object> targetMap = new HashMap<String, Object>();
	/**
	 * 解析参数
	 * 初始化对象
	 */
	private void init(Map<String, Object> parameterMap,Object thiz){
		this.parameterMap = parameterMap;
		try {
			Class<?> clazz = thiz.getClass();
			for(String key : this.parameterMap.keySet()){
				String[] params = key.split("\\.");
				if(params.length > 1){
					String paramName = "";
					/**
					 * 剔除最后一个元素
					 * user.username ==> user
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 重载方法：主要是在 action 中用
	 * @param request
	 * @param actionOrModel
	 * @return
	 */
	public Object invoke(HttpServletRequest request, Class<?> actionOrModel){
		// string - string[]
		Map<String, String[]> params = request.getParameterMap();
		Map<String, Object> newParams = new HashMap<String, Object>();
		for(String key : params.keySet()){
			String[] values = (String[]) params.get(key);
			String value = "";
			for (int i = 0; i < values.length; i++) {
				value += values[i];
				if((i+1) < values.length){
					value += ",";
				}
			}
			newParams.put(key,value);
		}
		Object obj = null;
		try {
			obj = actionOrModel.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.init(newParams, obj);
		return this.invoke(newParams, obj);
	}
	
	
	/**
	 * 执行参数反射：
	 * 将action中的属性赋于从form/url取得的值
	 * @param action ：当前正在执行的action，或者带有setter | getter 的对象
	 */
	public <T> T invoke(Map<String, Object> parameterMap, T actionOrModel){
		this.init(parameterMap, actionOrModel);
		if(this.parameterMap.size() == 0){
			return null;
		}
		Class<?> clazz = actionOrModel.getClass();
		
		for(String key : this.parameterMap.keySet()){
			//primitive属性
			if(key.lastIndexOf(".") == -1){
				try {
					Field field = actionOrModel.getClass().getDeclaredField(key);
					Class<?> paramType = field.getType();
					Object value = this.parameterMap.get(key);
					//JDK1.4 不支持对primitive类型变量的反射
					if(paramType.toString().equals("int")){
						value = Integer.valueOf(value.toString());
					}
					if(paramType.toString().equals("double")){
						value = Double.valueOf(value.toString());
					}
					Method method = actionOrModel.getClass().getDeclaredMethod("set"+StringUtils
							.toUpperCaseFirstChar(key), new Class[]{paramType});
					method.invoke(actionOrModel, new Object[]{value});
				} catch (NoSuchFieldException e) {
					log.warning("Action中的字段["+key+"]不存在,无法进行参数映射...");
				} catch (Exception e) {
					e.printStackTrace();
				}  
			}else{//非private属性; 处理user.username之类的属性
				String paramName = "";
				Object objValue = null;
				int i = 0;
				String[] fields = key.split("\\.");
				for(String field : fields){
					i++;
					paramName += field;
					try {
						if(i < key.split("\\.").length){
							Method readMethod = clazz.getDeclaredMethod("get"+StringUtils.toUpperCaseFirstChar(field), new Class[]{});
							objValue = readMethod.invoke(actionOrModel, new Object[]{});
							if(objValue == null){
								objValue = targetMap.get(paramName);
								Method writeMethod = clazz.getDeclaredMethod("set"+StringUtils.toUpperCaseFirstChar(field),
										new Class[]{objValue.getClass()});
								//关联actionOrModel与其中的非primitive属性
								writeMethod.invoke(actionOrModel, new Object[]{objValue});
								if(objValue == null){
									throw new Exception("出错了。。。。");
								}
							}
						}else{
							Class<?> paramType = clazz.getDeclaredField(field).getType();
							Method method = clazz.getDeclaredMethod("set"+StringUtils.toUpperCaseFirstChar(field),
									new Class[]{paramType});
							Object mapVal = this.parameterMap.get(key);
							if(mapVal == null){
								continue;
							}
							//将mapVal中的值转换为适当的类型
							method.invoke(objValue, new Object[]{handleParamValue(clazz,field,mapVal)});
						}
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					} catch (NoSuchFieldException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
					clazz = objValue.getClass();
					paramName += ".";
				}
			}
		}
		return actionOrModel;
	}
	
	/**
	 * 处理参数值
	 */ 
	private Object handleParamValue(Class<?> clazz,String field,Object mapVal){
		Object paramValue = null;
		try {
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
				case String :
					paramValue = mapVal.toString();
					break;
				default :
					paramValue = mapVal;
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paramValue;
	}
	
	
	public static void main(String[] args) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("username", "111");
		map.put("user.username", "222");
		//map.put("user.child.username", "333");
		System.out.println();
		ParameterReflect pr = new ParameterReflect();
		User user = new User();
		user = pr.invoke(map, user);
		System.out.println(".......");
	}
	 
}

class User{
	
	private String username;
	private User user;
	
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