package org.coffee.tools.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * 解析二进制流  request.getInputstream
 * @author wangtao
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class MultipartStream {
	/**
	 * 从request.getInputstream获取的待解析的流
	 */
	private ServletInputStream in;
	/**
	 * 解析出的参数映射
	 * k:表单的input的name属性
	 * v:表单.......的value属性
	 * 如果是二进制流的话 V则是FormFile
	 */
	private Map parameterMap = new Hashtable();
	/**
	 * 分界符
	 */
	private String boundary;
	/**
	 * request的字符编码
	 */
	private String charset = "UTF-8";
	/**
	 * 缓冲区；保存每行流内容
	 */
	private byte[] buffer = new byte[256];
	
	private int len = 0;
	
	//作用于文本的正则
	private Pattern textPat = Pattern.compile("name=\"(.+?)\"");
	// 用于file流的正则
	private Pattern filePat = Pattern.compile("name=\"(.+?)\";\\s+filename=\"(.*?)\"");
	// 文件类型
	private Pattern typePat = Pattern.compile("Content-Type:\\s(.+)");
	
	/**
	 * 临时文件的存放目录
	 * E:\Tomcat\\work\Catalina\localhost\project_name
	 */
	private transient File tmpDir;
	/**
	 * 初始化上传参数
	 * @param request
	 */
	public MultipartStream(HttpServletRequest request){
		try {
			this.in = request.getInputStream();
			// 默认是UTF-8
			if(request.getCharacterEncoding() != null){
				this.charset = request.getCharacterEncoding();
			}
			String contentType = request.getContentType();
			/**
			 * contentType的格式如下;如果request参数为空, 则contentType==null
			 * multipart/form-data; boundary=---------------------------7da8c2d50206
			 */
			//如果request
			if(contentType != null){
				this.boundary = request.getContentType().replaceAll(".+boundary=", "--");
			}
			//临时文件存放的根路径
			this.tmpDir = (File)request.getSession().getServletContext().getAttribute("javax.servlet.context.tempdir");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析流的内容
	 * ---------
	 * @return 参数映射为Map
	 * k:表单的input的name属性
	 * v:表单.......的value属性
	 * 如果是二进制流的话 V则是FormFile
	 */
	public Map parser(){
		String line = null;
		/**
		 * 开始读取流内容；按行读取
		 */
		while ((line = readLine()) != null) {
			if (line.startsWith("Content-Disposition: form-data;")) {
				int i = line.indexOf("filename=");
				if (i >= 0) { 
					// 如果一段分界符内的描述中有filename=,说明enctype="multipart/form-data"
					parserFileStream(line);
				}else{ // 解析文本流
					parserTextStream(line);
				}
			}
		}
		return parameterMap;
	}
	
	/**
	 * 
	 * @param map : 该map为 {@link #parser()} 后返回的结果
	 * @return : 返回反射后的对象
	 */
	public <T> T toBean(Map map, Class<T> beanClass) {
		T obj = null;
		try{
			obj = beanClass.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
		for (Iterator it = this.parameterMap.keySet().iterator(); it.hasNext();) {
			String inputName = it.next().toString();
			try {
				Field field = beanClass.getDeclaredField(inputName);
				Class paramType = field.getType();
				Method method = beanClass.getDeclaredMethod(
						"set" + inputName.substring(0,1).toUpperCase() + inputName.substring(1),
						new Class[] { paramType });
				Object value = this.parameterMap.get(inputName);
				if(value == null || value.toString().trim().length() == 0){
					continue;
				}
				Object newVal = value;
				if(field.getType().isPrimitive()){
					String type = field.getType().toString();
					if(type.contains("long")){
						newVal = Long.valueOf(value + "");
					}
					else if(type.contains("int")){
						newVal = Integer.valueOf(value + "");
					}
					else if(type.contains("float")){
						newVal = Float.valueOf(value + "");
					}
					else if(type.contains("double")){
						newVal = Double.valueOf(value + "");
					}
				}
				if(field.getType().equals("Date")){
					newVal = parseDate(value);
				}
				method.invoke(obj,new Object[] {newVal});
			} catch (NoSuchFieldException e) {
				//log.warning("Action中的字段[" + key + "]不存在,无法进行参数映射...");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}  
		return obj;
	}
	
	/**
	 * 解析file域
	 * @param line : 从流中读取的流内容（一行）
	 */
	private void parserFileStream(String line){
		try{
			Matcher fileMat = filePat.matcher(line);
			while(fileMat.find()){
				// 解析 ：参数名称、文件名
				FormFile formFile = new FormFile();
				String paramName = fileMat.group(1);
				// 截取末尾的.file后缀
				//paramName = paramName.substring(0,paramName.lastIndexOf("."));
				formFile.setFileName(fileMat.group(2));
				// 下一行：解析content-Type
				String nextLine =  readLine();
				fileMat = filePat.matcher(nextLine);
				Matcher typeMat = typePat.matcher(nextLine);
				if(typeMat.find()){
					String fileType = typeMat.group(1);
					formFile.setContentType(fileType);
					if (fileType.trim().equals("application/octet-stream")) {
						// 文件内容为空
						continue;
					}else{// 文件内容非空， 则继续解析文件(内容)
						while ((line = readLine()) != null) {
							if (line.length() <= 2) {
								break;
							}
						}
						File tmpFile = new File(tmpDir,UUID.randomUUID().toString()+".tmp");
						FileOutputStream dos = new FileOutputStream(tmpFile);
						while ((line = readLine(charset)) != null) {
							if (line.indexOf(this.boundary) != -1) {
								break;
							}
							dos.write(buffer, 0, len);
						}
						dos.close();
						formFile.setFile(tmpFile);
						this.parameterMap.put(paramName, formFile);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 解析text域
	 * @param line : 从流中读取的流内容（一行）
	 */
	private void parserTextStream(String line){
		Matcher textMat = textPat.matcher(line);
		if (textMat.find()) {
			String paramName = textMat.group(1).trim();
			/**
			 * Content-Disposition: form-data; name="..."
			 * 因为表单域的名称跟值之间有一个换行即之间多了一个\n\r
			 * 所以多读取一行
			 */
			this.readLine(charset);
			String nextLine = this.readLine(charset);
			if(nextLine.trim().length() > 0){
				this.parameterMap.put(paramName, nextLine.trim());
			}
		}
	}
	/**
	 * 从ServletInputStream流中[按照指定编码方式]读取一行内容
	 * @param charset : 按照指定的编码方式读取文件 
	 * @return : 读取的内容
	 */
	private String readLine(String charset) {
		String bufferTemp = "";
		try {
			len = in.readLine(this.buffer, 0, this.buffer.length);
			if (len == -1) {
				return null;
			}
			//text流 ：解析InputStream流的
			bufferTemp = new String(this.buffer, 0, len, charset);				
		} catch (Exception ex) {
			bufferTemp = null;
		}
		return bufferTemp;
	}
	/**
	 *  读取未经过编码的流 ：按行读取
	 */
	private String readLine(){
		String bufferTemp = "";
		try {
			len = in.readLine(this.buffer, 0, this.buffer.length);
			if (len == -1) {
				return null;
			}
			bufferTemp = new String(this.buffer, 0, len);				
		} catch (Exception ex) {
			bufferTemp = null;
		}
		return bufferTemp;
	}
	/**
	 * 判断该request请求是否是MULTIPART
	 * @param request : servlet中的request请求
	 * @return : true - 表单按照二进制流方式提交 ；false 普通方式(字符串)提交
	 */
	 public static final boolean isMultipartContent(
	            HttpServletRequest request) {
        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toUpperCase().startsWith("MULTIPART")) {
            return true;
        }
        return false;
    }
	 
	 private Date parseDate(Object value){
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(value.toString());
		 } catch (Exception e) {
			try{
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				return sdf.parse(value.toString());
			} catch(Exception ex){
				try{
					sdf = new SimpleDateFormat("HH:mm:ss");
					return sdf.parse(value.toString());
				} catch(Exception exc){
				}
			} 
			return null;
		}
	}
}
