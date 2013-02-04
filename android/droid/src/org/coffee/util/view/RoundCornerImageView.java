package org.coffee.util.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RoundCornerImageView extends ImageView {

	public RoundCornerImageView(Context context) {
		super(context);
		this.init(null);
	}

	public RoundCornerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(attrs);
	}

	public RoundCornerImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.init(attrs);
	}

	private void init(AttributeSet attrs) {
		paint.setAntiAlias(true);
		paint.setPathEffect(pathEffect);
		
		if(attrs != null)
		{
			
		}
	}

	final Paint paint = new Paint();

	final float roundPx = 8.0F;

	final CornerPathEffect pathEffect = new CornerPathEffect(roundPx);

    
	@Override
	protected void onDraw(Canvas canvas) {

		Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
	
		Path clipPath = new Path();
		clipPath.addRect(0, 0, this.getWidth(), this.getHeight(),
				Path.Direction.CCW);
		
		canvas.drawPath(clipPath, paint);//dst
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
		canvas.drawBitmap(bitmap, new Matrix(), paint);//src
		
//	     final Paint paint = new Paint();  
//	     final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
//	     final RectF rectF = new RectF(rect);  
//	  
//	     paint.setAntiAlias(true);  
//	     canvas.drawRoundRect(rectF, roundPx, roundPx, paint);  
//	  
//	     paint.setXfermode(new PorterDuffXfermode(Mode.SCREEN));  
//	     canvas.drawBitmap(bitmap, new Matrix(), paint);   	
	}
}