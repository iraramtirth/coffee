package coffee.tools.mail;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import coffee.common.util.io.Outer;
import coffee.tools.excel.XlsReader;
import coffee.tools.excel.XlsUtils;


public class MailSender {
	
	public static void main(String[] args) {//1265999498  bjk9999
		MailSender sender = new MailSender(SmtpType.SMTP_QQ,"1827895109","souvoum");
		
		String uuid = UUID.randomUUID().toString().replace("-","").toUpperCase();
		String subject = "招财旺福 开运护身["+uuid.substring(10,15)+"]";
		String content = "十二星座幸运水晶手串60元;<br/>" +
				"925银唯美情侣吊坠见证爱情;<br/>" +
				"翡翠吊坠99元专场限时开放!<br/>" +
				"<a href=\"http://em.mmb.cn/wap/Column.do?columnId=520377&"+uuid+"\"> 马上去看看>> </a><br/>"+
				"<img src=\"cid:image\"/><br/>"+
				"买卖宝商城祝您天天开心!<br/>" +
				""+UUID.randomUUID();
		Map<String,String> imgMap = new HashMap<String, String>();
		imgMap.put("image", "c:/mmb.jpg");
		
		XlsReader reader = new XlsReader("F:\\工作任务\\5000 QQ号.xls");
		List<Map<String,String>> itemsMap = reader.query(0, 0, 0,0 );
		List<String> qqList = XlsUtils.toStringList(itemsMap);
		System.out.println(qqList);
		Outer.setPath("c:/email_qq.log", true, true);
		//
//		qqList.clear();
//		qqList.add("245355193");
//		qqList.add("786792111");
		
		for(String qq : qqList){
			sender.sendTo(qq+"@qq.com", subject ,content,imgMap);
			Outer.pl(qq+"\tok");
			try {
				Thread.sleep(1000 * 5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//sender.sendTo("26139027@qq.com", subject ,content,imgMap);
//		sender.sendTo("786792111@qq.com", subject ,content,imgMap);
//		new MailSender().sendTo("245355193@qq.com");
	}
	
	private String smtp = "smtp.qq.com"; 	//邮件服务器 （默认）
	private String username = "786792111";	//用户名
	private String password = "&*779899";	//密码
	//发件人
	private String from;
	
	public interface SmtpType{
		public static final String SMTP_126 = "smtp.126.com";
		public static final String SMTP_QQ = "smtp.qq.com";
	}
	
	public MailSender(){
		this.from = username +"@" + smtp.substring(smtp.indexOf(".")+1);
	}
	//
	public MailSender( String smtp,String username, String password){
		this.smtp = smtp;
		this.username = username;
		this.password = password;
		from = username +"@" + smtp.substring(smtp.indexOf(".")+1);
	}

	EmailAuthenticator auth = new EmailAuthenticator(this.username,
			this.password);
	Properties props = System.getProperties();
	{
		props.put("mail.smtp.host", smtp);	//设置smtp服务器
		props.put("mail.smtp.auth", "true");//设置是否需要auth认证
	}
	Session session = Session.getDefaultInstance(props);
	/**
	 * 发送邮件
	 * @param to	: 接收人
	 * @param subject : 主题
	 * @param content : 内容
	 * @param imgUrls ：  如果内容中包含img标记，则该参数的K-V分别表示
	 */
	public void sendTo(String to,String subject,String content,Map<String,String> imgMap) {
		//每次创建一个msg
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from));
			//收件人
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSubject(subject);	//主题
			msg.setSentDate(new Date());//日期

			//mmp为邮件内容
            MimeMultipart mmp = new MimeMultipart();
            mmp.setSubType("related");   
            
            //文本内容
            MimeBodyPart htmlTextPart = new MimeBodyPart();   
            htmlTextPart.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>"  
                            + content, "text/html;charset=UTF-8");   
            mmp.addBodyPart(htmlTextPart);   
            //html中的引用的img标记
            if(imgMap  != null){
            	 for(String imageId : imgMap.keySet()){
     	            MimeBodyPart messageBodyPart = new MimeBodyPart();
     	            DataSource fds = new FileDataSource(imgMap.get(imageId));
     	            messageBodyPart.setDataHandler(new DataHandler(fds));
     	            messageBodyPart.setHeader("Content-ID","<"+imageId+">");
     	            mmp.addBodyPart(messageBodyPart);
                 }
            }
            msg.setContent(mmp);/// 设置邮件内容
            
			Transport transport = session.getTransport("smtp");
			transport.connect(smtp, from, password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			System.out.println(to+"\t\tOK");
		} catch (Exception e) {
			e.printStackTrace();
			Outer.pl(to+"\t failed");
		}
	}
}
/**
 * email权限验证 
 * @author 王涛
 */
class EmailAuthenticator extends Authenticator {
	
	private String username, password;
	
	public EmailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(this.username, this.password.toCharArray());
	}
}
