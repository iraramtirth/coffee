package org.android.util;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

/**
 * android.graphics包相关工具类
 * @author 王涛
 */
public class GraphicsUtils {

	/**
	 * 获取字体高度
	 * @param textSize : textSize==null时  获取字符串的默认字体,  
	 */
	public static int getFontHeight(Integer textSize){
		Paint paint = new Paint();
		if(textSize != null){
			paint.setTextSize(textSize);
		}
		FontMetrics fm = paint.getFontMetrics();
		return (int)(fm.descent - fm.ascent);  
	}
}
