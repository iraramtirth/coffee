package org.coffee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

public class CoffeeGallery extends AdapterView<Adapter>
{

    private Adapter mAdapter;

    public int mTouchStartX = 0;

    /**
     * 第一个item距离父组件左侧的具体。一般为负值 mGalleryLeft = mGaleryLeftStart +
     * scrolledDistance
     */
    private int mGalleryLeft = 0;
    /**
     * 可以看做特殊情况的 mGalleryLeft 即当ACTION_DOWN触发时候的mGalleryLeft mGaleryLeftStart =
     * getChildAt(0).getLeft(
     */
    private int mGaleryLeftStart = 0;

    public CoffeeGallery(Context context)
    {
        super(context);
    }

    public CoffeeGallery(Context context, AttributeSet attrs)
    {
        super(context,
            attrs);
    }

    public CoffeeGallery(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,
            attrs,
            defStyle);
    }

    @Override
    public Adapter getAdapter()
    {
        return this.mAdapter;
    }

    @Override
    public void setAdapter(Adapter adapter)
    {
        this.mAdapter = adapter;
        super.removeAllViewsInLayout(); // 删除所有组件
        super.requestLayout(); // 重新绘制

        if (mAdapter.getCount() == 0)
        {
            invalidate();
        }
    }

    @Override
    public View getSelectedView()
    {
        return null;
    }

    @Override
    public void setSelection(int position)
    {
        View child = mAdapter.getView(position,
            null,
            this);

        child.measure(MeasureSpec.UNSPECIFIED,
                MeasureSpec.EXACTLY);
        mGalleryLeft =  -1 * position * child.getMeasuredWidth();
        int maxLeft = -(mAdapter.getCount() * child.getMeasuredWidth() - getWidth());
        if (mGalleryLeft < maxLeft)
        {
            mGalleryLeft = maxLeft;
        }
        if (mGalleryLeft > 0)
        {
            mGalleryLeft = 0;
        }
        requestLayout();
    }

    /**
     * geiWidth() : view在设定好布局后整个view的高度。 getMeasureWidth()
     * 对view上的内容进行测量后得到的Vew内容占据的宽度
     * 前提是你必须在父布局的onLayout()方法或者此View的onDraw()方法里调用measure(0,0)(measure参数可以自己定义)
     * 否则你得到的结果和getWidth()得到的结果一样
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
        int bottom)
    {
        super.onLayout(changed,
            left,
            top,
            right,
            bottom);
        if (mAdapter == null)
        {
            return;
        }
        if (super.getChildCount() == 0)
        {
            int position = 0;
            int rightEdge = 0;
            while (rightEdge < getWidth() && position < mAdapter.getCount())
            {
                View child = addAndMeasureChild(position);
                rightEdge += child.getMeasuredWidth();
                position++;
            }
        }
        else
        {
            final int offset = mGalleryLeft - getChildAt(0).getLeft();
            int rightEdge = getChildAt(getChildCount() - 1).getRight();
            while (rightEdge + offset < getWidth()
                && getChildCount() < mAdapter.getCount())
            {
                View newRightchild = addAndMeasureChild(getChildCount());
                rightEdge += newRightchild.getMeasuredWidth();
            }
        }
        this.positionItems();
    }

    /**
     * Positions the children at the "correct" positions
     */
    private void positionItems()
    {
        int left = mGalleryLeft;
        for (int index = 0; index < getChildCount(); index++)
        {
            View child = getChildAt(index);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            int top = (super.getHeight() - height) / 2;
            child.layout(left,
                top,
                left + width,
                top + height);
            left += width;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (getChildCount() == 0)
        {
            return false;
        }
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = (int) event.getX();
                mGaleryLeftStart = getChildAt(0).getLeft();
                break;
            case MotionEvent.ACTION_MOVE:
                int scrolledDistance = (int) event.getX() - mTouchStartX;
                mGalleryLeft = mGaleryLeftStart + scrolledDistance;
               
                int maxLeft = -(mAdapter.getCount() * getChildAt(0).getWidth() - getWidth());
                if (mGalleryLeft < maxLeft)
                {
                    mGalleryLeft = maxLeft;
                }
                
                if (mGalleryLeft > 0)
                {
                    mGalleryLeft = 0;
                }
                
                requestLayout();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * Adds a view as a child view and takes care of measuring it
     * 
     * @param child The view to add
     * @param layoutMode Either LAYOUT_MODE_ABOVE or LAYOUT_MODE_BELOW
     */
    private View addAndMeasureChild(final int position)
    {

        View child = mAdapter.getView(position,
            null,
            this);
        LayoutParams params = child.getLayoutParams();
        if (params == null)
        {
            params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        }

        addViewInLayout(child,
            -1,
            params,
            true);

        final int itemHeight = getHeight();
        child.measure(MeasureSpec.UNSPECIFIED,
            MeasureSpec.EXACTLY | itemHeight);
        return child;
    }

}
