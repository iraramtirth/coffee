package org.coffee.tools.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.coffee.jdbc.dao.util.TUtils;
import org.coffee.tools.excel.XlsWriter.AppendType;

/**
 * Excel通用工具类
 * 
 * @author 王涛
 */
public class XlsUtils {
	/**
	 * 创建xls文件
	 */
	public static boolean createXls(String xlsPath) {
		HSSFWorkbook wb = new HSSFWorkbook();
		FileOutputStream out;
		try {
			File file = new File(xlsPath);
			if(file.exists() == false){
				if(file.getParentFile().exists() == false){
					file.getParentFile().mkdirs();
				}
				file.createNewFile();
			}
			out = new FileOutputStream(file);
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
	 * 
	 * @param cell
	 * @return
	 */
	public static String getCellValue(HSSFCell cell) {
		String cellValue = "";
		// 2011-06-09修改-start
		if (cell == null) {
			return cellValue;
		}
		// 2011-06-09修改 end
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_NUMERIC:
			// 2011-06-09修改-start
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				double d = cell.getNumericCellValue();
				Date date = HSSFDateUtil.getJavaDate(d);
				cellValue = sdf.format(date);
			} else {// 2011-06-09修改-end
				try {//用int将会导致转型失败，即溢出
					cellValue = (long) cell.getNumericCellValue() + "";
				} catch (Exception e) {
					cellValue = cell.getNumericCellValue() + "";
				}
			}
			break;
		default:
			cellValue = cell.getStringCellValue();
			break;
		}//
		if (cellValue.toLowerCase().matches("\\(null\\)|null")) {
			cellValue = "";
		}
		return cellValue.trim();
	}

	/**
	 * 原始列名称
	 */
	public static String getColumnName(String srcCol) {
		String colName = srcCol.trim();
		Matcher matcher = Pattern.compile(".+\\((.+)\\)$").matcher(colName);
		while (matcher.find()) {
			colName = matcher.group(1);
		}
		if (colName.trim().length() == 0) {
			return srcCol;
		}
		return colName;
	}
	/**
	 * 
	 * @param items : 从xlsReader.query的结果
	 * @return
	 */
	public static <T> List<T> toBeanList(List<Map<String, String>> items, Class<T> t) {
		List<T> resultList = new  ArrayList<T>();
		try {
			for (Map<String, String> item : items) {
				T obj = t.newInstance();
//				for (String key : item.keySet()) {
//					setValue(obj, key, item.get(key), String.class);
//				}
				for(Field field : obj.getClass().getDeclaredFields()){
					//获取其映射的name
					String fieldName =  TUtils.getColumnName(obj.getClass(), field.getName());
					field.setAccessible(true);
					field.set(obj, item.get(fieldName));
				}
				resultList.add(obj );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	public static List<String> toStringList(List<Map<String, String>> items) {
		List<String> resultList = new  ArrayList<String>();
		for (Map<String, String> itemMap : items) {
			String value = "";
			for (String key : itemMap.keySet()) {
				value += itemMap.get(key) + ",";
			}
			value = value.replaceAll(",+?$", "");
			resultList.add(value);
		}
		return resultList;
	}

	/**
	 * 生产一个用于测试的xls文档
	 */
	public static void generateXlsDemo(String xlsPath) {
		if (xlsPath == null) {
			xlsPath = "c:/text.xls";
		}
		XlsWriter writer = new XlsWriter(xlsPath);
		writer.append(new String[] { "ID", "用户名(username)", "密码(password)" },
				0, AppendType.ROW);
		writer.append(new String[] { "001", "咖啡", "1234" }, 0, AppendType.ROW);
		writer.close();
	}
	/**
	 * 赋值
	 * 废弃，该方法暂且不用
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static <T> Object setValue(T obj, String fieldName, T value, Class<?>... valueClass){
		String firstChar = fieldName.charAt(0)+"";
		try {
			String methodName = "set" + fieldName.replaceFirst(".", firstChar.toUpperCase());
			Class<?>[] paramClass = new Class[]{value.getClass()};
			if(valueClass.length > 0){
				// 对于基本数据类型，必须指定
				paramClass = new Class[]{valueClass[0]};
			}
			obj.getClass().getMethod(methodName,paramClass ).invoke(obj, new Object[]{value});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 *  获取列名 
	 */
	public static <T> String getColumnName(Class<T> clazz,String fieldName){
		Field field = null;
		try {
			field = clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Column column = field.getAnnotation(Column.class);
		if(column != null){
			return column.name();
		}else{
			return fieldName;
		}
	}
	public static void main(String[] args) {
		// String colName = XlsUtils.getColumnName("商品名称(name)");
		// System.out.println(colName);
	}
}
