package coffee.im.bluetooth.ui.activity;

import org.bluetooth.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import coffee.im.bluetooth.ui.activity.base.BaseActivity;

/**
 * Main
 * 
 * @author wangtaoyfx 2013-1-11上午10:30:31
 */
public class TestActivity extends BaseActivity implements OnClickListener {

	private String username;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityToMgr = false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// NetworkModule module = new NetworkModule(new CoreModule());
		// //启动module线程
		// module.start();
		// //启动HttpConnectionThread线程--
		// module.setConnectionThreads(null, new ConnectionAgent[]{new
		// HttpProxy()});
		// HandlerRequest(module,"http://www.baidu.com/");
		// HandlerRequest(module,"http://g.cn/");
		// LogicMgr.getLoginLogic()
		username = "241313766";
		password = "iipopp";
		// LogicMgr.getLoginLogic().login(userName, passWord);
		// loginLogic.login("241313766", "iipopp");

		// Request request = new Request(Constants.REQ_GET_NAV_CONFIG, this,
		// true);
		// request.addParameter(Constants.PARA_CLIENT_TYPE,
		// SysConstants.CLIENT_TYPE);
		// request.addParameter(Constants.PARA_PLATFORM, Utility.getPlatfrom());
		// request.addParameter(Constants.PARA_CLIENT_VERSION,
		// Utility.getVersionName());
		// request.addParameter(Constants.PARA_LOGIN_ACCESS,
		// Constants.LOGIN_NET_ACCESS_NOT_CMWAP);// 登录方式CMNET、WIFI或者CMWAP
		//
		// request.addParameter(Constants.PARA_USER_NAME, username);// 登录用户名
		// request.addParameter(Constants.PARA_USER_PWD, password);// 用户密码
		// request.addParameter(Constants.PARA_ALGORITHM, new
		// AlgorithmProxy());// 加密算法
		//
		// request.addParameter(Constants.PARA_RESUME_NETWORK, "resume");
		// // sendRequest(request);
		// FetionApp.getFetionAgent().handleRequest(request);

		// 发送消息
		View btn1 = this.findViewById(R.id.button1);
		btn1.setOnClickListener(this);

		// 登录
		View btn2 = this.findViewById(R.id.button2);
		btn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 测试按钮
		case R.id.button1:
			LoginLogic.getInstance().logout();
			break;
		case R.id.button2:
			LogicMgr.getLoginLogic().login(username, password);
			// LoginLogic.getInstance().login(username, password);
			break;
		default:
			break;
		}

	}

//	private void HandlerRequest(NetworkModule module, String url) {
//		Request req = new Request(Constants.REQ_SEND_HTTP,
//				new RequestListener() {
//
//					@Override
//					public void onRequestFinshed(Request req) {
//						Object obj = req.getResponse();
//						String content = new String((byte[]) obj);
//						System.out.println(content);
//						Log.i("HTTP____", content);
//					}
//				}, true);
//		req.addParameter(Constants.PARA_URI, url);
//		req.addParameter(Constants.PARA_METHOD, Constants.HTTP_GET);
//
//		req.setLinkedRequest(req);
//
//		module.handleRequest(req);
//	}

	@Override
	public void doInitView() {

	}

}
