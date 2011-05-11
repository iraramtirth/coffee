package org.coffee.tools.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class CrawlerUtils {

	/**
	 * ====================
	 * [只尝试一次,不成功则返回空]
	 * =====================
	 * 获取URL的文本内容
	 * 其中部分特殊字符已经处理
	 * @param linkUrl : 监控页面的URL
	 * @throws IOException
	 */
	public synchronized static String getDocumentHtml(String linkUrl)
			{
		StringBuilder doc = new StringBuilder();
		try {
			URL url = new URL(linkUrl);
			URLConnection uc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				//处理特殊字符
				line = line.replace("&amp;", "&");
				doc.append(line);
			}
		} catch (MalformedURLException e) {
		} catch(IOException ioe){
			
		}
		return doc.toString();
	}
	/**
	 * =====================
	 * [尝试指定的次数]
	 * =====================
	 * 重载
	 * @param count : 失败后尝试的次数,最小为1
	 * @return : 返回问道的源码
	 */
	public synchronized static String getDocumentHtml(String linkUrl,int count){
		String srcDoc = "";
		try {
			//最低尝试一次
			if(count < 1){
				count = 1;
			}
			srcDoc = CrawlerUtils.getDocumentHtml(linkUrl);
		} finally {
			if(srcDoc == null || srcDoc.trim().length() == 0){
				for(int i=0;;i++){
					srcDoc = CrawlerUtils.getDocumentHtml(linkUrl);
					if(srcDoc != null && srcDoc.length() > 0){
						break;//成功
					}
					if(i == count){//尝试十次
						break;//失败
					}
				}//for end
			}//if  end
		}//finally end
		return srcDoc;
	}

	public static void main(String[] args) {
//		String pc = generatePcNo();
//		System.out.println(pc);
	}
}
