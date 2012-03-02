package coffee.code;

import java.net.URLEncoder;

import coffee.code.R;
import coffee.util.sys.SystemUtils;

import android.app.Application;
import android.os.Build;

/**
 * @author wangtao
 */
public class App extends Application {
	
	public static App context;
	public String SIM;	//sim卡 即：用户手机号
	public String IMEI;	//设备号
	public String FROM;	//渠道
	public String UA;	//user-agent手机型号
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		SIM = SystemUtils.getPhoneNumber();
		IMEI = SystemUtils.getDeviceId();
		FROM = SysConfig.FROM;
		try{
			UA = URLEncoder.encode(Build.BRAND,"UTF-8") 
				+ "-" + 
				URLEncoder.encode(Build.MODEL, "UTF-8");
		}catch(Exception e){
			//e.printStackTrace();
		}
	}
	/**
	 * 参数
	 */
	public String getQueryArgs(){
		return "from=" + FROM + "&imei=" + IMEI + "&sim=" + SIM + "&ua=" + UA; 
	}
}
