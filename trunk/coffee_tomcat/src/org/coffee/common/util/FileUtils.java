package org.coffee.common.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件相关操作 
 * @author wangtao
 */
public class FileUtils {
	private static String enc = "UTF-8";
	/**
	 * 存放结果的List<String>
	 */
	private static List<String> filesList = new ArrayList<String>();
	/**
	 * 遍历指定路径下的所有符合条件的文件路径
	 * @param path : 路径
	 * @param extRegx  文件后缀 ：正则表达式(完全匹配)
	 * @return 符合条件的文件列表
	 */
	public static List<String> listFiles(String path,String extRegx){
		//清空存放结果的list
		filesList.clear();
		//深度遍历
		traverse(new File(path),extRegx);
		return filesList;
	}
	/**
	 * 递归深度遍历文件夹 
	 */
	private static void traverse(File file,final String extRegx){
		final Pattern ptn = Pattern.compile(extRegx); 
		file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				Matcher mat = ptn.matcher(file.getPath().substring(file.getPath().lastIndexOf('.')+1));
				if(file.isDirectory()){
					traverse(file,extRegx);
				}else{
					if(mat.matches()){
						filesList.add(file.getPath());
					}
				}//返回结果已经能够没意义了
				return false;
			}
		});
	}
	
	/**
	 * 创建指定路径的[文件]
	 * @param path : 路径
	 * @return : 返回结果  ：true - 创建成功
	 */
	public static boolean createNewFileOrDirectory(String path){
		boolean bool = false;
		try {
			path = decode(path);
			File file = new File(path);
			if(file.exists() == false){
				if(file.isDirectory()){
					bool = file.mkdirs();
				}else{
					String parentPath = path.substring(0,path.lastIndexOf("/"));
					File parent = new File(parentPath);
					if(parent.exists() == false){
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
	 * 解密文件路径
	 * @param path : 
	 * @return : 返回解码后的文件路径
	 */
	private static String decode(String path){
		try {
			path = URLDecoder.decode(path,enc);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return path;
	}
	
	/**
	 * 获取一个类的绝对路径
	 * 适用于web工程,java工程
	 * @param Clazz 
	 */
	public static <T> String getAbsolutePath(Class<T> clazz){
		return clazz.getResource("").getPath();
	}
	/**
	 * 获取工程的根目录 
	 * 可以在web/java工程中调用
	 */
	public static String getProjectBasePath(){
		return FileUtils.class.getClassLoader().getResource("/").getPath();
	}
	/**
	 * 获取文件的绝对路径 
	 * 适用于web工程  
	 * @param page : 相对于webRoot的页面
	 */
	public static String getAbsolutePath(String page){
		String base = getProjectBasePath();
		return base + "/WebRoot" + page; 
	}
	
	
	public static void main(String[] args) {
		String path = "E:/2010登记全部导入表";
		List<String> ls = listFiles(path, "xls");
		for(String file : ls){
			System.out.println(file);
		}
	}
}
