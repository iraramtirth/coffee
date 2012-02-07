package com.zxing;  
  
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
  
/** 
 *  
 */  
public class ImgUtil {  
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
     * 鐢熸垚1缁寸爜鍥剧墖 
     *  
     * @param path 
     *            鏂囦欢鍦板潃锛屽叏璺緞 
     * @param content 
     *            鍐呭 
     */  
    public static void write1D(String path, String content) {  
        write1D(path, content, "png", 200, 100);  
    }  
  
    /** 
     * 鐢熸垚1缁寸爜鍥剧墖 
     *  
     * @param path 
     *            鏂囦欢鍦板潃锛屽叏璺緞 
     * @param content 
     *            鍐呭 
     * @param suffix 
     *            鍥剧墖鍚庣紑 
     * @param imgWidth 
     *            鍥剧墖瀹�
     * @param imgHeight 
     *            鍥剧墖楂�
     */  
    public static void write1D(String path, String content, String suffix,  
            int imgWidth, int imgHeight) {  
        int codeWidth = 3 + (7 * 6) + 5 + (7 * 6) + 3;  
        codeWidth = Math.max(codeWidth, imgWidth);  
        try {  
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
                    BarcodeFormat.CODE_128, codeWidth, imgHeight, null);  
            MatrixToImageWriter.writeToFile(bitMatrix, suffix, new File(path));  
        } catch (Exception e) {  
            e.printStackTrace();  
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
    /** 
     * 鐢熸垚2缁寸爜鍥剧墖 
     *  
     * @param path 
     *            鏂囦欢鍦板潃锛屽叏璺緞 
     * @param content 
     *            鏂囧瓧鍐呭锛屼笉鑳戒负绌�
     * @param suffix 
     *            鐢熸垚鍥剧墖鍚庣紑 
     * @param coding 
     *            缂栫爜 
     * @param imgWidth 
     *            鍥剧墖瀹�
     * @param imgHeight 
     *            鍥剧墖楂�
     */  
    public static void write2D(String path, String content, String suffix,  
            String coding, int imgWidth, int imgHeight) {  
        try {  
        	int length = content.length();
        	// by yinan
        	String content1 = new String();
        	content1 = content.substring(0, length/3);        
        	String content2 = new String();
        	content2 = content.substring(length/3,length/3*2);        
        	String content3 = new String();
        	content3 = content.substring(length/3*2, length);
        	
            BitMatrix byteMatrix1;  
            Hashtable<EncodeHintType, String> hints1 = new Hashtable<EncodeHintType, String>();  
            hints1.put(EncodeHintType.CHARACTER_SET, coding); // 鏂囧瓧缂栫爜銆� 
            byteMatrix1 = new MultiFormatWriter().encode(content1,  
                    BarcodeFormat.QR_CODE, imgWidth, imgHeight, hints1);
            
            BitMatrix byteMatrix2;  
            Hashtable<EncodeHintType, String> hints2 = new Hashtable<EncodeHintType, String>();  
            hints2.put(EncodeHintType.CHARACTER_SET, coding); // 鏂囧瓧缂栫爜銆� 
            byteMatrix2 = new MultiFormatWriter().encode(content2,  
                    BarcodeFormat.QR_CODE, imgWidth, imgHeight, hints2);
            
            BitMatrix byteMatrix3;  
            Hashtable<EncodeHintType, String> hints3 = new Hashtable<EncodeHintType, String>();  
            hints3.put(EncodeHintType.CHARACTER_SET, coding); // 鏂囧瓧缂栫爜銆� 
            byteMatrix3 = new MultiFormatWriter().encode(content3,  
                    BarcodeFormat.QR_CODE, imgWidth, imgHeight, hints3);
            
            BufferedImage image = toBufferedImage(byteMatrix1, byteMatrix2, byteMatrix3);
            
            
            File file = new File(path);
            ImageIO.write(image, suffix, file);  
  
            // QRCodeWriter writer = new QRCodeWriter();  
            // BitMatrix bitMatrix = null;  
            // try {  
            // bitMatrix = writer.encode("濮撳悕锛氬紶涓夛紝鎬у埆锛氱敺锛屽勾榫勶細25锛岀睄璐細涓浗鍖椾含",  
            // BarcodeFormat.QR_CODE, 300, 300);  
            // MatrixToImageWriter.writeToFile(bitMatrix, "png", new  
            // File("D://test.png"));  
            // } catch (WriterException e) {  
            // e.printStackTrace();  
            // } catch (IOException e) {  
            // e.printStackTrace();  
            // }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    public static int nextInt(int end){
    	Random random = new Random();
		int val = random.nextInt(end);
		return val;
	}
    
    private static BufferedImage toBufferedImage(BitMatrix matrix) {  
        int width = matrix.getWidth();  
        int height = matrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_ARGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, COLOR[(matrix.get(x,y)?1:0)]);
            }  
        }  
        return image;  
    }  
    private static BufferedImage toBufferedImage(BitMatrix matrix1, BitMatrix matrix2, BitMatrix matrix3) {  
    	// by yinan
        int width = matrix1.getWidth();  
        int height = matrix1.getHeight();  
        BufferedImage image = new BufferedImage(width, height,  
                BufferedImage.TYPE_INT_ARGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {
            	int r = COLOR[(matrix3.get(x,y)?1:0)];
            	int g = COLOR[(matrix2.get(x,y)?1:0)]<<8;
            	int b = COLOR[(matrix1.get(x,y)?1:0)]<<16;
                image.setRGB(x, y, r+g+b+0xff000000);
            }  
        }  
        return image;  
    }  
  
}