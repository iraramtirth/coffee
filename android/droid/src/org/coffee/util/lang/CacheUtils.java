package org.coffee.util.lang;

import java.io.File;

import android.os.Environment;
/**
 * 清空缓存
 * @author wangtao
 */
public class CacheUtils {
	private static final String CACHE_DIR = Environment.getExternalStorageDirectory() + "/cache";
	/**
	 * 清空缓存目录
	 * @param cacheDir ： 缓存目录
	 */
	public static boolean clear(String cacheDir){
		if(cacheDir == null){
			cacheDir = CACHE_DIR;
		}
		File file = new File(cacheDir);
		boolean bool = FileUtils.delFileOrDir(file.getAbsolutePath());
		return bool;
	}
	
	/**
	 * 删除文件或者目录以及其下的子目录
	 * @param filepath
	 * @return
	 */
	public static boolean delFileOrDir(String filePath){
		boolean bool = true;
		File f = new File(filePath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						bool = bool && delFileOrDir(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					bool = bool && delFile[j].delete();// 删除文件
				}//end for
			}
		}
		return bool;
	}
	
}
