package org.coffee.view;

import org.coffee.util.adapter.OnGestureEvent;
import org.coffee.util.res.RES;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.coffee.R;

public class ImageZoomer extends FrameLayout implements OnClickListener{
	
	private TextView mZoomIn;	//放大 
	private TextView mZoomOut;	//缩小
	
	private Bitmap mBitmap;	
	private Matrix mMatrix = new Matrix();
	
	
	private float scale = 1.0F;	//缩放比例
	private float maxScale = 2;	//放大的最小比例
	
	//默认图像位于组件的中央【x\y方向上垂直居中】
	private int[] moveLength = new int[2];//移动的长度
	
	private int left;	//图像当前位置的的left值
	private int top;	//图像当前位置的的top值
	
	public ImageZoomer(Context context){
		this(context,null);
	}
	public ImageZoomer(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
		this.setWillNotDraw(false);
	}
	
	private void init(){
		mBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.earth);
		
		mZoomOut = new TextView(this.getContext());
		mZoomOut.setBackgroundResource(R.drawable.zoomout);
		
		mZoomIn = new TextView(this.getContext());
		mZoomIn.setBackgroundResource(R.drawable.zoomin);
		
		LinearLayout lLayout = new LinearLayout(this.getContext());
		lLayout.setOrientation(LinearLayout.HORIZONTAL);
		lLayout.addView(mZoomOut);
		lLayout.addView(mZoomIn);
		
		mZoomIn.setOnClickListener(this);
		mZoomOut.setOnClickListener(this);
		
//		RelativeLayout rLayout = new RelativeLayout(this.getContext());
//		rLayout.setLayoutParams(new RelativeLayout.LayoutParams(200,-2));
//		//zoomOut
//		mZoomOut = new TextView(this.getContext());
//		mZoomOut.setText("ZoomOut");
//		mZoomOut.setBackgroundColor(this.getContext().getResources().getColor(R.color.red));
//		RelativeLayout.LayoutParams outRlParams = new RelativeLayout.LayoutParams(-2,-2);
//		outRlParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		outRlParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//		rLayout.addView(mZoomOut, outRlParams);
//		//zoomIn
//		mZoomIn = new TextView(this.getContext());
//		mZoomIn.setText("ZoomIn");
//		mZoomIn.setBackgroundColor(this.getContext().getResources().getColor(R.color.red));
//		RelativeLayout.LayoutParams inRlParams = new RelativeLayout.LayoutParams(-2,-2);
//		inRlParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		inRlParams.addRule(RelativeLayout.LEFT_OF , mZoomOut.getId());
//		mZoomIn.setLayoutParams(inRlParams);
//		rLayout.addView(mZoomIn);
		
		//set layoutParam
		FrameLayout.LayoutParams inParams = new	FrameLayout.LayoutParams(RES.WRAP_CONTENT, RES.WRAP_CONTENT);
		inParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
		this.addView(lLayout, inParams);
		
		//
		this.setOnTouchListener(onGestureEvent);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mBitmap != null){
			Paint paint = new Paint();
			int dstWidth = (int) (mBitmap.getWidth() * scale) ;
			int dstHeight = (int) (mBitmap.getHeight() * scale);
			Bitmap matrixBitmap = Bitmap.createScaledBitmap(mBitmap, dstWidth, dstHeight, false);
			if(left == 0){
				left = (this.getWidth() - dstWidth )/2;
			}else{
				left += moveLength[0];
			}
			if(top == 0){
				top = (this.getHeight() - dstHeight )/2;
			}else{
				top += moveLength[1];
			} 
			canvas.drawBitmap(matrixBitmap, left, top, paint);
		}
	}

	public void setBitmap(Bitmap mBitmap){
		this.mBitmap = mBitmap;
		invalidate();
	}
	@Override
	public void onClick(View v) {
		if(v == mZoomIn){
			scale += 0.1;
			if(scale > maxScale){
				return;
			}
			mMatrix.setScale(scale, scale);
		}
		if(v == mZoomOut){
			scale -= 0.1;
			mMatrix.setScale(scale, scale);
		}
		invalidate();
	}
	
	
	OnGestureEvent onGestureEvent = new OnGestureEvent(){

		public boolean onScroll(MotionEvent e1, MotionEvent e2){
			moveLength[0] = (int) (e2.getX() - e1.getX());
			moveLength[1] = (int) (e2.getY() - e1.getY());
			//不同时为0时候，即使非静止状态
			if(moveLength[0] != 0 || moveLength[1] != 0){
				invalidate();
			}
			return true;
		}
	};
	
}
