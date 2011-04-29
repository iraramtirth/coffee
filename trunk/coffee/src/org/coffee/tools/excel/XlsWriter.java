package org.coffee.tools.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XlsWriter {
	private HSSFWorkbook wb; 
	private HSSFSheet sheet;
	private FileOutputStream out;
	
	public XlsWriter(String xlsPath) throws IOException {
		try {
			File file = new File(xlsPath);
			if(file.exists() == false){
				XlsUtils.createXls(xlsPath);
			}
			FileInputStream fs = new FileInputStream(xlsPath);
			POIFSFileSystem ps = new POIFSFileSystem(fs);
			wb = new HSSFWorkbook(ps);
			sheet = wb.getSheetAt(0);
			out = new FileOutputStream(new File(xlsPath));
		} catch (IOException e) {
			String msg = "读取文件失败...";
			msg += e.getMessage();
		}
	}
	/**
	 * 追加
	 * @param columns ：待追加的数据
	 * @param x ：骑士列
	 */
	public void append(String[] columns, int y) {
		HSSFRow row=sheet.getRow(0); 
		row = sheet.createRow(sheet.getLastRowNum()+1);
		for (int i = 0; i < columns.length; i++) {
			row.createCell(i+y).setCellValue(columns[i]);
		}
	}

	public void close(){
		try {
			out.flush();
			wb.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		String xlsPath = "F:\\工作任务\\test.xls";
		String column = "名称	价格	主分类	子分类	介绍	图片	品牌	ID	编号";
		try {
			new XlsWriter(xlsPath).append(column.split("\\s+?"), 12);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
