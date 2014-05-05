package coffee.frame.book;

import org.coffee.util.framework.Measurer;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import coffee.utils.log.Log;

public class BookConfig {
	private int fontSize = 40;
	private int textColor = Color.BLACK;
	private int backColor = 0xffbb9e85; // 背景颜色
	private int marginWidth = 15; // 左右与边缘的距离
	private int marginHeight = 20; // 上下与边缘的距离

	private int lineCount; // 每页可以显示的行数
	private int lineFontNum;// 每行显示的字数
	private float visibleHeight; // 绘制内容的宽
	private float visibleWidth; // 绘制内容的宽

	private Paint mPaint;
	private int mWidth;
	private int mHeight;

	public BookConfig(Activity context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		this.mWidth = displayMetrics.widthPixels; // Pager 宽和高
		this.mHeight = displayMetrics.heightPixels;
	
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);
		mPaint.setTextSize(fontSize);
		mPaint.setColor(textColor);
		visibleWidth = mWidth - marginWidth * 2;
		visibleHeight = mHeight - marginHeight * 2 - new Measurer().getStatusBarHeight(context);
		lineCount = (int) (visibleHeight / fontSize); // 可显示的行数
		lineFontNum = (int) (visibleWidth / fontSize);
		Log.d("bookConfig", "fontSize: " + fontSize + " , marginWidth: " + marginWidth + ", marginHeight: " + marginHeight);
		Log.d("bookConfig", "lineCount: " + lineCount + " , lineFontNum: " + lineFontNum);
		Log.d("bookConfig", "visibleHeight: " + visibleHeight + " , visibleWidth: " + visibleWidth);
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public int getBackColor() {
		return backColor;
	}

	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}

	public int getMarginWidth() {
		return marginWidth;
	}

	public void setMarginWidth(int marginWidth) {
		this.marginWidth = marginWidth;
	}

	public int getMarginHeight() {
		return marginHeight;
	}

	public void setMarginHeight(int marginHeight) {
		this.marginHeight = marginHeight;
	}

	public int getLineCount() {
		return lineCount;
	}

	public void setLineCount(int lineCount) {
		this.lineCount = lineCount;
	}

	public int getLineFontNum() {
		return lineFontNum;
	}

	public void setLineFontNum(int lineFontNum) {
		this.lineFontNum = lineFontNum;
	}

	public float getVisibleHeight() {
		return visibleHeight;
	}

	public void setVisibleHeight(float visibleHeight) {
		this.visibleHeight = visibleHeight;
	}

	public float getVisibleWidth() {
		return visibleWidth;
	}

	public void setVisibleWidth(float visibleWidth) {
		this.visibleWidth = visibleWidth;
	}

	public Paint getmPaint() {
		return mPaint;
	}

	public void setmPaint(Paint mPaint) {
		this.mPaint = mPaint;
	}

	public int getmWidth() {
		return mWidth;
	}

	public void setmWidth(int mWidth) {
		this.mWidth = mWidth;
	}

	public int getmHeight() {
		return mHeight;
	}

	public void setmHeight(int mHeight) {
		this.mHeight = mHeight;
	}
}
