package com.coffee.qrcode.j2se;  
  
import java.io.File;
import java.util.Hashtable;

import com.coffee.qrcode.BinaryBitmap;
import com.coffee.qrcode.DecodeHintType;
import com.coffee.qrcode.LuminanceSource;
import com.coffee.qrcode.MultiFormatReader;
import com.coffee.qrcode.Reader;
import com.coffee.qrcode.Result;
import com.coffee.qrcode.common.HybridBinarizer;

  
/** 
 *  
 */  
public class DecodeUtils {  
	// by yinan
	private static final int BLACK = 0xff000000;
    private static final int YELLOW = 0xffffff00;
    private static final int RED = 0xffff0000;
    private static final int WHITE = 0xFFFFFFFF;  
    private static final int [] COLOR = {0xff,0x00};
  
    /** 
     * 璇诲彇涓�淮鍜屼簩缁寸爜 
     *  
     * @param path 
     *            鏂囦欢鍦板潃锛屽叏璺緞 
     * @return 
     */  
    public static String read(String path) {  
        return read(path, "UTF-8");  
    }  
  
    /** 
     * 璇诲彇涓�淮鍜屼簩缁寸爜 
     *  
     * @param path 
     *            鏂囦欢鍦板潃锛屽叏璺緞 
     * @param coding 
     *            鏂囧瓧缂栫爜 
     * @return 
     */  
    public static String read(String path, String coding) {  
        try {  
            Reader reader = new MultiFormatReader();  
            File file = new File(path);  
            BufferedImage image;  
            
            image = ImageIO.read(file);
            String resultStr = "";

            for (int i = 0; i < 3; i++) // by yinan
            {
            	BufferedImage image_channel = seperate(image, i);
            	File sfile = new File("test.png");
                ImageIO.write(image_channel, "png", sfile);  
                LuminanceSource source = new BufferedImageLuminanceSource(image_channel);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
                Result result;  
                Hashtable hints = new Hashtable();
                hints.put(DecodeHintType.CHARACTER_SET, coding);  
                // 瑙ｇ爜璁剧疆缂栫爜鏂瑰紡涓猴細utf-8  
                
                result = new MultiFormatReader().decode(bitmap, hints);
                resultStr = resultStr + result.getText();
            }
            return resultStr;  
  
        } catch (Exception ex) {  
            return null;  
        }  
    }  
  
    /** 
     * 鐢熸垚2缁寸爜鍥剧墖 
     *  
     * @param path 
     *            鏂囦欢鍦板潃锛屽叏璺緞 
     * @param content 
     *            鏂囧瓧鍐呭锛屼笉鑳戒负绌�
     */  
    public static void write2D(String path, String content) {  
        write2D(path, content, "png", "UTF-8", 200, 200);  
    }  
    
    public static BufferedImage seperate(BufferedImage image, int ch)
    {
    	// by yinan
    	BufferedImage out = new BufferedImage(image.getWidth(), image.getHeight(),  
                image.getType());  
    	int mask = 0;
    	
    	for (int x = 0; x < image.getWidth(); x++)
    	{
    		for (int y = 0; y < image.getHeight(); y++)
    		{
    			int color = image.getRGB(x, y);
	    		mask = 0xff << (8*(2-ch));
	    		color = color & mask;
	    		color = color >> (8*(2-ch));
				color = color + (color << 8) + (color << 16) + 0xff000000;
    			out.setRGB(x, y, color);
    		}
    	}
    	return out;
    }
    
   
  
}