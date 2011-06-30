package org.coffee.tools.xml.jaxb;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

/**
 * Jaxb工具类
 *  
 * @author 王涛
 */
public class JaxbUtils {
	
	/**
	 * 将java.util.List<T> 转化为 xml
	 * @param <T>
	 * @param jaxbList : 源数据
	 * @param xmlPath ： 生成的xml文件的路径
	 * @param xsdPath ： 如果不进行校验则为null
	 * @param classesToBeBound
	 */
	public static <T> void marshall(JaxbList<T> jaxbList, String xmlPath,String xsdPath, Class<?>... classesToBeBound){   
        try {
        	Class<?>[] classArr = new Class[classesToBeBound.length + 1];
        	classArr[0] = jaxbList.getClass();
        	System.arraycopy(classesToBeBound, 0, classArr, 1, classesToBeBound.length);
        	//
			JAXBContext context = JAXBContext.newInstance(classArr);   
			Marshaller ms = context.createMarshaller();   
			//格式化输出
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// 设置 xmlns:xsi属性，只让其根节点显示
			ms.setProperty(Marshaller.JAXB_NO_NAMESPACE_SCHEMA_LOCATION, "");
			
			if(xsdPath != null){   
			    try {   
			        File xsdFile = new File(xsdPath);   
			        Schema schema =  SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)   
			        .newSchema(xsdFile);   
			        ms.setSchema(schema);   
			    } catch (Exception e) {   
			        e.printStackTrace();   
			    }   
			}   
			ms.marshal(jaxbList, new File(xmlPath));
		} catch (JAXBException e) {
			e.printStackTrace();
		}   
    }   
  
	/**
	 * 将xml转换为JaxbList
	 * 注意：JaxbList需要有setter方法 , 否则将unmarshall不到结果
	 * @param <T>
	 * @param clazz
	 * @param xmlPath
	 * @param xsdPath ： 如果不进行校验则为null
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> JaxbList<T> unmarshall(String xmlPath,String xsdPath, Class<?>... classesToBeBound){   
		try {
			Class<?>[] classArr = new Class[classesToBeBound.length + 1];
			classArr[0] = JaxbList.class;
			System.arraycopy(classesToBeBound, 0, classArr, 1, classesToBeBound.length);
			//
			JAXBContext context = JAXBContext.newInstance(classArr);   
			Unmarshaller ums = context.createUnmarshaller();   
			if(xsdPath != null){   
			    try {   
			        File xsdFile = new File(xsdPath);   
			        Schema schema =  SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI)   
			        .newSchema(xsdFile);   
			        ums.setSchema(schema);   
			    } catch (Exception e) {   
			        e.printStackTrace();   
			    }   
			}   
			JaxbList<T> jaxbList = (JaxbList<T>) ums.unmarshal(new File(xmlPath));   
			return jaxbList;
		} catch (JAXBException e) {
			e.printStackTrace();
		}   
		//默认返回一个空的List
		return new JaxbList<T>();
    }  


	
}
