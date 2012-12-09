package coffee.tools.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerUtils {

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
	public static String getDocumentHtml(String linkUrl, int tryCount) {
		String docHtml = "";
		try {
			// 最低尝试一次
			if (tryCount < 1) {
				tryCount = 1;
			}
			docHtml = CrawlerUtils.getDocumentHtml(linkUrl);
			// 设置编码
//			String regex = "content=\".+?charset=(.+)\"";
//			Pattern ptn = Pattern.compile(regex);
//			Matcher mat = ptn.matcher(docHtml);
//			if (mat.find()) {
//				encode = mat.group(1);
//			}
		} finally {
			if (docHtml == null || docHtml.trim().length() == 0) {
				for (int i = 0; i < tryCount - 1; i++) {
					docHtml = CrawlerUtils.getDocumentHtml(linkUrl);
					if (docHtml != null && docHtml.length() > 0) {
						break;// 成功
					}
				}// for end
			}// if end
		}// finally end
		return docHtml;
	}

	/**
	 * 去除了html标记 详见 {@link getDocumentHtml(String linkUrl, int tryCount)}
	 * 
	 * @param linkUrl
	 * @param tryCount
	 * @return
	 */
	public static String getDocumentText(String linkUrl, int tryCount) {
		String html = getDocumentHtml(linkUrl, tryCount);
		String text = html.replaceAll("<[^>]*?>", "");
		return text;
	}

	/**
	 * 获取指定页面url 符合指定regex的超链接的url以及本文
	 * 
	 * @param pageUrl
	 *            : 页面的url
	 * @param regex
	 *            : 超链接的regex
	 * @return Map<String,String> // 超链接url,超链接文本
	 */
	public static Map<String, String> selectA(String doc, String keywords) {
		Map<String, String> linksMap = new HashMap<String, String>();
		// <a.+?href=\"[^\"]+?"+keywords+".+?\".+?>(.+?)</a>
		String aTagRegex = "<a.+?>(.+?)</a>";
		Pattern pattern = Pattern.compile(aTagRegex);
		Matcher matcher = pattern.matcher(doc);
		//
		while (matcher.find()) {
			String aTagContent = matcher.group(0);
			if (aTagContent.matches(keywords)) {
				String href = aTagContent.replaceFirst(".+href=\"|\'", "")
						.replaceFirst("\".+", "");
				String text = aTagContent.replaceFirst("<.+?>", "")
						.replaceFirst("</a>", "");
				linksMap.put(href, text);
			}
		}
		return linksMap;
	}

}
