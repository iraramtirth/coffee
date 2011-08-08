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
	 * @param path
	 */
	public void saveFormFileTo(FormFile formFile, String path){
		try {
			System.out.println(formFile.getContentType());
			
			File file = formFile.getFile();
			FileInputStream fin = new FileInputStream(file);
//			String savePath = file.getname
			FileOutputStream fout = new FileOutputStream(path + file.getName());
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
	}
}
