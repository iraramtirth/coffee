package org.coffee.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
//	public static final String CACHE_GOODS = "CACHE_GOODS";
	//27e353e6-38ba-4f7f-9e70-fab04ce4bada
	private static final String WCC_KEY = "86041fa5863a48fd3d6ad6b570eb63e6";
	
	public static String getKey(){
		InputStream xmlCfg = PropertiesUtils.class.getResourceAsStream("/wcc/wcc.property");
		Properties props = new Properties();
		try {
			props.load(xmlCfg);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String key = props.getProperty("key");
		if(key == null || key.trim().length() == 0){
			return WCC_KEY;
		}else{
			return key;
		}
	}
	
}
