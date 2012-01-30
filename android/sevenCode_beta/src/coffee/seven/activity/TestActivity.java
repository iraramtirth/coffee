package coffee.seven.activity;

import org.droid.util.json.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import coffee.seven.bean.UserBean;

public class TestActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String jsString = "{username:coffee,password:123}";
		try {
			JSONObject json = new JSONObject(jsString);
			String name = json.getString("username");
			TextView tv = new TextView(this);
			tv.setText(name);
			UserBean user = JsonUtils.toBean(json, UserBean.class);
			System.out.println(user);
			
			this.setContentView(tv);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
