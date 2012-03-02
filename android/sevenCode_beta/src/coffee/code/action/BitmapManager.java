package coffee.code.action;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;

/**
 * 主要用于管理Bitma对象的创建以及释放内存 
 * @author wangtao
 */
public class BitmapManager {
	private static Map<String, Bitmap> bmpMap = new HashMap<String, Bitmap>(); 
	
	public static void put(String localUrl, Bitmap bmp){
		bmpMap.put(localUrl, bmp);
	}
	
	public static Bitmap get(String localUrl){
		return bmpMap.get(localUrl);
	}
	
	public static boolean containsKey(String localUrl){
		return bmpMap.containsKey(localUrl);
	}
	
	//释放内存 
	public static void recycleAll(){
		try{
			for (String localUrl : bmpMap.keySet()) {
				Bitmap bmp = bmpMap.get(localUrl);
				if(bmp != null && !bmp.isRecycled()){
					bmp.recycle();
					System.gc();
					bmp = null;
				}
			}
			bmpMap.clear();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
