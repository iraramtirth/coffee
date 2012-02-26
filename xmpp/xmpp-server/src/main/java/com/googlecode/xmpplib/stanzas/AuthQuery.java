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
import com.googlecode.xmpplib.provider.AuthenticationController;
import com.googlecode.xmpplib.provider.AuthenticationListener;

/**
 * This is an implementation of the obsolete XEP-0078: Non-SASL Authentication
 * XMPP standard. See http://xmpp.org/extensions/xep-0078.html for more
 * information.
 */
public class AuthQuery extends PacketProcessor implements AuthenticationListener {
//TODO	private AuthenticationController authenticationController;

	private int currentTag = -1;
	
	private String username;
	private String password;
	private String digest;
	private String resource;

	public AuthQuery(StreamProcessor handler, AuthenticationController authenticationController) {
		super(handler);
//TODO		this.authenticationController = authenticationController;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDigest() {
		return digest;
	}

	public void setDigest(String digest) {
		this.digest = digest;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	@Override
	protected void handleStartTag() throws XmlPullParserException, IOException {
		super.handleStartTag();
		username = null;
		password = null;
		digest = null;
		resource = null;
	}

	@Override
	protected void handleEndTag() throws XmlPullParserException, IOException {
		super.handleEndTag();
		
		if (username != null && password == null && digest == null) {
			StringBuffer xmpp = new StringBuffer();
			xmpp.append("<iq xmlns='jabber:client' type='result' id='").append(handler.iq.getId()).append("'");
			xmpp.append(" from='127.0.0.1'");
			xmpp.append(" to='").append("christoph@127.0.0.1/nimo-13mbp").append("'");
			xmpp.append(">");
			xmpp.append("<query xmlns='jabber:iq:auth'>");
			xmpp.append("<username>").append(username).append("</username>");
			xmpp.append("<digest/>");
			xmpp.append("<resource/>");
			xmpp.append("</query>");
			xmpp.append("</iq>");
			handler.getXmlWriter().write(xmpp.toString());
		} else if (username != null && (password != null || digest != null)) {
			StringBuffer xmpp = new StringBuffer();
			xmpp.append("<iq xmlns='jabber:client' type='result' id='").append(handler.iq.getId()).append("'");
			xmpp.append(" from='127.0.0.1'");
			xmpp.append(" to='").append("christoph@127.0.0.1/nimo-13mbp").append("'");
			xmpp.append("/>");
			handler.getXmlWriter().write(xmpp.toString());
		} else {
			StringBuffer xmpp = new StringBuffer();
			xmpp.append("<iq xmlns='jabber:client' type='error' id='").append(handler.iq.getId()).append("'");
			xmpp.append(" from='127.0.0.1'");
			xmpp.append(" to='").append("christoph@127.0.0.1/nimo-13mbp").append("'");
			xmpp.append("<error code='406' type='modify'><not-acceptable xmlns='urn:ietf:params:xml:ns:xmpp-stanzas'/></error>");
			xmpp.append("/>");
			handler.getXmlWriter().write(xmpp.toString());
		}
	}

	@Override
	protected void handleEvent() throws XmlPullParserException, IOException {
		super.handleEvent();
		
		if (type == XmlPullParser.START_TAG) {
			String tag = getXmlPullParser().getName();
			if (tag.equals("username")) {
				currentTag = 0;
			} else if (tag.equals("password")) {
				currentTag = 1;
			} else if (tag.equals("digest")) {
				currentTag = 2;
			} else if (tag.equals("resource")) {
				currentTag = 3;
			}
		} else if (type == XmlPullParser.TEXT) {
			String text = getXmlPullParser().getText();
			if (currentTag == 0) {
				username = text;
			} else if (currentTag == 1) {
				password = text;
			} else if (currentTag == 2) {
				digest = text;
			} else if (currentTag == 3) {
				resource = text;
			}
		}
	}

	public void receivePasswordFormat(Serializable xmppData, String username,
			String passwordFormat) {
		// TODO Auto-generated method stub
		
	}

	public void receivePassword(Serializable xmppData, String username,
			String password) {
		// TODO Auto-generated method stub
		
	}

	public void receivePasswordError(Serializable xmppData, String username) {
		// TODO Auto-generated method stub
		
	}
}
