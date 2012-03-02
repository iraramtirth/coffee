package coffee.util.view;

import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class TextUtils {
	/** 
     * 获取字体高度 
     * @param textSize : textSize==null时  获取字符串的默认字体,   
     */  
    public static int getFontHeight(Float textSize){  
        Paint paint = new Paint();  
        if(textSize != null){  
            paint.setTextSize(textSize);  
        }  
        FontMetrics fm = paint.getFontMetrics();  
        return (int)(fm.descent - fm.ascent);    
    }  
    
    /** 
     * 获取字符串宽度
     * @param textSize : textSize==null时  获取字符串的默认字体,
     * @return ： 单位像素   
     */  
    public static int getTextWidth(Float textSize, String str){  
        Paint paint = new Paint();  
        if(textSize != null){  
            paint.setTextSize(textSize);  
        }  
//      return (int)(paint.getFontSpacing());  
    	float strWidth = paint.measureText(str); 
    	return (int) strWidth;
    }  
    
}
