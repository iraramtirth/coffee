package coffee.angle.base;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import coffee.angle.App;

public class Sprite extends Texture {
	/**
	 * velocity x,y
	 */
	protected int vx = 2;
	protected int vy = 1;

	public Sprite(int drawableResource) {
		bitmap = BitmapFactory.decodeResource(App.getContext().getResources(),
				drawableResource);
		runnable = true;
		level = 1;
	}

	public Sprite(int drawableResource, int vx, int vy) {
		this(drawableResource);
		this.vx = vy;
		this.vy = vy;
	}

	public void draw(Canvas canvas) {
		if (!isVisual()) {
			return;
		}
		canvas.drawBitmap(bitmap, px, py, null);
		px += vx;
		py += vy;
	}
}
