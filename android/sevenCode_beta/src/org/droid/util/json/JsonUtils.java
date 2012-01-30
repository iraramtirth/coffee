package org.droid.util.json;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.droid.util.TUtils;
import org.droid.util.sqlite.annotation.Column;
import org.json.JSONObject;

public class JsonUtils {
	
	@SuppressWarnings("unchecked")
	public static <T>  T toBean(JSONObject json, Class<T> beanClass){
		try {
			T obj = beanClass.newInstance();
			Map<String,Object> items = new HashMap<String, Object>();
			for(Iterator<String> it = json.keys(); it.hasNext();){
				String key = it.next();
				items.put(key, json.get(key));
			}
			for(Field field : beanClass.getDeclaredFields()){
				Column column = field.getAnnotation(Column.class);
				String fieldName = field.getName();
				if(column != null){
					fieldName = column.name();
				}
				Object value = items.get(fieldName);
				if(value != null){
					//递归
					if(value instanceof JSONObject){
						value = toBean((JSONObject)value, field.getClass());
					}else{
						TUtils.setValue(obj, fieldName, value);
					}
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
