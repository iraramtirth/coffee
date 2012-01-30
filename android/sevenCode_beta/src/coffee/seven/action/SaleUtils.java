package coffee.seven.action;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

public class SaleUtils {
	
//	private static int FONT_SIZE_15  = 15;
	private static int FONT_SIZE_20  = 20;
	/**
	 * 抢购价
	 */
	public static CharSequence getPrice(float price){
		String str1 = "抢购价: ";
		String str2 = " 元";
		String str = str1 + price + str2;
		int start = str.indexOf(price + "");
		int end = start + (price + "").length();
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		//设置整体颜色
		style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str.length(), 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置价格的字体颜色
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#ff9900")), start, end, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置价格的字体大小
		style.setSpan(new AbsoluteSizeSpan(FONT_SIZE_20, true), start, end,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return style;
	}
	
	/**
	 * 原价
	 */
	public static CharSequence getOriPrice(float oriPrice){
		String str1 = "原价: ";
		String str2 = " 元";
		String str = str1 + oriPrice + str2;
		//int start = str.indexOf(oriPrice + "");
		//int end = start + (oriPrice + "").length();
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		//设置整体颜色
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#898989")), 0, str.length(), 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置价格的字体颜色
		//style.setSpan(new ForegroundColorSpan(Color.parseColor("#898989")), start, end, 
			//	Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置价格的字体大小
		//style.setSpan(new AbsoluteSizeSpan(FONT_SIZE_20, true), start, end,
		//		Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return style;
	}
	/**
	 * 
	 */
	public static CharSequence getRemainCount(String remainCount){
		String str1 = "剩余数量: ";
		String str2 = "";
		String str = str1 + remainCount + str2;
		int start = str.indexOf(remainCount + "");
		int end = start + (remainCount + "").length();
		SpannableStringBuilder style = new SpannableStringBuilder(str);
		//设置整体颜色
		style.setSpan(new ForegroundColorSpan(Color.BLACK), 0, str.length(), 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置价格的字体颜色
		style.setSpan(new ForegroundColorSpan(Color.RED), start, end, 
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//设置价格的字体大小
		style.setSpan(new AbsoluteSizeSpan(FONT_SIZE_20, true), start, end,
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		return style;
	}
	
	
	public static SaleService saleService = new SaleService();
	
}
