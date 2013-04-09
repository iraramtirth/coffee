package org.coffee.util.view;

import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * 手势事件 onDown method return true it will forword callback to onScroll
 * onScrollmethod return true it will forword callback to onFling
 * 
 * @author coffee 2013-1-24下午2:31:08
 */
public class OnGestureEvent implements View.OnTouchListener, OnGestureListener,
		OnDoubleTapListener {

	private GestureDetector gestureDetector;
//	private MotionEvent preEvent;
//	private MotionEvent nextEvent;

	public OnGestureEvent() {
		gestureDetector = new GestureDetector(this);
	}

	/**
	 * 该方法来自OnTouchListener //gestureDetector通过OnTouchListener间接响应
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
//		if (preEvent == null) {
//			preEvent = MotionEvent.obtain(event); // 第一次触发
//		} else {
//			preEvent = MotionEvent.obtain(nextEvent);
//		}
//		nextEvent = MotionEvent.obtain(event);
//		// 重置preEvent/nextEvent， 该操作很重要
//		if (event.getAction() == MotionEvent.ACTION_UP) {
////			preEvent = null;
////			nextEvent = null;
//		} else {
//			this.onScroll(preEvent, nextEvent);
//		}
		return this.gestureDetector.onTouchEvent(event);
	}

	/**
	 * 扩展与onScroll(e1, e2, distanceX, distanceY)不同的是 e1、e2都不断触发
	 * 
	 * @see {org.coffee.adapter.OnGestureEvent#onScroll()}
	 */
	public boolean onScroll(MotionEvent e1, MotionEvent e2) {
		return true;
	}

	////////一下方法是实现子自OnGuestureListener
	/**
	 * 与接触屏幕时触发 (该事件当触摸屏幕时候(不管是scrll还是...其他==)一定发生)
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return true;
	}

	/**
	 * 滑动后up时触发
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// if(e1.getX() - e2.getX() >50 && Math.abs(velocityX) > 20){
		// Toast.makeText(context, "向左", Toast.LENGTH_SHORT).show();
		//
		// }
		// if(e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 20){
		// Toast.makeText(context, "向右", Toast.LENGTH_SHORT).show();
		// }
		// if(e1.getY() - e2.getY() > 50 && Math.abs(velocityY) > 20){
		// Toast.makeText(context, "向上", Toast.LENGTH_SHORT).show();
		// }
		// if(e2.getY() - e1.getY() > 50 && Math.abs(velocityY) > 20){
		// Toast.makeText(context, "向下", Toast.LENGTH_SHORT).show();
		// }
		return true;
	}

	// 用户长按触摸屏，由多个MotionEvent ACTION_DOWN触发
	@Override
	public void onLongPress(MotionEvent e) {
		// Toast.makeText(context, "onLongPress", Toast.LENGTH_SHORT).show();
	}

	/**
	 * 当划屏的时候，会连续激发该事件 e1：只触发1次。e2随着划屏不断触发
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return true;
	}

	// 用户轻触触摸屏，尚未松开或拖动，由一个1个MotionEvent ACTION_DOWN触发
	// 注意和onDown()的区别，强调的是没有松开或者拖动的状态
	@Override
	public void onShowPress(MotionEvent e) {
		// Toast.makeText(context, "onShowPress", Toast.LENGTH_SHORT).show();
	}

	/**
	 * touch down后又没有滑动（onScroll）又没有长按（onLongPress），然后Touchup时触发 -
	 * 点击一下非常快的（不滑动）Touchup：onDown->onSingleTapUp->onSingleTapConfirmed -
	 * 点击一下稍微慢点的
	 * （不滑动）Touchup：onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed
	 */
	// 用户（轻触触摸屏后）松开，由一个1个MotionEvent ACTION_UP触发
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// Toast.makeText(context, "onSingleTapUp", Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		return false;
	}

}
