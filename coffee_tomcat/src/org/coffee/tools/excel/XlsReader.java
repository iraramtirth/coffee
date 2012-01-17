package org.coffee.tools.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.demo.bean.User;

/**
 * 读取excel
 * 
 * @author 王涛
 */
public class XlsReader {

	private HSSFSheet sheet;
	private HSSFWorkbook wb;
	
	public XlsReader(String xlsPath){
		try {
			FileInputStream fis = new FileInputStream(xlsPath);
			POIFSFileSystem fs = new POIFSFileSystem(fis);
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);///默认读取第一个
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 读取指定sheetName的Excel文档 
	 */
	public XlsReader(String xlsPath,String sheetName) {
		try {
			FileInputStream fis = new FileInputStream(xlsPath);
			POIFSFileSystem fs = new POIFSFileSystem(fis);
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheet(sheetName);///读取指定name的sheet
			fis.close();
		} catch (IOException e) {
			String msg = "读取文件失败...";
			msg += e.getMessage();
			//throw new IOException(msg);
		}
	}
	
	/**
	 * @param columns
	 *           	 ：	列名称 ; 支持不连续的列
	 * @param ix	:	索引所在的起始行 , 从 0开始
	 * @param iy	:	索引所在的起始列, 从0开始
	 * @param cx	:	内容起始行， 从0开始 (列索引已经根据ix,iy保存下来了)
	 * @return	 	: 	List<Map
	 */
	public List<Map<String,String>> query(String[] columns, int ix, int iy, int cx) {
		List<Map<String,String>> items = new ArrayList<Map<String,String>>();
		if (sheet == null || columns == null || columns.length == 0) {
			return items;
		}
		int[] columnsIndex = new int[columns.length];
		//默认从起始行读取列索引下表
		HSSFRow hr = sheet.getRow(ix);
		for (int i = 0; i < columns.length; i++) {
			for (int j = iy; j < hr.getLastCellNum(); j++) {
				HSSFCell cell = hr.getCell(j);
				String cellValue = XlsUtils.getCellValue(cell);
				if(columns[i].equals(cellValue)){
					columnsIndex[i] = j;
					break;
				}
			}
		}
		//获取新的列名  --> 商品名称(name) --> name
		for (int i = 0; i < columns.length; i++) {
			columns[i] = XlsUtils.getColumnName(columns[i]);
		}
		//
		for (int i = cx; i < sheet.getPhysicalNumberOfRows(); i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				// LinkedHashMap 保持key的顺序
				Map<String,String> item = new LinkedHashMap<String,String>();
				for (int j = 0; j < columnsIndex.length; j++) {
					// 注意：当取某一行中的数据的时候，需要判断数据类型，否则会报错
					item.put(columns[j], XlsUtils.getCellValue(row.getCell(columnsIndex[j])));
				}
				if(item.size() > 0){
					items.add(item);
				}
			}
		}
		return items;
	}

	/**
	 * 重载：默认把第一行的列作为索引列
	 * 仅支持连续的行,整行
	 * @param ix	:	索引所在的起始行
	 * @param iy	:	索引所在的起始列
	 * @param cx	:	内容起始行：列索引已经根据ix,iy保存下来了
	 * @return
	 */
	public List<Map<String,String>> query(int ix, int iy, int cx) {
		if (sheet == null) {
			return new ArrayList<Map<String,String>>();
		}
		String[] columns = null;
		HSSFRow row = sheet.getRow(ix);
		if (row != null) {
			String value = "";
			columns = new String[row.getLastCellNum()-iy];
			for (int j = iy; j < row.getLastCellNum(); j++) {
				value = XlsUtils.getCellValue(row.getCell(j));
				if(value != null){
					value = XlsUtils.getColumnName(value);
					columns[j-iy] = value;
				}
			}
		}
		//
		List<Map<String,String>> items = new ArrayList<Map<String,String>>();
		for (int i = cx; i < sheet.getPhysicalNumberOfRows(); i++) {
			row = sheet.getRow(i);
			if (row != null) {
				// LinkedHashMap 保持key的顺序
				Map<String,String> item = new LinkedHashMap<String,String>();
				for (int j = 0; j < columns.length; j++) {
					item.put(columns[j], XlsUtils.getCellValue(row.getCell(j)));
				}
				if(item.size() > 0){
					items.add(item);
				}
			}
		}
		return items;
	}
	
	
	/**
	 *  读取所有sheet的name
	 */
	public String[] getNamesOfAllSheet(){
		int sheets = wb.getNumberOfSheets();
		String[] sheetNames = new String[sheets];
		for(int i=0; i<sheets; i++){
			sheetNames[i] = wb.getSheetName(i);
		}
		return sheetNames;
	}
	/**
	 * 读取第一行 
	 */
	public String[] getTitlesOfSheet() {
		List<String> columns = new ArrayList<String>();
		//默认从第一行读取列索引下表
		HSSFRow hr = sheet.getRow(0);
			for (int j = 0; j < hr.getLastCellNum(); j++) {
				HSSFCell cell = hr.getCell(j);
				String cellValue = XlsUtils.getCellValue(cell);
				columns.add(cellValue);
			}
		return (String[])columns.toArray(new String[0]);
	}
	
	
	public static void main(String[] args) {
//		XlsReader reader = new XlsReader("c:/test.xls");
//		List<Map<String,String>> result = reader.query(new String[]{"价格(name)", "备注(remark)"},0, 0, 1);
//		System.out.println(result);
		
	
		XlsReader reader = new XlsReader("c:/text.xls");
		
		List<Map<String, String>> items = reader.query(0, 0, 1);
		List<User> userList = XlsUtils.toBeanList(items, User.class);
		System.out.println(userList);
		
	}
}
