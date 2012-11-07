package org.coffee.tools.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
/**
 * 
 * @author wangtao
 */
public class EnumUtils {
	
	public static void main(String[] args) {
//		Field[] fields = Person.class.getDeclaredFields();
//		
//		for(Field  field : fields)
//		{
//			System.out.println(field.getName());
//		}
//		
//		Person p = Person.valueOf("AGE_19");
//		
//		System.out.println(p.codeValue);
//		
//		System.out.println(Person.values());
//		
//		for(Person ps : Person.values())
//		{
//			System.out.println(ps.codeValue);
//		}
		System.out.println("...");
		Person p19 = get(Person.values(), 20);
		System.out.println(p19.codeValue);
		
	}
	
	
	/**
	 * 
	 * @param enumType : 传入 enum.values方法的返回值
	 * @param codeValue ： 字段的值
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T get(T[] enumType , int codeValue)
	{
		
		try {
			Method values = enumType[0].getClass().getMethod("values", new Class[]{});
			T[] obj = (T[]) values.invoke(enumType[0], new Object[]{});
			
			for(T t : obj)
			{
				Field codeValueField = t.getClass().getDeclaredField("codeValue");
				
				codeValueField.setAccessible(true);
				
				codeValueField.get(t);
				
				if(String.valueOf(codeValue).equals(codeValueField.get(t) + ""))
				{
					return t;
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

enum Person
{
	AGE_19(19),
	AGE_29(20);
	int codeValue;
	Person(int codeValue)
	{
		this.codeValue = codeValue;
	}
}
