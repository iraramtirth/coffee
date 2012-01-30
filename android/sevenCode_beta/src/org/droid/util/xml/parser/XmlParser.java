package org.droid.util.xml.parser;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.droid.util.lang.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 通用的普通xml解析器
 * 使用情况：
 * <result type="p">
		<item id="1" mmbprice="12.50" price="20">
			<name>NOKIA2011</name>
			<picList>
				<pic>http://192.168.1.20:8080/mmb_server/pic/NOKIA2011.png</pic>
			</picList>
			<info>诺基亚2012</info>
		</item>
			<item id="2" mmbprice="135.50" price="78.50">
			<name>Nokia 9070</name>
			<picList>
				<pic>icon</pic>
			</picList>
			<info>诺基亚2012非智能手机</info>
		</item>	<item id="2" mmbprice="135.50" price="78.50">
			<name>Nokia 9070</name>
			<pic>icon</pic>
			<info>诺基亚2012非智能手机</info>
		</item>
	</result>
 */
public class XmlParser{

	/**
	 * 每次parser只有一个全局的对象
	 */
	private XmlPullParser parser = Xml.newPullParser();
	
	public String xmlText;
	
	/**
	 * 设置需要pull的xml文本内容 
	 */
	public XmlParser(String xmlText){
		try {
			this.xmlText = xmlText;
			parser.setInput(new StringReader(xmlText));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  pull流文件
	 */
	public XmlParser(InputStream in){
		StringBuilder xmlText = new StringBuilder();
		BufferedInputStream bin = new BufferedInputStream(in);
		byte[] data = new byte[1024 * 10];
		int len = 0;
		try{
			while((len = bin.read(data)) != -1){
				xmlText.append(new String(data, 0, len));
			}
			bin.close();
			this.xmlText = xmlText.toString();
			parser.setInput(new StringReader(xmlText.toString()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	 
	/**
	 * 利用android内置的pull方式解析xml文件
	 * @param clazz
	 * @param listTag : 只有在递归的时候才用到，当用的时候
	 * <sale>
	 * 	<saleId></saleId>
	 *  <saleTitle></saleTitle>
	 *  <goodsList>
	 *  	<goods></goods>
	 *   	<goods></goods>
	 *   	<goods></goods>
	 *  </goodsList>
	 * </sale>
	 * 那么在调用的时候就会掺入  Goods.class, goodsList : 当解析遇到goodsList结束的时候便会结束递归
	 * 因为 parser对象是全局的
	 */
	public <T> List<T> pullerList(Class<T> clazz, String ...listTag){
		List<T> items = new ArrayList<T>();
		if(StringUtils.isEmpty(xmlText)){
			return items;
		}
		try {
			int event = parser.getEventType();
			String tagName = null;
			T t = null;
			while (event != XmlPullParser.END_DOCUMENT) {
				tagName = parser.getName();
				//配置参数为空，则按照class的name以及field的name进行匹配
				switch (event) {
				case XmlPullParser.START_TAG:// 2
					//Class级别
					if (TXmlUtils.isRootElement(clazz, tagName)) {
						t = clazz.newInstance();
					} else{	//Field级别
						if(TXmlUtils.isElement(clazz, tagName)) {
							String fieldName = TXmlUtils.getFieldName(clazz, tagName);
							if(fieldName == null){
								fieldName = tagName;
							}
							if(TXmlUtils.isListElement(clazz, fieldName)){
								Class<?> annoType = TXmlUtils.getGenericType(clazz, tagName);
								List<?> lst = this.pullerList(annoType, tagName);
								TXmlUtils.setValue(t, fieldName, lst);
							}
							else if(TXmlUtils.isMapElement(clazz, fieldName)){
								Map<String,String> map = pullerMap(clazz, tagName);
								TXmlUtils.setValue(t, fieldName, map);
							}else{
								String value = parser.nextText();
								TXmlUtils.setValue(t, fieldName, value);
							}
						}else{
							break;// 跳出该次witch，即：不执行取属性的操作
						}
					}
					// 取属性:
					for (int i = 0; i < parser.getAttributeCount(); i++) {
						String attrName = parser.getAttributeName(i);
						String value = parser.getAttributeValue(i);
						String fieldName = TXmlUtils.getFieldName(clazz, attrName);
						if(fieldName == null){
							fieldName = attrName;
						}
						TXmlUtils.setValue(t, fieldName, value);
					}
					break;
				case XmlPullParser.END_TAG:// 3
					if (TXmlUtils.isRootElement(clazz, tagName)) {
						items.add(t);
					}
					//如果listTag非空， 说明是递归调用该方法， 即当tagName == listTag[0]的时候，结束递归
					if(listTag.length > 0 && tagName.equals(listTag[0])){
						return items;
					}
					break;
				}// ++....
				event = parser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return items;
	}
	
	public <T> T pullerT(Class<T> clazz, String ... listTag){
		List<T> lst = pullerList(clazz, listTag);
		if(lst != null && lst.size() > 0){
			return lst.get(0);
		}
		return null;
	}
	
	private <T> Map<String,String> pullerMap(Class<T> clazz, String ...mapTag){
		Map<String,String> items = new HashMap<String, String>();
		try {
			int event = parser.getEventType();
			String tagName = null;
			while (event != XmlPullParser.END_DOCUMENT) {
				tagName = parser.getName();
				//配置参数为空，则按照class的name以及field的name进行匹配
				switch (event) {
				case XmlPullParser.START_TAG:// 2
					if("item".equals(tagName)){
						String key = parser.getAttributeValue(0);
						String value = parser.nextText();
						items.put(key, value);
					}
					break;
				case XmlPullParser.END_TAG:// 3
					//如果listTag非空， 说明是递归调用该方法， 即当tagName == listTag[0]的时候，结束递归
					if(mapTag.length > 0 && tagName.equals(mapTag[0])){
						return items;
					}
					break;
				}
				event = parser.next();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return items;
	}
}
