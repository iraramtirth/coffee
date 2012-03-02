package org.coffee.seven.activity.base;

import java.util.HashMap;
import java.util.Map;

import coffee.seven.action.MmbSystem;
import coffee.seven.activity.MainTabActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ViewFlipper;

public abstract class FlipperActivity extends Activity implements IActivity {

	protected ViewFlipper flipper;
	
	/**
	 * 每次保存两组 k-v : 
	 * Activity_ID 	--	 Activity_View
	 * Activity_View.toString -- parentView
	 */
	protected Map<String, View> flipperMap = new HashMap<String, View>();
	
	private View rootView;//默认视图
	
	private Intent intentRoot;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		try {
			String className =  getIntent().getStringExtra(ACTIVITY_CLASS_NAME);
			Class<?> clazz = Class.forName(className);
			intentRoot = new Intent(this, clazz);
			//重新加载
			//if(OrderHomeActivity.class == clazz){ 
//					|| SaleNowFlipperActivity.class == clazz
//					|| SaleNextFlipperActivity.class == clazz){
				//intentRoot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			//}
			
			flipper = new ViewFlipper(this);
			flipper.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			//如果 不设置 FLAG_ACTIVITY_CLEAR_TOP 每次加载返回的都是同一个view
			//flipper 是两个不同的view 。 所以你第二次 flipper.addView 的时候会报
			//The specified child already has a parent [第一次创建的 flipper 跟第二次创建的 ]
			rootView = MainTabActivity.mActivityManager.startActivity(
					ROOT_ACTIVITY_ID, intentRoot).getDecorView();
			
			this.displayView(rootView);
			
			this.flipperMap.put(ROOT, rootView);
			this.setContentView(flipper);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param activityId
	 * @param intent
	 * @param parentActivityId : 
	 * 	如果  parentActivityId == null 则 该view是ROOTView prentView = null;
	 * 	   parentActivityId == ROOT 则 该view是ROOTView的child， prentView == rootView
	 * @param reload ： 是否需要重新加载该ID
	 */
	public void show(String activityId, Intent intent, String parentActivityId, boolean reload) {
		View nextView = flipperMap.get(activityId);
		//
		if(reload){
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}
		//重新加载
		if(nextView == null || reload){	
			nextView = MainTabActivity.mActivityManager.startActivity(activityId, intent).getDecorView();
		}
		flipperMap.put(activityId, nextView);
		View prentView;
		if(parentActivityId != null){
			prentView = flipperMap.get(parentActivityId);
		}else{
			prentView = rootView;
		}
		flipperMap.put(nextView.toString(), prentView);
		this.displayView(nextView);
	}
	/**
	 * 注意访问级别设置为 public
	 * @param flipperIndex
	 */
	public void show(String activityId){
		if(activityId == null || activityId == ROOT){
			//注意
			this.flipper.removeAllViews();
			this.displayView(rootView);
		}else{
			View view = flipperMap.get(activityId);
			this.displayView(view);
		}
	}
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event){
		switch(event.getKeyCode()){
			case KeyEvent.KEYCODE_BACK:
				if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
					View parentView = flipperMap.get(flipper.getCurrentView().toString());
					if(parentView != null){
						//返回到ROOT组件
						if(flipperMap.get(parentView.toString()) == null){
							//该方法将销毁Activity
							MainTabActivity.mActivityManager.destroyActivity(IActivity.LEVEL_2, true);
							MainTabActivity.mActivityManager.destroyActivity(IActivity.LEVEL_3, true);
							this.flipper.removeAllViews();
						}
						this.displayView(parentView);
					}else{//已经是父组件了
						MmbSystem.exit(this);
					}
					return false;
				}
			case KeyEvent.ACTION_UP:
				break;	
			case KeyEvent.KEYCODE_DEL:
				return super.dispatchKeyEvent(event);
		}
		return true;
	}
	
	protected void displayView(View view){
		try{
			flipper.addView(view);
		}catch(Exception e){
			String err = e.getMessage();
			if(err != null && err.contains("The specified child already has a parent")){
				//删除当前view以后所有的view
				flipper.removeView(view);
				flipper.addView(view);
			}
		}
		flipper.setDisplayedChild(flipper.getChildCount() - 1);
	}
}
