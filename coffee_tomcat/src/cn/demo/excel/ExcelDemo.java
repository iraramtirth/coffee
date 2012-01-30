package cn.demo.excel;

import java.util.List;
import java.util.Map;

import org.coffee.tools.excel.XlsReader;

public class ExcelDemo {
	public static void main(String[] args) {
		XlsReader reader = new XlsReader("c:/001.xls");
		
		List<Map<String, String>> items = reader.query(new String[]{"一级", "二级", "三级"}, 1, 3, 2);
		
		System.out.println(items);
	}
}
