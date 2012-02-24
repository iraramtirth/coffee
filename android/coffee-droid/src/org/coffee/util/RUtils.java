package org.coffee.util;

import java.lang.reflect.Field;

import android.util.Log;

/**
 * R工具类
 * @author coffee
 */
public class RUtils {
	//"com.android.internal.R"
	/**
	 * 遍历R中的所有类得变量
	 */
	public static void print(String rClassName){
		try{
			Class<?> r = Class.forName(rClassName);
			Class<?>[] rInlineClass = r.getDeclaredClasses();//内部类
			for(Class<?> cls : rInlineClass){
				Class<?>[] rIIClass = cls.getDeclaredClasses();//内部类的内部类
				if(rIIClass.length > 0){
					print(rIIClass.getClass().getName());
				}else{//没有内部类了，则打印信息
					Field[] fields = cls.getDeclaredFields();
					for(Field field : fields){
						field.setAccessible(true);
						String fieldName = field.getName();
						Object fieldValue = field.get(null);
						Log.i("o", fieldName + " <> " + fieldValue);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 获取资源ID
	 * 用法：getResId("com.android.internal.R", "id", "title_container");
	 * @param rClassName	： 包名:比如 com.android.internal.R
	 * @param rDeclareClassName : 声明的类，比如说 id layout style 等
	 * @param fieldName	： 资源名
	 * @return
	 */
	public static int getResId(String rClassName, String rDeclareClassName, String resName){
		int resId = 0;
		try{
			Class<?> r = Class.forName(rClassName);
			Class<?>[] rInlineClass = r.getDeclaredClasses();//内部类
			for(Class<?> cls : rInlineClass){
				Class<?>[] rIIClass = cls.getDeclaredClasses();//内部类的内部类
				if(rIIClass.length > 0){
					getResId(rIIClass.getClass().getName(), rDeclareClassName, resName);
				}else{
					if(cls.getName().equals(rClassName+"$"+rDeclareClassName)){
						resId = cls.getField(resName).getInt(null);
						break;
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resId;
	}
	
	/**
	 * 获取资源ID
	 * 用法：getResId("com.android.internal.R$id", "title_container");
	 * @return
	 */
	public static int getResId(String rClassName, String resName){
		int resId = 0;
		try{
			Class<?> r = Class.forName(rClassName);
			resId = r.getField(resName).getInt(null);
		}catch(Exception e){
			e.printStackTrace();
		}
		return resId;
	}
}
