package org.coffee.seven.activity;


import android.os.Bundle;
import coffee.seven.R;
import coffee.seven.activity.base.BaseActivity;
import coffee.seven.activity.base.IActivity;

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
