package com.android.game.api;

import android.graphics.Canvas;
import android.graphics.RectF;
/**
 * Layer
 * Sprite当前位置的矩形区域 
 * @author wangtao
 */
public abstract class Layer extends RectF {
	protected int x = 0;				 // Layer的横坐标
	protected int y = 0; 				 // Layer的纵坐标
	protected int width = 0;			 // Layer的宽度
	protected int height = 0;			 // Layer的高度
	protected boolean visible = true; 	 // Layer是否可见

	protected Layer(int width, int height) {
		setWidth(width);
		setHeight(height);
	}
	/**
	 * 绘制Layer，必须被重载
	 * @param c
	 */
	public abstract void paint(Canvas c);

	
	//
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * 相对于当前的位置移动Layer
	 * @param dx
	 *            横坐标变化量
	 * @param dy
	 *            纵坐标变化量
	 */
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}
	 
	public final int getX() {
		return x;
	}
	public final int getY() {
		return y;
	}
	public final int getWidth() {
		return width;
	}
	public final int getHeight() {
		return height;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	public final boolean isVisible() {
		return visible;
	}
	void setWidth(int width) {
		if (width < 0) {
			throw new IllegalArgumentException();
		}
		this.width = width;
	}
	void setHeight(int height) {
		if (height < 0) {
			throw new IllegalArgumentException();
		}
		this.height = height;
	}

}
