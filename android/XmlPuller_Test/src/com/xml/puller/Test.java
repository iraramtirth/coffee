package com.xml.puller;

import coffee.lang.Reader;
import coffee.xml.parser.XmlParser;

import com.xml.puller.bean.SingleBlogMb;

public class Test {
	public static void main(String[] args) {
		// User user = new User();
		//
		// user = new
		// XmlParser("<user><id>123</id></user>").pullerT(User.class);
		//
		// System.out.println(user.getId());

		String xml = new Reader("xml/blog/singleblog.xml").readAll();

		XmlParser parser = new XmlParser(xml);

		System.out.println(xml);

		SingleBlogMb  mb = parser.pullerT(SingleBlogMb.class);
		
		//测试BlogListMb是否正确
//		System.out.println("retn="+mb.getRetn());
//		System.out.println("statusList.size="+mb.getStatusList().size());
//		BlogStatus s1 = mb.getStatusList().get(0);
//		BlogStatus s2 = mb.getStatusList().get(1);
//		System.out.println("s1: id="+s1.getId()+" ;source="+s1.getSource()+" ;username="+s1.getBlogUser().getName());
//		System.out.println("s2: id="+s2.getId()+" ;source="+s2.getSource()+" ;username="+s2.getBlogUser().getName());
		
		//测试BlogNewMb是否正确
//		System.out.println("retn="+mb.getRetn());
//		System.out.println("source="+mb.getStatusList().get(0).getSource());
//		BlogStatus bs1 = mb.getStatusList().get(0);
//		System.out.println("1:id="+bs1.getId()+" ;text="+bs1.getText()+" ;username="+bs1.getBlogUser().getName());
		
		
		//测试BlogSearchThirdMb是否正确
//		System.out.println("desc="+mb.getDesc());
//		System.out.println("statusList.size="+mb.getStatusList().size());
//		BlogStatus s1 = mb.getStatusList().get(0);
//		BlogStatus s2 = mb.getStatusList().get(1);
//		System.out.println("s1: weiboname="+s1.getWeiboname()+" ;icon="+s1.getWeiboicon());
//		System.out.println("s2: weiboname="+s2.getWeiboname()+" ;icon="+s2.getWeiboicon());
		
		//测试BlogCommentMb是否正确
//		System.out.println("desc="+mb.getDesc());
//		System.out.println("text="+mb.getComment().getText());
//		System.out.println("source="+mb.getComment().getSource());
//		BlogUser user = mb.getComment().getUser();
//		BlogStatus status = mb.getComment().getStatus();
//		System.out.println("user: name="+user.getName()+" ;location="+user.getLocation());
//		System.out.println("status: text="+status.getText()+" ;source="+status.getSource());
//		System.out.println("status.user: name="+status.getBlogUser().getName()+" ;location="+status.getBlogUser().getLocation());
		
		//测试SingleBlogMb
		System.out.println("text="+mb.getStatus().getText());
		System.out.println("source="+mb.getStatus().getSource());
		System.out.println("user: id="+mb.getStatus().getBlogUser().getId());
//		
		//测试BlogUserInfoMb
//		System.out.println("id="+mb.getBlogUser().getId());
//		System.out.println("status: text="+mb.getBlogUser().getStatus().getText());
		
		//测试blogcommentlistmb
//		System.out.println("user: source="+mb.getCommentList().get(0).getSource());
//		System.out.println("user: name="+mb.getCommentList().get(0).getUser().getName());
//		System.out.println("status: source="+mb.getCommentList().get(0).getStatus().getSource());
//		System.out.println("status: name="+mb.getCommentList().get(0).getStatus().getBlogUser().getName());
		
		//测试BlogMentionMeMb
//		System.out.println(mb);
//		System.out.println("source="+mb.getStatusList().get(0).getSource());
//		System.out.println("name="+mb.getStatusList().get(0).getBlogUser().getName());
//		System.out.println("retweeted_status: text="+mb.getStatusList().get(0).getRetweetedStatus().getText());
//		System.out.println("user: location="+mb.getStatusList().get(0).getRetweetedStatus().getBlogUser().getLocation());
		
		//测试BlogNewTipMb
//		System.out.println("weiboID="+mb.getCountList().get(0).getWeiboid());
		
		
		//测试BlogRelayMb
//		System.out.println("source="+mb.getStatus().getSource());
//		System.out.println("user: name="+mb.getStatus().getBlogUser().getName());
//		System.out.println("retweeted_status: text="+mb.getStatus().getRetweetedStatus().getText());
		
		
	}
}
