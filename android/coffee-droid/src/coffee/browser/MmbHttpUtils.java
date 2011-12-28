package coffee.browser;

import coffee.activity.BrowserActivity;
import coffee.util.lang.RegexUtils;

import android.app.Activity;
import android.util.Base64;

/**
 * mmb.cn 网站相关工具类
 * 
 * @author wangtao
 */
public class MmbHttpUtils extends WebViewUtils {

	private static String charset = "UTF-8";
	private static String mimeType = "text/html";
	private static StringBuilder doc = new StringBuilder();
	
	
	/**
	 * @param oriHtml
	 *            : 原始html
	 * @return : 返回处理后的html
	 */
	public static String handleHtml(String oriHtml) {
		doc.setLength(0);// 清空
		doc.append(oriHtml);
		if (doc.indexOf("</head>") > 0) {
			doc.insert(doc.indexOf("</head>"), metaLow);
		}
		if(doc.indexOf("</style>") > 0){
			doc.insert(doc.indexOf("</style>"), cssRemoveHighLight);
		}
		return doc.toString();
	}

	
	/**
	 * @param url
	 *            : 该URL是不需要\或者是已经处理过得标准的、完整的URL 即是：主机 +URI
	 * @return : 返回网页的标题<title></title>
	 */
	public static String loadUrl(BrowserActivity context, String host, String linkUrl) {
		String title = "";
		try {
			String doc = "";
			String url = parserUrl(host, linkUrl);
			//http://mmb.cn/wap/upload/productImage/1314088464871.jpg
			if(url.matches(".+?\\.(jpg|gif|jpeg|png)+.*?")){
				charset = null;
				byte[] data = BrowserUtils.getImage((Activity)context, url); 
				doc = Base64.encodeToString(data, Base64.DEFAULT);
				doc = "<img src=\"data:image/jpeg;base64," + doc + "\" />";
			}else{
				doc = handleHtml(BrowserUtils.get((Activity)context, url));
				title = RegexUtils.match(doc, "<title>(.+?)</title>",1);
				charset = "utf-8";	
			}
			//即使记录
			BrowserHistory.put(linkUrl, doc);
			context.getWebView().loadDataWithBaseURL(url, doc, mimeType, charset, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}
	

}
