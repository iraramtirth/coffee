package org.coffee.tools.crawl;

import java.util.HashMap;
import java.util.Map;



/**
 * 抓取一个网站
 * @author wangtao
 */
public class CrawlerWebSite {
	private static final String baseUrl = "http://www.maimaibao.com";
	
	private static Map<String,String> allLinks = new HashMap<String,String>();
	
	public void run(String linkUrl, int depth){
		Map<String,String> linksMap = CrawlerUtils.selectA(linkUrl);
		for(String link : linksMap.keySet()){
			if(link.startsWith(baseUrl) && allLinks.containsKey(link) == false){
				allLinks.put(link, linksMap.get(link));
				CrawlerUtils.saveToFile(link);
				if(depth -1 >= 0){
					run(link, depth-1);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new CrawlerWebSite().run(baseUrl, 1);
		System.out.println(allLinks);
		System.out.println(allLinks.size());
	}
}
