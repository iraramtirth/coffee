package org.coffee.json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.coffee.sqlite.TUtils;
import org.coffee.sqlite.annotation.Column;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonUtils {
	
	@SuppressWarnings("unchecked")
	public static <T> T toBean(JSONObject json, Class<T> beanClass){
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
				String columnName = fieldName;
				if(column != null){
					columnName = column.name();
				}
				Object value = items.get(columnName);
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
	
	public static <T> List<T> toList(JSONArray jsonArr, Class<T> beanClass){
		List<T> lst = new ArrayList<T>();
		for(int i = 0; i < jsonArr.length(); i++){
			try {
				JSONObject jsonObj = jsonArr.getJSONObject(i);
				lst.add(toBean(jsonObj, beanClass));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return lst;
	}
}
