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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.googlecode.xmpplib.Stream;

public class Bind extends XmlPuller {
	private int currentTag = -1;

	public String resource;

	public Bind(Stream handler) {
		super(handler);
	}

	@Override
	protected void handleStartTag() throws XmlPullParserException, IOException {
		super.handleStartTag();
		
		String tag = getXmlPullParser().getName();
		System.out.println("bind tag: " + tag);
	}

	@Override
	protected void handleEndTag() throws XmlPullParserException, IOException {
		super.handleEndTag();

		String jid = "christoph@127.0.0.1/" + resource;
		System.out.println("finish bind! send jid: " + jid);

		StringBuffer xmpp = new StringBuffer();
		xmpp.append("<iq xmlns='jabber:client' type='result' id='").append(handler.iq.getId()).append("'");
		xmpp.append(" from='127.0.0.1'");
		xmpp.append(" to='").append(jid).append("'");
		xmpp.append(">");
		xmpp.append("<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'>");
		xmpp.append("<jid>").append(jid).append("</jid>");
		xmpp.append("</bind>");
		xmpp.append("</iq>");
		handler.getXmlWriter().write(xmpp.toString());
		
		currentTag = -1;
	}

	@Override
	protected void handleEvent() throws XmlPullParserException, IOException {
		super.handleEvent();
		
		if (type == XmlPullParser.START_TAG) {
			String tag = getXmlPullParser().getName();
			System.out.println("bind found tag " + tag);

			if (tag.equals("resource")) {
				currentTag = 0;
			} else {
				System.out.println("unknown bind tag: " + tag);
				currentTag = -1;
			}
		} else if (type == XmlPullParser.END_TAG) {
			String tag = getXmlPullParser().getName();
			if (tag.equals("resource")) {
				currentTag = -1;
			}
		} else if (currentTag == 0 && type == XmlPullParser.TEXT) {
			resource = getXmlPullParser().getText();
		}
	}
}
