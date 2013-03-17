package coffee.angle.base;

import coffee.angle.App;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * 背景图
 * 
 * @author coffee<br>
 *         2013-3-16下午5:10:49
 */
public class Background extends Texture {
	protected int w = 0;
	protected int h = 0;

	public Background(int drawableResource) {
		bitmap = BitmapFactory.decodeResource(App.getContext().getResources(),
				drawableResource);
		level = 0;
		if (bitmap != null) {
			w = bitmap.getWidth();
			h = bitmap.getHeight();
		}
	}

	public void draw(Canvas canvas) {
		if (!isVisual()) {
			return;
		}
		int maxW = Math.max(w, bitmap.getWidth());
		// int maxH = Math.max(h, bitmap.getHeight());
		// x方向平铺
		for (int i = px; i <= px + maxW;) {
			canvas.drawBitmap(bitmap, px, py, null);
			px += bitmap.getWidth();
		}
	}
}
