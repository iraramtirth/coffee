package coffee.browser.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * webview浏览记录
 * @author coffee
 */
public class BrowserHistory {
	/**************************************
	 * 注意该变量非常重要
	 * 当前页面的link
	 * ************************************
	 */
	private static String currentLink = "";	
	/**
	 * k : URL
	 * v : 网页内容
	 */
	private static Map<String, String> hisMap = new HashMap<String, String>();
	/**
	 * 记录【后退】栈中的URL
	 */
	private static Stack<String> backStack = new Stack<String>();
	/**
	 * 记录【前进】栈中的URL
	 */
	private static Stack<String> forwardStack = new Stack<String>();
	
	
	/**
	 * 已经浏览过的记录
	 * @return : 返回网页的linkURL ，如果返回null， 则说明没有浏览记录了
	 */
	public static String goBack(){
		if(backStack.empty()){
			return null; 
		}
		//如果当前页面的link存在于back栈中，则..
		if(currentLink.equals(backStack.lastElement())){
			//将[后退栈]中的记录移动到[前进栈]中
			forwardStack.push(backStack.pop());
		}
		if(backStack.empty()){
			return null;
		}
		currentLink = backStack.lastElement();
		return currentLink;
	}
	
	public static String goForward(){
		if(forwardStack.empty()){
			return null;
		}
		//如果当前页面的link存在于forward栈中，则..
		if(currentLink.equals(forwardStack.lastElement())){
			////将[forward栈]中的记录移动到[back栈]中
			backStack.push(forwardStack.pop());
		}
		//如果[已经到最新的linkUrl记录了]
		if(forwardStack.empty()){
			return null;
		}
		currentLink = forwardStack.lastElement();
		return currentLink;
	}
	
	/**
	 * 新打开的链接, 增加记录
	 */
	public static void put(String linkUrl,String doc){
		hisMap.put(linkUrl, doc);
		backStack.push(linkUrl);
		currentLink = linkUrl;// ******** 当前linkUrl **********
	}
	/**
	 * 从历史记录中
	 * 读取指定URL的内容
	 */
	public static String browser(String linkUrl){
		String doc = hisMap.get(linkUrl);
		return doc;
	}
	
	/**
	 * @return : 当前页面linkUrl
	 */
	public static String getCurrentUrl(){
		return currentLink;
	}
}
