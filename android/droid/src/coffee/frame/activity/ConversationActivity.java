package coffee.frame.activity;

import org.coffee.R;

import android.os.Bundle;
import coffee.frame.activity.base.BaseActivity;

/**
 * 
 * 
 * @author coffee<br>
 *         2013上午11:57:05
 */
public class ConversationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activityToMgr = false;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void doInitView() {
		setContentView(R.layout.main_tab1);
	}

}
