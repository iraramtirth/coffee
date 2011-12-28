package coffee.browser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;
/**
 * 通用的http工具类 
 * @author wangtao
 */
public class HttpClient {
	protected static String encode = "UTF-8";
	protected static StringBuilder doc = new StringBuilder();
	
	// User agent strings.
	protected static final String DESKTOP_USERAGENT =
            "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_7; en-us)"
            + " AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0"
            + " Safari/530.17";
	protected static final String IPHONE_USERAGENT = 
            "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us)"
            + " AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0"
            + " Mobile/7A341 Safari/528.16";
    
	/**
	 * 其中部分特殊字符已经处理
	 * 
	 * @param linkUrl
	 *            : 监控页面的URL
	 * @throws IOException
	 */
	public static String get(String linkUrl) {
		doc.delete(0, doc.length());
		try {
			URL url = new URL(linkUrl);
			HttpURLConnection uc = (HttpURLConnection) url.openConnection();
			uc.setRequestProperty("user-agent", IPHONE_USERAGENT);
			uc.setConnectTimeout(1000 * 10);
			BufferedReader in = new BufferedReader(new InputStreamReader(uc
					.getInputStream(), encode));
			String line = null;
			while ((line = in.readLine()) != null) {
				doc.append(line);
			}
			in.close();
			uc.disconnect();
		}catch(UnknownHostException e){
		}catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return doc.toString();
	}

	/**
	 * 读取图片
	 * @param urlStr ： 传入的数据是 http://..../xxx.jpg的形式
	 * @return
	 */
	public static byte[] getImage(String urlStr){
		byte[] imageRaw = null;
		  try {
		     URL url = new URL(urlStr);
		     HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		     InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		     ByteArrayOutputStream out = new ByteArrayOutputStream();
		     int c;
		     while ((c = in.read()) != -1) {
		         out.write(c);
		     }
		     out.flush();
		     imageRaw = out.toByteArray();
		     urlConnection.disconnect();
		     in.close();
		     out.close();
		  } catch (IOException e) {
		     e.printStackTrace();
		  }
		  return imageRaw;
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
				doc.append(line);
			}
			in.close();
			uc.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return doc.toString();
	}
}
