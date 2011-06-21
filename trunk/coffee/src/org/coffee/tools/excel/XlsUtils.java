package org.coffee.tools.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/**
 * Excel通用工具类
 *  
 * @author 王涛
 */
public class XlsUtils {
	/**
	 * 创建xls文件
	 */
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
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * 读取单元格的值
	 * @param cell
	 * @return
	 */
	public static String getCellValue(HSSFCell cell){
		String cellValue = "";
		//2011-06-09修改-start
		if(cell == null){
			return cellValue;
		}
		//2011-06-09修改 end
		switch(cell.getCellType()){
		case HSSFCell.CELL_TYPE_NUMERIC:
			//2011-06-09修改-start
			if(HSSFDateUtil.isCellDateFormatted(cell)){
			     double d = cell.getNumericCellValue();    
		         Date date = HSSFDateUtil.getJavaDate(d);    
		         cellValue = sdf.format(date);
			}else{//2011-06-09修改-end
				try{
					cellValue = (int)cell.getNumericCellValue() + "";
				} catch(Exception e){
					cellValue = cell.getNumericCellValue() + "";
				}
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
	
	/**
	 *  原始列名称
	 */
	public static String getColumnName(String srcCol){
		String colName = srcCol.trim();
		Matcher matcher = Pattern.compile(".+\\((.+)\\)$").matcher(colName);
		while (matcher.find()) {
			colName = matcher.group(1);
		}
		if(colName.trim().length() == 0){
			return srcCol;
		}
		return colName;	
	}
	
	
	public <T> List<T> toBean(List<Map<String,String>> items, Class<T> t){
		
		return null;
	}
	
	
	public static void main(String[] args) {
		String colName = XlsUtils.getColumnName("商品名称(name)");
		System.out.println(colName);
	}
}
