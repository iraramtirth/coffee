package org.coffee.util.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 文件 | 控制台输出流 
 * @author coffee
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
		if(path_ == null){
			path_ = System.getProperty("user.dir");
		}
		path = path_;
		append = append_;
		//删除
		if(delete || append == false){
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
		SimpleDateFormat sdf = new SimpleDateFormat(timePattern, Locale.getDefault());
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
				if(file.getParentFile().exists() == false){
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
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
