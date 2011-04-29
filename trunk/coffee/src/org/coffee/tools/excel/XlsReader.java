package org.coffee.tools.excel;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 读取excel
 * 
 * @author 王涛
 */
public class XlsReader {

	private HSSFSheet sheet;

	public XlsReader(String xlsPath) throws IOException {
		try {
			FileInputStream fis = new FileInputStream(xlsPath);
			POIFSFileSystem fs = new POIFSFileSystem(fis);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
		} catch (IOException e) {
			String msg = "读取文件失败...";
			msg += e.getMessage();
			throw new IOException(msg);
		}
	}

	/**
	 * @param columns
	 *            ：列名称 ; 支持不连续的列
	 * @param x
	 *            : 起始行， 从0开始
	 * @param y
	 *            : 其实列，从0开始
	 * @return	  : List<Map<String,String>
	 */
	public List<Map<String,String>> query(String[] columns, int x, int y) {
		List<Map<String,String>> items = new ArrayList<Map<String,String>>();
		if (sheet == null || columns == null || columns.length == 0) {
			return items;
		}
		int[] columnsIndex = new int[columns.length];
		//默认从第一行读取列索引下表
		HSSFRow hr = sheet.getRow(0);
		for (int i = 0; i < columns.length; i++) {
			for (int j = 0; j < hr.getLastCellNum(); j++) {
				HSSFCell cell = hr.getCell(j);
				String cellValue = getCellValue(cell);
				if(columns[i].equals(cellValue)){
					columnsIndex[i] = j;
				}
			}
		}
		//
		for (int i = x; i < sheet.getPhysicalNumberOfRows(); i++) {
			HSSFRow row = sheet.getRow(i);
			if (row != null) {
				Map<String,String> item = new HashMap<String,String>();
				for (int j = 0; j < columnsIndex.length; j++) {
					// 注意：当取某一行中的数据的时候，需要判断数据类型，否则会报错
					item.put(columns[j], getCellValue(row.getCell(columnsIndex[j])));
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
	 * @param cx	:	索引所在的起始行
	 * @param cy	:	索引所在的起始列
	 * @param x		:	内容起始行
	 * @param y		:	内容起始列
	 * @return
	 */
	public List<Map<String,String>> query(int cx, int cy, int x, int y) {
		if (sheet == null) {
			return new ArrayList<Map<String,String>>();
		}
		String[] columns = null;
		HSSFRow row = sheet.getRow(cx);
		if (row != null) {
			String value = "";
			columns = new String[row.getLastCellNum()];
			for (int j = cy; j < row.getLastCellNum(); j++) {
				value = this.getCellValue(row.getCell(j));
				columns[j-cy] = value;
			}
		}
		return query(columns,x,y);
	}
	
	private String getCellValue(HSSFCell cell){
		String cellValue = "";
		switch(cell.getCellType()){
		case HSSFCell.CELL_TYPE_NUMERIC:
			try{
				cellValue = (int)cell.getNumericCellValue() + "";
			} catch(Exception e){
				cellValue = cell.getNumericCellValue() + "";
			}
			break;
		default:
			cellValue = cell.getStringCellValue();
			break;
		}//
		if(cellValue.toLowerCase().matches("\\(null\\)|null")){
			cellValue = "";
		}
		return cellValue.trim();
	}
	
	
	private static void testQuery_1() throws IOException{
		//String column = "名称 	编号";
		String column = "主分类  编号";
		String[] columns = column.split("\\s+");
		String xlsPath = "F:\\工作任务\\商品列表_for我查查_.xls";
		XlsReader reader = new XlsReader(xlsPath);
		List<Map<String,String>> items = reader.query(columns, 1, 2);
		System.out.println(items.size());
		Map<String,String> newMap = new HashMap<String,String>();
		for(Iterator<Map<String,String>> it = items.iterator(); it.hasNext();){
			Map<String,String> map =  it.next();
			for (int i = 0; i < columns.length; i++) {
				System.out.print(map.get(columns[i])+"\t");
				newMap.put(map.get(columns[i]), "");
			}
			System.out.println();
		}
		//System.out.println(newMap);
	}
	public static void testQuery_2(){
		
	}
	public static void main(String[] args) {
		try {
			XlsReader.testQuery_1();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
