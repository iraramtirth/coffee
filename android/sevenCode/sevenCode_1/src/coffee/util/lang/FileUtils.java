package coffee.util.lang;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
/**
 * @author wangtao
 */
public class FileUtils {
	private static String enc = "UTF-8";

	/**
	 * 读取指定文件下的全部内容
	 * 
	 * @param filePath
	 */
	public static String read(String filePath) {
		byte[] buffer = new byte[1024];
		int len = -1;
		StringBuilder sb = new StringBuilder();
		try {
			BufferedInputStream bin = new BufferedInputStream(
					new FileInputStream(filePath));
			while ((len = bin.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, len));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 创建文件或者目录
	 * @param path
	 * @return
	 */
	public static boolean createNewFileOrDir(String path) {
		boolean bool = false;
		try {
			path = decode(path);
			File file = new File(path);
			if (file.exists() == false) {
				if (file.isDirectory()) {
					bool = file.mkdirs();
				} else {// 创建多级目录
					String parentPath = path
							.substring(0, path.lastIndexOf("/"));
					File parent = new File(parentPath);
					if (parent.exists() == false) {
						/**
						 * 注意：需要权限 android.permission.WRITE_EXTERNAL_STORAGE
						 */
						bool = parent.mkdirs();
					}
					bool = file.createNewFile();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	/**
	 * 解密文件路径
	 * 一般用于将%20等符号转化为空格，等
	 * @param path
	 *            :
	 * @return : 返回解码后的文件路径
	 */
	private static String decode(String path) {
		try {
			path = URLDecoder.decode(path, enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return path;
	}

}
