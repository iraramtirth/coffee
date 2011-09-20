package org.coffee.tools.upload;


import java.io.File;

public class FormFile {
	
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
