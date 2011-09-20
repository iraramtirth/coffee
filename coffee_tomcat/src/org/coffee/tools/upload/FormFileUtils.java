package org.coffee.tools.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 2011-06-09
 * @author 王涛
 */
public class FormFileUtils {
	
	/**
	 * @param formFile
	 * @param saveDir : 文件的保存目录
	 * @return 返回文件的完成路径
	 */
	public static String saveFormFileTo(FormFile formFile, String saveDir){
		String filePath = "";
		try {
			File file = formFile.getFile();
			FileInputStream fin = new FileInputStream(file);
			filePath = saveDir + file.getName();
			FileOutputStream fout = new FileOutputStream(filePath);
			byte[] data = new byte[1024 * 10];
			int len = 0;
			while((len = fin.read(data)) != -1){
				fout.write(data, 0, len);
			}
			fout.close();
			fin.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return filePath;
	}
}
