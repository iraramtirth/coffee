package org.coffee.util.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
public class CustomView extends View{
    
    private Paint paint;
    private ArrayList<PointF> graphics = new ArrayList<PointF>();
    private Paint  lPaint;
    private Path   mPath;
    private  int of = 0;
    private Boolean over =false;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    public CustomView(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(65);
        
        lPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lPaint.setColor(Color.BLACK);
        lPaint.setStyle(Paint.Style.STROKE);//空心
        lPaint.setStrokeJoin(Paint.Join.ROUND);
        lPaint.setStrokeCap(Paint.Cap.ROUND);
        lPaint.setStrokeWidth(5);
        
        mPath = new Path();
        
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            over = false;
            graphics.clear();
            of=0;
            graphics.add(new PointF(x, y));
             touch_start(x, y);
             invalidate();
            break;
        case MotionEvent.ACTION_MOVE:
            graphics.add(new PointF(x, y));
            touch_move(x, y);
            invalidate();
            break;
        case MotionEvent.ACTION_UP:
            over = true;
            touch_up();
            invalidate();
            break;
    }

        return true;
    }
    
    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    //  canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);
        canvas.drawColor(Color.WHITE);//清理
        canvas.drawPath(mPath, lPaint);
        if(over&&graphics.size()>0){
            canvas.drawPoint(graphics.get(of).x, graphics.get(of).y, paint);
            of+=1;
            if(of<graphics.size()){
                if(of==graphics.size()-1){
                    mPath.reset();//移动完成后移除线条
                }
                invalidate();
            }
        }
    }
}