package com.zhutou;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import coffee.common.util.FileUtils;
import coffee.tools.excel.XlsReader;
import coffee.util.database.Session;

/**
 * 研究生
 * 
 * @author wangtaoyfx
 */
public class GraduateMain {
	public static void main(String[] args) {
		String proj = FileUtils.getProjectBasePath();
		XlsReader reader = new XlsReader(proj + "/2012-2013(1)学期研究生分班表.xls");
		System.out.println(reader.getNamesOfAllSheet());
		
		List<GraduateBean> items = new ArrayList<GraduateBean>();
		
		for (String sheetName : reader.getNamesOfAllSheet()) {
			System.out.println(sheetName);
			if("博士班".equals(sheetName) || "日语班".equals(sheetName) ||
					"MFA班".equals(sheetName) )
			{
				continue;
			}
			reader = new XlsReader(proj + "/2012-2013(1)学期研究生分班表.xls", sheetName);
			List<Map<String,String>> itemsLst = reader.query(4, 2, 5, 0);
			
			System.out.println(itemsLst);
			
			for(Map<String,String> item : itemsLst)
			{
				GraduateBean bean = new GraduateBean();
				bean.setId(item.get("学号"));
				bean.setEdu(item.get("系"));
				bean.setName(item.get("姓名"));
				items.add(bean);
			}
		}
		
		Session session = new Session();
		try {
			session.insert(items);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		session.close();
	}
}
