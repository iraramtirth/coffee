package org.coffee.util.lang;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 测试用的
 * 打印对象的值
 * @author coffee
 */
public class Printer {
	public static void info(Object obj){
		Field[] fields = obj.getClass().getDeclaredFields();
		try {
			for (Field field : fields) {
				field.setAccessible(true);
				Class<?> type = field.getType(); 
				if(type.getSimpleName().contains("List")){
					List<?> lst = (List<?>) field.get(obj);
					for(Object item : lst){
						info(item);//递归
					}
				}else{
					System.out.println("{"+field.getName()+"="+field.get(obj) + "}");
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
}
