package org.coffee.tools.xml;

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
	
	public static <T> void marshall(JaxbList jaxbList,String xmlPath,String xsdPath){   
        try {
			JAXBContext context = JAXBContext.newInstance(jaxbList.getClass());   
			Marshaller ms = context.createMarshaller();   
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
  
	public static <T> JaxbList unmarshall(Class<JaxbList> clazz,String xmlPath,String xsdPath) throws Exception{   
        JAXBContext context = JAXBContext.newInstance(clazz);   
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
        JaxbList jaxbList = (JaxbList) ums.unmarshal(new File(xmlPath));   
        return jaxbList;   
    }  


	
}
