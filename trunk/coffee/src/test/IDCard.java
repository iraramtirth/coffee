package test;

import java.util.ArrayList;
import java.util.List;

import coffee.common.util.io.Outer;
import coffee.tools.crawl.CrawlerUtils;

public class IDCard {
	public static void main(String[] args) {
		String linkUrl = "http://qq.ip138.com/idsearch/index.asp?action=idcard&userid=";
		String content = null;
		List<String> items = new ArrayList<String>();
		try {

			java.text.DecimalFormat format = new java.text.DecimalFormat("0000");
			Outer.setPath("~/idcard.txt", true, false);
			// 石家庄—130101-8 保定130600-4
			String idcard = "";
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 10000; j++) {
					idcard = "13010" + i + "19891224" + format.format(j);
					content = CrawlerUtils.getDocumentHtml(linkUrl + idcard, 3,
							"gb2312");
					System.out.println(content);
					if (!content.contains("该18位身份证号校验位不正确")) {
						if (content.contains("<td class=\"tdc2\">女</td>")) {
							items.add(idcard);
							Outer.pl(idcard);
						}
					}
				}
			}
			System.out.println(items);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
