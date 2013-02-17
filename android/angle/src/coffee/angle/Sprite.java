package coffee.angle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Sprite {
	private Bitmap bmp;
	private boolean visual = false;
	private int x;
	private int y;

	public Sprite(int drawableResId) {
		bmp = BitmapFactory.decodeResource(App.getContext().getResources(),
				drawableResId);
	}

	public void draw(Canvas canvas) {

	}

	// *************************************
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isVisual() {
		return visual;
	}

	public void setVisual(boolean visual) {
		this.visual = visual;
	}

	public int getHeight() {
		return bmp.getHeight();
	}

	public int getWidth() {
		return bmp.getWidth();
	}

	public Bitmap getBitmap() {
		return bmp;
	}
}
