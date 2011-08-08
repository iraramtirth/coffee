package org.coffee.persistence.spi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.ProviderUtil;

import org.coffee.persistence.impl.EntityManagerFactoryImpl;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class CoffeePersistenceProvider implements PersistenceProvider{
	
	EntityManagerFactory emf;
	
	public EntityManagerFactory createContainerEntityManagerFactory(
			PersistenceUnitInfo info, Map<String,String> map) {
		System.out.println("xx");
		return null;
	}
	
	public EntityManagerFactory createEntityManagerFactory(String emName,
			Map<String,String> map) {
		// 取得配置文件
		if(map == null){
			map = new HashMap<String, String>();
		}
		try {
			String persistenceXml = this.getClass().getResource("/").toString();
			//解码:win系统下的路径若带有中文，则会编码为%20
			persistenceXml = URLDecoder.decode(persistenceXml,"UTF-8");
			Document doc = new SAXReader().read(persistenceXml+"/META-INF/persistence.xml");
			//根节点
			Element root = doc.getRootElement();
			//遍历 persistence-unit 节点 
			for(Element psUnit : root.elements("persistence-unit")){
				System.out.println();
				//查找指定的persistence-unit
				if(emName.equals(psUnit.attribute("name").getValue())){
					//provider
//					Element provider = psUnit.element("provider");
					//properties节点
					Element props = psUnit.element("properties");
					//property节点
					for(Element prop : props.elements("property")){
						map.put(prop.attribute("name").getValue(),prop.attribute("value").getValue());
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return new EntityManagerFactoryImpl(map);
	}

	@Override
	public ProviderUtil getProviderUtil() {
	
		return null;
	}

	
}
