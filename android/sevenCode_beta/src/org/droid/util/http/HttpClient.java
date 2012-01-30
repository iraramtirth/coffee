package org.droid.util.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * 通用的http工具类
 * 
 * @author wangtao
 */
public class HttpClient {
	private final String TAG = HttpClient.class.getSimpleName();
	protected static String encode = "UTF-8";

	protected static String PROXY_SERVER = "10.0.0.172";

	// 检查网络类型
	public static NetType netType;

	// 网络类型
	public enum NetType {
		WAP,	//移动
		NET,
		NONE 	// 不可用
	}

	/**
	 * 设置网络类型
	 *  * netWrokInfo.getExtraInfo()
	 * [X86]  NetworkInfo: type: ETH[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: true
	 * [WIFI] NetworkInfo: type: WIFI[], state: CONNECTED/CONNECTED, reason: (unspecified), extra: (none), roaming: false, failover: false, isAvailable: true
	 * [CMNET]NetworkInfo: type: MOBILE[EDGE], state: CONNECTED/CONNECTED, reason: dataEnabled, extra: cmnet, roaming: false, failover: false, isAvailable: true
	 * @param context
	 */
	public synchronized static void setNetworkType(Activity context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();
		if (netWrokInfo == null || !netWrokInfo.isAvailable()) {
			Toast.makeText(context, "当前的网络不可用，请开启\n网络", Toast.LENGTH_LONG)
					.show();
			netType = NetType.NONE;
		}else if(netWrokInfo.getExtraInfo() == null){
			//wifi
			netType = NetType.NET;
		}else if (netWrokInfo.getExtraInfo().toLowerCase().contains("wap")) {
			netType = NetType.WAP;
		} else if(netWrokInfo.getExtraInfo().toLowerCase().contains("net")){
			netType = NetType.NET;
		}
	}

	// 检查网络类型
	public synchronized static boolean checkNetworkStatus(final Activity context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		connManager.getActiveNetworkInfo();
		// 网络状态
		boolean netSataus = false;
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null) {
			netSataus = info.isAvailable();
		}
		return netSataus;
	}

	// User agent strings.
	protected static final String DESKTOP_USERAGENT = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_7; en-us)"
			+ " AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0"
			+ " Safari/530.17";
	protected static final String IPHONE_USERAGENT = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us)"
			+ " AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0"
			+ " Mobile/7A341 Safari/528.16";

	/**
	 * 读取图片
	 * 
	 * @param urlStr
	 *            ： 传入的数据是 http://..../xxx.jpg的形式
	 * @return
	 */
	public byte[] getImage(String urlStr) {
		if(netType == NetType.WAP){
			try{
				byte[] data = (byte[])get(urlStr, 1);
				return data;
			}catch(Exception e){
				return null;
			}
		}
		
		byte[] imageRaw = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection uc = createHttpURLConnection(url);
			uc.connect();
			InputStream in = new BufferedInputStream(uc.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int c;
			while ((c = in.read()) != -1) {
				out.write(c);
			}
			out.flush();
			imageRaw = out.toByteArray();
			uc.disconnect();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageRaw;
	}

	/**
	 * post方式提交数据
	 * 
	 * @param baseUrl
	 *            ： url
	 * @param paramsMap
	 *            : 参数集
	 * @param enc
	 *            : 编码方式
	 */
	public String post(String baseUrl, Map<String, String> paramsMap) {
		String doc = "";
	    try{
	    	DefaultHttpClient httpclient = new DefaultHttpClient();
	    	if(netType == NetType.WAP){
	    		HttpHost proxy = new HttpHost(PROXY_SERVER);
	    		httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	    	}
	        HttpPost post = new HttpPost(baseUrl); 
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            for (String paramName : paramsMap.keySet()) {
				String paramValue = paramsMap.get(paramName);
				if (paramValue == null) {
					continue;
				}
				params.add(new BasicNameValuePair(paramName, paramValue));
            }
            UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params,HTTP.UTF_8);
            post.setEntity(ent);
            HttpResponse responsePOST = httpclient.execute( post);  
            HttpEntity resEntity = responsePOST.getEntity();  
            if (resEntity != null) {    
            	doc = EntityUtils.toString(resEntity);
                Log.i(TAG,doc);
            }
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return doc;
	}
	
	
	//创建Http连接
	public HttpURLConnection createHttpURLConnection(URL url) {
		HttpURLConnection uc = null;
		try{
			if (netType == NetType.WAP ) {// cmwap 上网模式， 使用代理
	 			java.net.Proxy proxy = new Proxy(Proxy.Type.HTTP,
						new InetSocketAddress(PROXY_SERVER, 80));
				uc = (HttpURLConnection) url.openConnection(proxy);
			}else{
				uc = (HttpURLConnection) url.openConnection();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return uc;
	}
	
	/**
	 * @param linkUrl
	 * @throws IOException
	 * java.net.UnknownHostException: Host is unresolved: not-a-legal-address:80
	 * @param type : type==0返回字符串   type==1 返回byte[]数组
	 */
	public Object get(String linkUrl, Integer... returnType){
		try{
			if(linkUrl == null || linkUrl.trim().equals("")){
				return "";
			}
			
		    DefaultHttpClient httpclient = new DefaultHttpClient();
		    if(netType == NetType.WAP){
		    	//设置代理
		    	HttpHost proxy = new HttpHost(PROXY_SERVER, 80);
		    	httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		    }
		    //设置http请求
		    HttpGet req = new HttpGet(linkUrl);
		    HttpResponse rsp = httpclient.execute(req);
		    
		    StatusLine line = rsp.getStatusLine();
		    if(line.getStatusCode() != HttpStatus.SC_OK){
		    	return "";
		    }
		    HttpEntity entity = rsp.getEntity();
		
		    if (entity != null) {
		    	if(returnType.length > 0 && returnType[0] == 1){
		    		return EntityUtils.toByteArray(entity);
		    	}else{
		    		String doc = EntityUtils.toString(entity);
		    		return doc;
		    	}
		    }
		    httpclient.getConnectionManager().shutdown();        
		} catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}
	
	 
}
