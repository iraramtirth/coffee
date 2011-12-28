package com.android.game.api2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * 游戏精灵
 * 
 * @author wangtao
 */
public class Sprite {
	
	private Bitmap sprite;
	private int x;			//水平方向坐标
	private int y;			//垂直方向坐标
	
	public static final int STEP = 5;
	
	public enum ActionType{
		NONE,
		UP,
		DOWN,
		LEFT,
		RIGHT
	}
	
	public Sprite(Bitmap bitmap){
		this.sprite = bitmap;
	}
	
	/**
	 * 定位
	 * @param x	：x方向坐标
	 * @param y	：y方向坐标
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	
	/**
	 * sprite移动|旋转
	 * @param: step: 最多接收一个参数
	 */
	public void paint(Canvas canvas, ActionType action, int... step){
		if(step.length == 0){
			step = new int[]{STEP};
		}
		for(int i=0; i<step[0]; i++){
			Paint paint = new Paint();
			Rect rect = this.getRectCur();// 当前所在的位置 
			canvas.drawRect(rect, paint);// 清空
			int[] xy = this.handleXY(action, 1);//每次移动一格
			this.x = xy[0];
			this.y = xy[1];
			canvas.drawBitmap(this.sprite, x, y, paint);
		}
	}
	/**
	 * 获取当前所在的位置,以及移动后的位置 [重叠+不重叠]
	 */
	public Rect getRect(ActionType action, int... step){
		int[] xy = this.handleXY(action, step);
		int minX = Math.min(xy[0], x);	 //x位移的最小值
		int minY = Math.min(xy[1], y);	 //y位移的最小值
		int absX = Math.abs(xy[0] - x);  //x位移的差额的绝对值
		int absY = Math.abs(xy[1] - y);	 //y位移的差值的绝对值
		//Log.e("xxx","锁定："+minX+" " + minY);
		Rect rect = new Rect(minX ,minY ,minX + this.sprite.getWidth() + absX,
					minY + this.sprite.getHeight() + absY);
		return rect;
	}
	
	/**
	 *  sprite[当前]位置下的矩形区域
	 */
	private Rect getRectCur(){
		return new Rect(x, y, x + sprite.getWidth(), y + sprite.getHeight());
	}
	/**
	 * 处理执行操作后的xy
	 * @param action
	 * @param step
	 */
	private int[] handleXY(ActionType action, int... step){
		int stepSize = 5;
		if(step.length > 0){
			stepSize = step[0];
		}
		int tmpX = x;
		int tmpY = y;
		switch(action){
		case NONE:
			break;
		case UP:
			tmpX = tmpX + stepSize;
			if(tmpX < 0){
				tmpX = 0;
			}
			break;
		}
		return new int[]{tmpX,tmpY};
	}
}
