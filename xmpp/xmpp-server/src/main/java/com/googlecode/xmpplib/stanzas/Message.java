/*
 * Copyright (C) 2009 by all constributors of the android-xmpp-server project
 * available under http://code.google.com/p/android-xmpp-server/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.   
 */
package com.googlecode.xmpplib.stanzas;

import java.io.IOException;
import java.io.Serializable;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.googlecode.xmpplib.StreamProcessor;
import com.googlecode.xmpplib.provider.MessageController;
import com.googlecode.xmpplib.provider.MessageListener;

public class Message extends PacketProcessor implements MessageListener {

	private MessageController messageController;

	private String id;
	private String type;
	private MessageListener.Message message = new MessageListener.Message();

	private int currentTag = -1;

	public Message(StreamProcessor handler, MessageController messageController) {
		super(handler);
		this.messageController = messageController;
	}

	@Override
	protected void handleStartTag() throws XmlPullParserException, IOException {		
		super.handleStartTag();
		
		id = getXmlPullParser().getAttributeValue(null, "id");
		type = getXmlPullParser().getAttributeValue(null, "type");
		if (type == null) {
			type = "normal";
		}
		message.from = getXmlPullParser().getAttributeValue(null, "from");
		message.to = getXmlPullParser().getAttributeValue(null, "to");
		message.subject = null;
		message.text = null;
		message.thread = null;
	}

	@Override
	public void handleEndTag() throws XmlPullParserException, IOException {
		super.handleEndTag();
		messageController.handleIncomingMessage(id, message);
	}

	@Override
	protected void handleEvent() throws XmlPullParserException, IOException {
		super.handleEvent();
		
		if (super.type == XmlPullParser.START_TAG) {
			String tag = getXmlPullParser().getName();
			
			System.out.println("message find child: " + tag);
			if (tag.equals("subject")) {
				currentTag = 0;
			} else if (tag.equals("body")) {
				currentTag = 1;
			} else if (tag.equals("thread")) {
				currentTag = 2;
			} else {
				currentTag = -1;
			}
		} else if (super.type == XmlPullParser.TEXT && currentTag == 0) {
			message.subject = getXmlPullParser().getText();
			currentTag = -1;
		} else if (super.type == XmlPullParser.TEXT && currentTag == 1) {
			message.text = getXmlPullParser().getText();
			currentTag = -1;
		} else if (super.type == XmlPullParser.TEXT && currentTag == 2) {
			message.thread = getXmlPullParser().getText();
			currentTag = -1;
		}
	}

	public void handleSendSuccessful(Serializable xmppData) {
		
	}

	public void handleSendFailure(Serializable xmppData) {
		
	}

	public void handleIncomingMessage(Serializable xmppData, MessageListener.Message message) {
		StringBuffer xmpp = new StringBuffer();
		xmpp.append("<message xmlns='jabber:client' id='").append(xmppData).append("'");
		xmpp.append(" from='").append(message.from).append("'");
		xmpp.append(" to='").append(message.to).append("'");
		xmpp.append(">");
		if (message.subject != null) {
			xmpp.append("<subject>").append(message.subject).append("</subject>");
		}
		if (message.text != null) {
			xmpp.append("<body>").append(message.text).append("</body>");
		}
		if (message.subject != null) {
			xmpp.append("<subject>").append(message.thread).append("</subject>");
		}
		xmpp.append("</message>");
		
		try {
			handler.getXmlWriter().write(xmpp.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
