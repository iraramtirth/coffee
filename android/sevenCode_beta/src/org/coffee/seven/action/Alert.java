package org.coffee.seven.action;


import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import coffee.seven.R;
import coffee.util.view.ViewUtils;

public class Alert {
	
	/**
	 * @param context
	 * @param title	： 标题
	 * @param content	： 提示内容
	 * @param clickListener : 与 clickListener 参数数目一致
	 * @param drawableResid : 按钮背景图  : 最多只接受三个参数 
	 *   {@link R.layout.alert_dialog};
	 */
	public static void show(Activity context, String title, String[] content, 
				View.OnClickListener[] clickListener, Integer[] drawableResid){
		View view = View.inflate(context, R.layout.alert_dialog, null);
		
		ViewUtils.setText(view, R.id.alert_title, title);
		if(content.length > 0){
			ViewUtils.setText(view, R.id.alert_content_1, content[0]);	
		}
		if(content.length > 1 && content[1] != null){
			TextView tv = ((TextView)view.findViewById(R.id.alert_content_2));
			tv.setVisibility(View.VISIBLE);
			tv.setText(Html.fromHtml(content[1]));
		}
		
		final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(view);
        dialog.show();
        //保存用不到的按钮
		for(int i=0; i<drawableResid.length; i++){
			if(drawableResid[i] != null){
				Button btn = null;
				switch (i) {
				case 0://left
					btn = (Button) view.findViewById(R.id.alert_btn_left);
					break;
				case 1://center 
					btn = (Button) view.findViewById(R.id.alert_btn_center);
					break;
				case 2://right
					btn = (Button) view.findViewById(R.id.alert_btn_right);
					break;
				}
				btn.setVisibility(View.VISIBLE);
				if(clickListener[i] == null){
					clickListener[i] = new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.cancel();
						}
					};
				}
				btn.setOnClickListener(clickListener[i]); //注册单击事件
				btn.setBackgroundResource(drawableResid[i]);
			}
		}
	}
	/**
	 * if(drawableResid.length == 1){
			Button btn = (Button) view.findViewById(R.id.alert_btn_left);
			btn.setVisibility(View.VISIBLE);
			if(onClickListener[0] == null){
				onClickListener[0] = new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						bulder.cancel();
					}
				};
			}
			btn.setOnClickListener(onClickListener[0]);
		}else{
			
		}
	 */
}
