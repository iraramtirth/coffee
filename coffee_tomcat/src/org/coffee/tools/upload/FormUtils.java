package org.coffee.tools.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

/**
 * 参数反射:工具类；
 * @since 1.4
 * @author wangtao
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class FormUtils {
	
	private static Map parameterMap;
	
	public static Object getParameter(String paramName){
		return parameterMap.get(paramName);
	}
	
	public static Object parser(HttpServletRequest request, Class beanClass){
		if(isMultipartContent(request)){
			Map paramMap = new MultipartStream(request).parser();
			parameterMap = paramMap;
			return invoke(parameterMap, beanClass);
		}else{
			parameterMap = handleParamerMap(request.getParameterMap());
			return invoke(parameterMap, beanClass);
		}
	}
	
	
	 /**
	   * 保存上传后的文件
	   * @param formFile : {@link util.wangtao.common.FormFile}
	   * @param projectDir ： 项目的工程目录：如 E:/workspace/mmb_server/
	   * @param uploadDir : 文件的上传路径 : 如    uoload/client/
	   * @return : 返回 uplaodDir + 文件名
	   */
	  public static String saveFormFileTo(FormFile formFile, String projectDir, String uploadDir){
			if(formFile == null){
				return null;
			}
			File file = formFile.getFile();
			try {
				FileInputStream fin = new FileInputStream(file);
				String filePath = projectDir + uploadDir + file.getName();
				File newFile = new File(filePath);
				if(newFile.exists() == false){
					newFile.getParentFile().mkdirs();
				}
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
			return uploadDir + file.getName();
		}
	
	
	/**
	 * 将parameterMap反射成Bean
	 * 
	 * @param paramterMap : 注意 该参数的value是经过处理后的，即value==string类型，而不是string[] array
	 * 					k: input name : type is string 
	 * 					v: input value :type is string array
	 * @param beanClass ： 实体Bean类型
	 * 
	 * @return ： 返回指定的 beanClass实例
	 */
	private static Object invoke(Map parameterMap, Class beanClass){
		/**
		 * 讲过处理的paramMap
		 * 将 值string[]转换成string多个值用 ','分割
		 */
		Object bean = null;
		try {
			bean = beanClass.newInstance();
			for (Iterator it = parameterMap.keySet().iterator(); it.hasNext();) {
				String inputName = it.next().toString();
				try {
					Field field = beanClass.getDeclaredField(inputName);
					Class paramType = field.getType();
					
					Method method = beanClass.getDeclaredMethod(
							"set" + inputName.substring(0, 1).toUpperCase() + inputName.substring(1),
							new Class[] { paramType });
					Object value = parameterMap.get(inputName);
					if(value == null || value.toString().trim().length() == 0){
						continue;
					}
					Object newVal = getCastedValue(paramType, value);
					method.invoke(bean, new Object[] {newVal});
				} catch (NoSuchFieldException e) {
					//log.warning("Action中的字段[" + key + "]不存在,无法进行参数映射...");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * 将制定value的值转换成所属类型的
	 * @param field
	 * @param value
	 * @return
	 */
	private static Object getCastedValue(Class fieldType, Object inputValue){
		if(inputValue instanceof FormFile){
			return null;
		}
		Object newVal = null;
		String type = fieldType.toString();
		if(fieldType.isPrimitive()){
			if(type.contains("long")){
				newVal = Long.valueOf(inputValue + "");
			}
			else if(type.contains("int")){
				newVal = Integer.valueOf(inputValue + "");
			}
			else if(type.contains("float")){
				newVal = Float.valueOf(inputValue + "");
			}
			else if(type.contains("double")){
				newVal = Double.valueOf(inputValue + "");
			}
		}else{//复合类型
			if(type.contains("Date")){
				newVal = parseDate(inputValue);
			}else if(type.contains("String")){
				newVal = inputValue;
			}
		}
		return newVal;
	}
	
	/**
	 * 处理原始的parameterMap ： 即是request.getParameterMap();
	 * 因为 request.getParameterMap()的key是 string类型 ，value是 string[]类型
	 * @param parameterMap 
	 * @return ： 返回处理后的 parameter ： k-v ： string-string
	 */
	private static Map handleParamerMap(Map parameterMap){
		Map newParamsMap = new HashMap();
		for(Iterator it= parameterMap.keySet().iterator(); it.hasNext();){
			String key = it.next().toString();
			String[] values = (String[]) parameterMap.get(key);
			String value = "";
			for (int i = 0; i < values.length; i++) {
				value += values[i];
				if((i+1) < values.length){
					value += ",";
				}
			}
			newParamsMap.put(key,value);
		}
		return newParamsMap;
	}
	
	/**
	 * 解析二进制流  request.getInputstream
	 * @author wangtao
	 */
	private static class MultipartStream {
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
							String fileName = formFile.getFileName();
							String ext = "";
							if(fileName.lastIndexOf(".") != -1){
								ext = fileName.substring(fileName.lastIndexOf("."));
							}
							File tmpFile = new File(tmpDir,UUID.randomUUID().toString() + ext);
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
		
	}
	/**
	 * 将指定的value转换成Date格式
	 * @param value
	 * @return ： 返回转型后的 java.util.Date
	 */
	 private static Date parseDate(Object value){
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
	 
	public static class FormFile {
		/**
		 * 文件名：路径经过处理的，上传时的真实路径的一部分：
		 * 即是 str.subString(str.lastInexOf("/")+1)
		 */
		private String fileName;
		/**
		 * 文件类型:[非后缀]
		 */
		private String ContentType;
		/**
		 * 上传的文件
		 */
		private File file;
		
		public String getFileName() {
			return fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public String getContentType() {
			return ContentType;
		}
		public void setContentType(String contentType) {
			ContentType = contentType;
		}
		public File getFile() {
			return file;
		}
		public void setFile(File file) {
			this.file = file;
		}
	}
	 
}