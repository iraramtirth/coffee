package org.coffee.struts.upload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

public class MultipartStream {
	/**
	 * 从request.getInputstream获取的待解析的流
	 */
	private ServletInputStream in;
	/**
	 * 解析出的参数映射
	 */
	private Map<String, Object> parameterMap = new Hashtable<String, Object>();
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
	public MultipartStream(HttpServletRequest request){
		try {
			this.in = request.getInputStream();
			this.charset = request.getCharacterEncoding();
			this.boundary = request.getContentType();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.charset);
	}
	
	/**
	 *  解析流的内容
	 */
	public Map<String, Object> parser(){
		String line = null;
		while ((line = readLine()) != null) {
			System.out.println(line);
			// 文件流
			if (line.startsWith("Content-Disposition: form-data;")) {
				int i = line.indexOf("filename=");
				if (i >= 0) { 
					// 如果一段分界符内的描述中有filename=,说明是文件的编码内容
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
	private void  parserFileStream(String line){
		try{
			Matcher fileMat = filePat.matcher(line);
			while(fileMat.find()){
				// 解析 ：参数名称、文件名
				FormFile formFile = new FormFile();
				String paramName = fileMat.group(1);
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
						File file = new File("c:/11.jpg");
						FileOutputStream dos = new FileOutputStream(file);
						while ((line = readLine()) != null) {
							if (line.indexOf(this.boundary) != -1) {
								break;
							}
							dos.write(buffer, 0, len);
						}
						dos.close();
						formFile.setFile(file);
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
			this.readLine();
			String nextLine  = this.readLine();
			System.out.println(nextLine);
			if(nextLine.trim().length() > 0){
				parameterMap.put(paramName, nextLine.trim());
			}
		}
	}
	/**
	 * 从ServletInputStream流中读取一行内容
	 * @param data 
	 * @return :
	 */
	private String readLine() {
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
	 * @param request
	 * @return
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
}
