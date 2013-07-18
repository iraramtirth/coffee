/*
 * @(#)MailUtil.java 11-8-3 上午5:23
 * CopyRight 2011. All rights reserved
 */
package org.coffee.util.framework;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件操作封装
 * 
 * @author coffee
 * @version 1.0
 */
public class MailUtil {

	/**
	 * 发送邮件
	 * 
	 * @param info
	 *            邮件信息
	 * @throws MessagingException
	 *             MessagingException
	 */
	public static void sendMail(final MailInfo info) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", info.getSmtpHost());
		props.put("mail.smtp.auth", String.valueOf(info.isNeedAuth()));//true
		//以下方法如果session.getDefaultInstance连续发送多次的时候回报异常
		//com.sun.mail.smtp.SMTPSendFailedException: 553 Mail from must equal authorized user
		Session session =  Session.getInstance(props,
				new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(info.getFrom(), info
								.getPassword());
					}
				});
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(info.getFrom()));
		InternetAddress[] address = new InternetAddress[info.getToList().length];
		for (int i = 0; i < info.getToList().length; i++) {
			address[i] = new InternetAddress(info.getToList()[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, address);
		Date current = new Date();
		msg.setSubject(info.getSubject());
		Multipart mp = new MimeMultipart();
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap
				.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
		MimeBodyPart mbpContent = new MimeBodyPart();
		mbpContent.setText(info.getContent());
		mp.addBodyPart(mbpContent);
		msg.setContent(mp);
		msg.setSentDate(current);
		Transport.send(msg, address);
	}

	/**
	 * 邮件信息
	 */
	public static class MailInfo {
		private String from;
		private String password;
		private String[] toList;
		private String subject;
		private String content;
		private boolean needAuth;
		private String smtpHost;

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String[] getToList() {
			return toList;
		}

		public void setToList(String[] toList) {
			this.toList = toList;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public boolean isNeedAuth() {
			return needAuth;
		}

		public void setNeedAuth(boolean needAuth) {
			this.needAuth = needAuth;
		}

		public String getSmtpHost() {
			return smtpHost;
		}

		public void setSmtpHost(String smtpHost) {
			this.smtpHost = smtpHost;
		}
	}
}
