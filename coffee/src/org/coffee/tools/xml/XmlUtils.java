package org.coffee.tools.xml;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.coffee.common.util.TUtils;
import org.coffee.common.util.io.Outer;

import cn.demo.bean.User;

/**
 * xml工具类
 * 主要针对coffee-jdbc 
 * @author 王涛
 */
public class XmlUtils {
	
	
	/**
	 * 将List-->转换为xml
	 * @param <T>
	 * @param items ： List结果集
	 * @param attr : 属性
	 */
	public static <T> void toXml(List<T> items,String xmlPath, String...attrs){
		if(items.size() == 0){
			return;
		}
		Class<?> clazz = items.get(0).getClass();
		Field[] fields = clazz.getDeclaredFields();
		StringBuilder sb = new StringBuilder();
		sb.append("<result>");
		String classAndArrrs = "<"+clazz.getSimpleName();
		int tag = 0;
		List<String> attrsList = Arrays.asList(attrs);
		for(String attr : attrsList){
			attr = org.coffee.jdbc.dao.util.TUtils.getColumnName(clazz, attr);
			classAndArrrs += " " + attr + "=\"{"+tag+"}\"";
			tag ++;
		}
		classAndArrrs += ">";
		tag = 0;
		for(T t : items){
			sb.append(classAndArrrs);
			for(Field field : fields){
				String fieldName = field.getName();
				Object fieldValue = TUtils.getValue(t, fieldName);
				String columnName = org.coffee.jdbc.dao.util.TUtils.getColumnName(t.getClass(), fieldName);
				//
				if(attrsList.contains(field.getName())){
					if(fieldValue == null){
						fieldValue = "";
					}
					int start = sb.lastIndexOf("{"+tag+"}");
					sb.replace(start, start + 3, fieldValue.toString());
					tag++;
				}else{
					//不显示值为null的字段
					if(fieldValue == null){
						continue;
					}
					sb.append("<").append(columnName).append(">")
					.append(fieldValue)
					.append("</").append(columnName).append(">");
				}
			}//inline for
			sb.append("</").append(clazz.getSimpleName()).append(">");
		}//outline for
		sb.append("</result>");
		Outer.setPath(xmlPath, true, true);
		Outer.p(sb.toString());
	}
	/**
	 * 将xml文档转换为List
	 * 
	 * @param xmlDoc
	 */
	public static void toList(String xmlDoc){
		
	}
	
	public static void main(String[] args){
		List<User> items = new ArrayList<User>();
		items.add(new User(1,"wangtao","bb"));
		toXml(items, "c:/df.xml", "id");
		
	}
}
