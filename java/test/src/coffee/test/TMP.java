package coffee.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class TMP {
	public static void main(String[] args) throws Exception {
		//cdms网络定位失败
		String holder = "{'address_language':'zh_CN','wifi_towers':[],'host':'maps.google.com',"
				+ "'cell_towers':[{'mobile_network_code':13824,'location_area_code':1,'cell_id':3139,"
				+ "'age':0,'mobile_country_code':'460'}],"
				+ "'radio_type':'cdma','request_address':true,'version':'1.1.0'}";
		
		//一下是gsm网络正确定位的发送数据
//		holder = "{'address_language':'zh_CN','wifi_towers':[],'host':'maps.google.com'," +
//				"'cell_towers':[{'mobile_network_code':1,'location_area_code':40986,'cell_id':2808634," +
//				"'age':0,'signal_strength':-111,'mobile_country_code':460}]," +
//				"'radio_type':'gsm','request_address':true,'version':'1.1.0'}";
		
//		holder = "{'address_language':'zh_CN','wifi_towers':[],'host':'maps.google.com'," +
//				"'cell_towers':[{'mobile_network_code':1,'location_area_code':13824,'cell_id':5731," +
//				"'age':0,'mobile_country_code':'460'}]," +
//				"'radio_type':'cdma','request_address':true,'version':'1.1.0'}";;
		
		HttpClient client = new DefaultHttpClient();
		HttpPost localHttpPost = new HttpPost("http://www.google.com/loc/json");
		String strJson = holder.toString();
		StringEntity objJsonEntity = new StringEntity(strJson);
		System.out.println("getLocationInfo: Location Send*****" + strJson);
		localHttpPost.setEntity(objJsonEntity);
		HttpResponse objResponse = client.execute(localHttpPost);
		HttpEntity httpEntity = objResponse.getEntity();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				httpEntity.getContent()));
		StringBuffer sb = new StringBuffer();
		String result = null;
		while ((result = br.readLine()) != null) {
			sb.append(result);
		}
		br.close();
		System.out.println("---------------");
		System.out.println(sb.toString());
	}
}
