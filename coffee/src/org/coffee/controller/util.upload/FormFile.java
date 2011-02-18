package org.coffee.struts.upload;

import java.io.File;

/**
 * 文件上传组件
 * @author wangtao
 */
public class FormFile {
	private String fileName;
	private String ContentType;
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
