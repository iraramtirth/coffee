package coffee.code.activity.base;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import coffee.code.R;
import coffee.code.SysConfig;

/*
 * 买卖宝
 * Activity的父类
 */
public class BaseActivity extends Activity implements IActivity{
	private BaseActivity context;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		if(bundle == null){
			return;
		}
		context = this;
		int layoutRes= bundle.getInt(KEY_LAYOUT_RES);
		this.setContentView(layoutRes);
		//设置标题
		TextView titleView = (TextView) this.findViewById(R.id.title);
		int titleRes = bundle.getInt(KEY_TITLE_DRAWABLE);
		if(titleRes != 0){
			if(titleView != null){
				titleView.setBackgroundResource(titleRes);
			}
		}else{
			String titleText = bundle.getString(KEY_TITLE_TEXT);
			if(titleView != null){
				titleView.setText(titleText);
			}
		}
		//打电话
		ImageButton btn = (ImageButton) this.findViewById(R.id.phone_400);
		if(btn != null){
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent myIntentDial = new Intent(  
                            Intent.ACTION_CALL, Uri.parse("tel:" + SysConfig.phone400)  
                    );  
                    context.startActivity(myIntentDial);
				}
			});
		}
	}
	
	/**
	 * 该方法有子类调用
	 * @param layout： 布局文件 
	 * @param titleDrawable ： title 图片
	 */
	protected Bundle loadLayout(int layout, int titleDrawable) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, layout);
		bundle.putInt(KEY_TITLE_DRAWABLE, titleDrawable);
		return bundle;
	}
	/**
	 * @param layout 布局文件 
	 * @param titleText ：title 文本
	 */
	protected Bundle loadLayout(int layout, String titleText) {
		Bundle bundle = new Bundle();
		bundle.putInt(KEY_LAYOUT_RES, layout);
		bundle.putString(KEY_TITLE_TEXT, titleText);
		return bundle;
	}
	
	/**
	 * 像素转化为密度
	 * @param px
	 */
	protected int getPx(float dip){
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		float px = dip * metrics.density;
		return (int)px;
	}
	
}
