package coffee.frame.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import coffee.frame.activity.base.BaseActivity;

/**
 * 
 * @author coffee <br>
 *         2014年4月23日下午3:44:43
 */
public class TouchViewTestActivity extends BaseActivity {
	private float touchX;
	private float touchY;
	private float preX;
	private float preY;
	private float mStartX;
	private float mStartY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		preX = touchX;
		preY = touchY;
		touchX = event.getRawX();
		touchY = event.getRawY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mStartX = touchX;
			mStartY = touchY;
			break;

		case MotionEvent.ACTION_MOVE:
			System.out.println("X " + (touchX - preX));
			System.out.println("Y " + (touchY - preY));
			
			break;

		case MotionEvent.ACTION_UP: // 捕获手指触摸离开动作
			System.out.println(touchX - mStartX);
			System.out.println(touchY - mStartY);
			break;
		}

		return true;
	}
}
