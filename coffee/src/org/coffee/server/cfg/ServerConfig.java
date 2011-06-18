package org.coffee.server.cfg;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * server.xml 的映射信息
 * 
 * @author wangtao
 *
 */
public class ServerConfig {
	/**
	 *  http 端口
	 */
	private int port;
	
	private Logger log = Logger.getLogger(this.toString());
	
	// 读取配置文件
	public ServerConfig() throws ParserConfigurationException, SAXException, IOException{
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		/**
		 * server.xml 文件路径。 默认在 conf 文件夹下
		 */
		String configPath = System.getProperty("user.dir")
				+"/conf/server.xml";
		File serverXml = new File(configPath); 
		sp.parse(serverXml, new MyHandler());
	}
	
	
	public int getPort() {
		return port;
	}

	/**
	 * 解析server.xml
	 * @author Administrator
	 *
	 */
	class MyHandler extends DefaultHandler{
		private long millis;

		@Override
		public void startDocument() throws SAXException {
			log.info("开始记载Server.xml文件");
			this.millis = System.currentTimeMillis();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if("connection".equals(qName)){
				port = Integer.parseInt(attributes.getValue("port"));
			}
		}
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
		}

		@Override
		public void endDocument() throws SAXException {
			log.info("加载server.xml 耗时"+(System.currentTimeMillis() - millis)+"秒");
		}
	}
}
