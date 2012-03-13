package coffee.code.activity;


import android.os.Bundle;
import coffee.code.activity.base.BaseActivity;
import coffee.code.activity.base.IActivity;
import coffee.code.R;

/**
 * 帮助页面
 * @author wangtao
 */
public class HelpActivity extends BaseActivity implements IActivity{
	
	@Override
	protected void onCreate(Bundle mBundle) {
		super.onCreate(loadLayout(R.layout.help, R.drawable.common_title_3));
	}
	
}
