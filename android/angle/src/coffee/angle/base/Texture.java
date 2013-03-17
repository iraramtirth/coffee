package coffee.angle.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import coffee.angle.App;

/**
 * 
 * @author coffee<br>
 *         2013-3-16下午5:08:34
 */
public abstract class Texture {
	/**
	 * resource
	 */
	protected Bitmap bitmap;
	/**
	 * -1 is not visual <br>
	 * 0 and larger will show
	 */
	protected int level = -1;
	/**
	 * position x,y
	 */
	protected int px = 100;
	protected int py = 100;

	protected boolean runnable = false;

	public abstract void draw(Canvas canvas);

	/*****************************/
	protected Context getContext() {
		return App.getContext();
	}

	public int getLevel() {
		return this.level;
	}

	public boolean isVisual() {
		return level > -1;
	}

	public boolean isRnnnable() {
		return this.runnable;
	}
}
