package org.droid.util;

/**
 * R工具类
 * @author coffee
 */
public class RUtils {
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
	 * @param rClassName	： 包名:比如 com.android.internal.R
	 * @param rDeclareClassName : 声明的类，比如说 id layout style 等
	 * @param fieldName	： 资源名
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
