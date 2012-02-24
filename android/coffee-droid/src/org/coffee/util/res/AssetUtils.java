package org.coffee.util.res;

import java.io.BufferedInputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetManager;

public class AssetUtils {
	/**
	 *  获取文件的内容 
	 */
	public static String getText(Activity context, String fileName){
		AssetManager manager = context.getAssets();
		StringBuilder doc = new StringBuilder();
		BufferedInputStream bin = null;
		try {
			bin = new BufferedInputStream(manager.open(fileName));
			byte[] data = new byte[1024 * 10];
			int len = -1;
			while( (len = bin.read(data)) != -1 ){
				doc.append(new String(data, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				if(bin != null){
					bin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return doc.toString();
	}
	
}
