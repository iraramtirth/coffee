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

public class IQ extends XmlPuller {
	private String type;
	private String from;
	private String to;
	private String id;

	private boolean handled;

	public IQ(Stream handler) {
		super(handler);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected void handleStartTag() throws XmlPullParserException, IOException {
		super.handleStartTag();
		type = getXmlPullParser().getAttributeValue(null, "type");
		from = getXmlPullParser().getAttributeValue(null, "from");
		to = getXmlPullParser().getAttributeValue(null, "to");
		id = getXmlPullParser().getAttributeValue(null, "id");
		handled = false;
	}

	@Override
	protected void handleEndTag() throws XmlPullParserException, IOException {
		super.handleEndTag();
		System.out.println("ende...");
		if (!handled) {
			StringBuffer xmpp = new StringBuffer();
			xmpp.append("<iq xmlns='jabber:client' type='result' id='").append(handler.iq.getId()).append("'");
			xmpp.append(" from='127.0.0.1'");
			xmpp.append(" to='").append("christoph@127.0.0.1/nimo-13mbp").append("'");
			xmpp.append("/>");
			handler.getXmlWriter().write(xmpp.toString());
		}
	}
	
	@Override
	protected void handleEvent() throws XmlPullParserException, IOException {
		super.handleEvent();
		
		if (super.type == XmlPullParser.START_TAG) {
			String tag = getXmlPullParser().getName();
			String ns = getXmlPullParser().getAttributeValue(null, "xmlns");
			
			System.out.println("iq find child: " + tag);
			
			if (tag.equals("query") && ns != null && ns.equals("jabber:iq:auth")) {
				handler.authQuery.parse();
				handled = true;
			} else if (tag.equals("bind") && ns != null && ns.equals("urn:ietf:params:xml:ns:xmpp-bind")) {
				handler.bind.parse();
				handled = true;
			} else if (tag.equals("query") && ns != null && ns.equals("jabber:iq:roster")) {
				handler.roster.parse();
				handled = true;
			} else {
				System.err.println("iq ignore unknown tag: " + tag + " / " + ns);
				handler.processor.parse();
			}
		} else {
			System.out.println("iq ignore type " + super.type);
			if (super.type == XmlPullParser.TEXT) {
				System.out.println("  " + getXmlPullParser().getText());
			}
		}
	}
}
