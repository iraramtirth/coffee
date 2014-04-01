package com.zhutou;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import coffee.common.util.FileUtils;
import coffee.tools.excel.XlsReader;
import coffee.util.database.Session;

/**
 * 本科生 2008 建筑学院
 * 
 * @author coffee
 */
public class Main {
	public static void main(String[] args) {

		String proj = FileUtils.getProjectBasePath();
		// XlsReader reader = new XlsReader(proj + "/2011-2012学年本科生名册(电子版).xls",
		// "建筑");
		// List<Map<String,String>> itemsLst = reader.query(2, 2, 294, 380);

		XlsReader reader = new XlsReader(proj + "/2011-2012学年本科生名册(电子版).xls");

		List<StudentBean> items = new ArrayList<StudentBean>();
		boolean start = false;

		for (String sheetName : reader.getNamesOfAllSheet()) {
			System.out.println(sheetName);
			if ("中国画".equals(sheetName)) {
				start = true;
			}
			if ("留学生".equals(sheetName)) {
				start = false;
			}
			if (!start) {
				continue;
			}
			reader = new XlsReader(proj + "/2011-2012学年本科生名册(电子版).xls",
					sheetName);
			List<Map<String, String>> lst = reader.query(2, 2, 3, 0);
			System.out.println(lst);

			for (Map<String, String> item : lst) {
				if (item.get("学号").trim().length() < 4 || "131001171".equals(item.get("学号"))){
						// || !"08".equals(item.get("学号").trim().subSequence(2, 4))) {
					continue;
				}
				StudentBean stu = new StudentBean();
				stu.setId(item.get("学号"));
				stu.setEdu(item.get("专业方向"));
				if("".equals(stu.getEdu()) || null == stu.getEdu())
				{
					stu.setEdu(item.get("所在工作室"));
				}
				stu.setSrc(item.get("生源地"));
				stu.setName(item.get("姓名"));
				stu.setSex(item.get("性别"));
				stu.setRemark(item.get("备注"));
				if("".equals(stu.getEdu()) || null == stu.getEdu() || stu.getName().equals("夏婧"))
				{
					System.out.println("ok");
					//stu.setEdu(item.get("所在工作室"));
				}
				items.add(stu);
			}
		}

		Session session = new Session();
		try {
			session.insert(items);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.close();
	}
}
