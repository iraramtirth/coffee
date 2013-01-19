package coffee.tools.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient {
	private static String encode = "UTF-8";

	/**
	 * ==================== [只尝试一次,不成功则返回空] ===================== 获取URL的文本内容
	 * 其中部分特殊字符已经处理
	 * 
	 * @param linkUrl
	 *            : 监控页面的URL
	 * @throws IOException
	 */
	private static String getDocumentHtml(String linkUrl) {
		StringBuilder doc = new StringBuilder();
		try {
			URL url = new URL(linkUrl);
			URLConnection uc = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream(), encode));
			String line = null;
			while ((line = in.readLine()) != null) {
				// 处理特殊字符
				line = line.replace("&amp;", "&");
				doc.append(line);
			}
		} catch (IOException ioe) {
		}
		return doc.toString();
	}

	/**
	 * ===================== [尝试指定的次数] ===================== 重载
	 * 
	 * @param count
	 *            : 失败后尝试的次数,最小为1
	 * @return : 返回问道的源码
	 */
	public static String openConnection(String linkUrl, int tryCount) {
		String docHtml = "";
		try {
			// 最低尝试一次
			if (tryCount < 1) {
				tryCount = 1;
			}
			docHtml = getDocumentHtml(linkUrl);
			// 设置编码
			String regex = "content=\".+?charset=(.+?)\"";
			Pattern ptn = Pattern.compile(regex);
			Matcher mat = ptn.matcher(docHtml);
			if (mat.find()) {
				encode = mat.group(1);
			}
		} finally {
			if (docHtml == null || docHtml.trim().length() == 0) {
				for (int i = 0; i < tryCount - 1; i++) {
					docHtml = getDocumentHtml(linkUrl);
					if (docHtml != null && docHtml.length() > 0) {
						break;// 成功
					}
				}// for end
			}// if end
		}// finally end
		return docHtml;
	}

	/**
	 * post方式提交数据
	 * @param baseUrl ： url
	 * @param paramsMap : 参数集
	 * @param enc : 编码方式
	 */
	public static String post(String baseUrl, Map<String,String> paramsMap){
		 StringBuilder doc = new StringBuilder();
		try {
			StringBuilder queryString = new StringBuilder();
			for(Iterator<String> it=paramsMap.keySet().iterator(); it.hasNext(); ){
				String paramName = it.next().toString();
				Object paramValue = paramsMap.get(paramName);
				if(paramValue == null){
					paramValue = "";
				}
				queryString.append(paramName)
						   .append("=")
						   .append(URLEncoder.encode(paramValue.toString(), encode))
						   .append("&");
			}
			if(queryString.length() > 0){
				queryString.deleteCharAt(queryString.length()-1);
			}
			URL url = new URL(baseUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestMethod("POST");
			//设置参数--begin
			uc.setDoOutput(true);
            byte[] b = queryString.toString().getBytes();
            uc.getOutputStream().write(b, 0, b.length);
            uc.getOutputStream().flush();
            uc.getOutputStream().close();
			//参数设置--end
            BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream(), encode));
			String line = null;
			while ((line = in.readLine()) != null) {
				// 处理特殊字符
				line = line.replace("&amp;", "&");
				doc.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc.toString();
	}
}
