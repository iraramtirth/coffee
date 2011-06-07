package org.coffee.tools.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * Excel通用工具类
 *  
 * @author 王涛
 */
public class XlsUtils {
	//创建xls文件
	public static boolean createXls(String xlsPath){
		HSSFWorkbook wb = new HSSFWorkbook();
		FileOutputStream out;
		try {
			out = new FileOutputStream(new File(xlsPath));
			wb.createSheet();
			wb.write(out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static String getCellValue(HSSFCell cell){
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
	
	public static void main(String[] args) {
		createXls("c:/ttt.xls");
	}
}
