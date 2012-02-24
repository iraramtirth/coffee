package org.coffee.util;

import org.coffee.util.framework.InputMethodUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 输入法工具类
 *  
 * @author wangtao
 */
public class InputMethodUtilsDemo extends Activity implements OnClickListener{
	private Button btnShow;
	private Button btnHide;
	private EditText editText;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		LinearLayout lnLayout = new LinearLayout(this);
		lnLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		lnLayout.setOrientation(LinearLayout.VERTICAL);
		
		btnShow = new Button(this);
		btnHide = new Button(this);
		btnShow.setText("调用输入法...");
		btnShow.setOnClickListener(this);
		btnHide.setText("隐藏输入法");
		btnHide.setOnClickListener(this);
		editText = new EditText(this);
		lnLayout.addView(btnShow);
		lnLayout.addView(btnHide);
		lnLayout.addView(editText);
		this.setContentView(lnLayout);
//		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);  
	}

	@Override
	public void onClick(View v) {
		if(v == btnShow){
			//InputMethodUtils.toggle(this);
			//InputMethodUtils.isShow(this,editText);
			InputMethodUtils.show(this, editText);
		}
		if(v == btnHide){
			 InputMethodUtils.hide(this);
		}
	}
	 
	
}
