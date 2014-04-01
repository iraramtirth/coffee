package coffee.server.cfg;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import coffee.server.servlet.bean.ServletBean;


/**
 * web.xml配置信息
 * 
 * @author coffee
 */
public class WebConfig {
	private static Logger log = Logger.getLogger(WebConfig.class.toString());
	/**
	 *  将 web.xml 中的 servlet 配置映射成 JavaBean 
	 *  String: servlet 名
	 *  ServletBean：
	 */
	@SuppressWarnings("unused")
	private Map<String,ServletBean> servlet = new HashMap<String,ServletBean>();
	
	/**
	 *   解析web.xml 文件
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public static void parse() throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		String path = System.getProperty("user.dir")+
				"/webapps/root/WEB-INF/web.xml";
		
		sp.parse(new File(path), new WebXmlHandler());	
	}
	
	static class WebXmlHandler extends DefaultHandler{
		private long millis;
		@Override
		public void startDocument() throws SAXException {
			log.info("开始解析web.xml");
			millis = System.currentTimeMillis();
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if("servlet".equals(qName)){
				
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			
		}

		@Override
		public void endDocument() throws SAXException {
			log.info("解析web.xml共耗时 "+ (System.currentTimeMillis()-millis) +"毫秒");
		}
	}
}
