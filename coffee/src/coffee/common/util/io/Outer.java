package coffee.common.util.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 文件 | 控制台输出流 
 * @author wangtao
 */
public class Outer {
	private static OutputStream out;
	/**
	 * 文件\路径编码
	 */
	private static String enc = "UTF-8";
	/**
	 * 文件追加模式
	 */
	private static boolean append = true;
	/**
	 * 是否显示时间
	 */
	private static boolean showTime = false;
	/**
	 * 时间格式化模式
	 */
	private static String timePattern = "yyyy-MM-dd hh:mm:ss"; 
	
	/**
	 * 输出流的路径，如果不指定该属性，默认将信息输出到console
	 */
	private static String path = "";
	
	/**
	 * 设置文件输出流的路径
	 * @param path_ : 文件路径 | 如果不存在将自动创建 
	 * @param append_ : true追加 |false不追加
	 * @param delete : 是否清空(删除)文件
	 */
	public static void setPath(String path_,boolean append_,boolean delete){
		path = path_;
		append = append_;
		if(path == null){
			path = System.getProperty("user.dir") + "/outer.txt";
		}
		//删除
		if(delete){
			File file = new File(path);
			if(file.exists()){
				file.delete();
			}
		}
	}
	
	/**
	 * 为信息打印输出点额外的信息
	 * @param msg	： 处理消息
	 * @return ：返回处理后的msg
	 */
	private static String handleMsg(String msg){
		if(append){
			msg += "\r\n";
		}
		if(showTime){
			msg = "["+currentTime()+"]" + "\t" + msg;
		}
		return msg;
	}
	/**
	 * 返回当前时间
	 */
	private static String currentTime(){
		SimpleDateFormat sdf = new SimpleDateFormat(timePattern);
		return sdf.format(new Date());
	}
	/**
	 * 文件输入流：输出指定信息到指定路径的文件
	 * 默认；追加(换行)
	 * @param path : 文件输出流的路径
	 * @param msg : 流内容
	 */
	private static void p(String path,String msg){
		try {
			path = URLDecoder.decode(path, enc);
			File file = new File(path);
			if(file.exists() == false){
				FileUtils.createNewFileOrDirectory(path);
			}
			out = new FileOutputStream(file, append);
			msg = handleMsg(msg);
			out.write(msg.getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 将信息输出到控制台
	 */
	public static void p(String msg){
		//如果没指定输出文件的路径
		if(path == null || path.trim().length() == 0){
			System.out.print(msg);
		}else{
			p(path,msg);
		}
	}
	/**
	 * 将信息输出到控制台
	 * 默认信息换行
	 */
	public static void pl(String msg){
		//如果没指定输出文件的路径
		if(path == null || path.trim().length() == 0){
			System.out.println(msg);
		}else{
			p(path,msg);
		}
		
	}
	
	public static void main(String[] args) {
		String path = "c:/dd/d.txt";
		setPath(path, true, true);
		pl("ssd");
		pl("sd");
	}
}

///////////////////////////////////

/**
 * 文件相关操作 
 * @author wangtao
 */
class FileUtils {
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
