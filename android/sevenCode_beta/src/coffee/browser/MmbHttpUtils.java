package coffee.browser;



import android.app.Activity;
import android.util.Base64;
import android.view.View;
import android.webkit.WebView;
import coffee.code.action.Alert;
import coffee.code.R;
import coffee.util.http.HttpClient;
import coffee.util.http.WebViewUtils;
import coffee.util.lang.StringUtils;

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
	public static String loadUrl(Activity context,WebView mWebView, String linkUrl) {
		String title = "";
		try {
			String doc = "";
			String url = linkUrl;//parserUrl(host, linkUrl);
			//http://mmb.cn/wap/upload/productImage/1314088464871.jpg
			if(url.matches(".+?\\.(jpg|gif|jpeg|png)+.*?")){
				charset = null;
				byte[] data = (byte[]) new HttpClient().get(url, 1); 
				doc = Base64.encodeToString(data, Base64.DEFAULT);
				doc = "<img src=\"data:image/jpeg;base64," + doc + "\" />";
			}else{
				doc = handleHtml(new HttpClient().get(url) + "");
				charset = "utf-8";	
			}
			//
			if(!StringUtils.isEmpty(doc)){
				mWebView.loadDataWithBaseURL(url, doc, mimeType, charset, url);
			}
			else{
				doc = "网络异常,请检查网络设置";
				Alert.show(context, "无法打开网页", new String[]{doc}, new View.OnClickListener[]{null,null},
						new Integer[]{null, R.drawable.alert_dialog_sure_selector});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

}
