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
import java.util.Collection;

import org.xmlpull.v1.XmlPullParserException;

import com.googlecode.xmpplib.StreamProcessor;
import com.googlecode.xmpplib.provider.RosterController;
import com.googlecode.xmpplib.provider.RosterListener;

public class Roster extends PacketProcessor implements RosterListener {
	private int currentTag = -1;

	private RosterController rosterController;

	public Roster(StreamProcessor handler, RosterController rosterController) {
		super(handler);
		this.rosterController = rosterController;
	}

	@Override
	protected void handleStartTag() throws XmlPullParserException, IOException {
		super.handleStartTag();
		
		String tag = getXmlPullParser().getName();
		String ns = getXmlPullParser().getAttributeValue(null, "xmlns");
		
		if (tag.equals("query") && ns != null && ns.equals("jabber:iq:roster")) {
			currentTag = 0;
		} else if (tag.equals("presence") && (ns == null || (ns != null && ns.equals("jabber:client")))) {
			currentTag = 1;
		} else {
			System.err.println("unknown tag in roster: " + tag + " ns: " + ns);
		}
	}

	@Override
	protected void handleEndTag() throws XmlPullParserException, IOException {
		super.handleEndTag();
		
		if (currentTag == 0) {
			System.out.println("requestCompleteUpdate");
			rosterController.requestCompleteUpdate(handler.iq.getId());
		} else if (currentTag == 1) {
			
		}
		currentTag = -1;
	}

	@Override
	protected void handleEvent() throws XmlPullParserException, IOException {
		super.handleEvent();
	}

	public void completeUpdate(Serializable xmppData,
			Collection<Contact> contacts, Collection<Integer> status) {
		String jid = "christoph@127.0.0.1";
		System.out.println("roster get an update for " + jid);
		
		StringBuffer xmpp = new StringBuffer();
		xmpp.append("<iq xmlns='jabber:client' type='result' id='").append(xmppData).append("'");
		xmpp.append(" from='127.0.0.1'");
		xmpp.append(" to='").append(jid).append("'");
		xmpp.append(">");
		xmpp.append("<query xmlns='jabber:iq:roster'>\n");
		
		for (Contact contact : contacts) {
			String id = contact.getNumber() + "@127.0.0.1";
			
			xmpp.append("<item jid='").append(id).append("'");
			xmpp.append(" name='").append(contact.getName()).append("'");
			xmpp.append(" subscription='both'>");
			xmpp.append("<group>Android</group>");
			xmpp.append("</item>\n");
		}
		
		xmpp.append("</query>");
		xmpp.append("</iq>");
		
		try {
			handler.getXmlWriter().write(xmpp.toString());
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
		
		for (Contact contact : contacts) {
			receiveStatusUpdate(null, contact, null);
		}
	}

	public void receiveStatusUpdate(Serializable xmppData, Contact contact,
			Integer status) {
		String jid = "christoph@127.0.0.1";
		System.out.println("receive status update for " + jid);
		
		String id = contact.getNumber() + "@127.0.0.1";
		
		StringBuffer xmpp = new StringBuffer();
		xmpp.append("<presence xmlns='jabber:client' type='result' id='").append(System.currentTimeMillis()).append("'");
		xmpp.append(" from='").append(id).append("'");
		xmpp.append(" to='").append(jid).append("'");
		xmpp.append(">");
		xmpp.append("<show>away</show>");
		xmpp.append("<status>be right back</status>");
		xmpp.append("<priority>0</priority>");
		xmpp.append("</presence>");
		
		try {
			handler.getXmlWriter().write(xmpp.toString());
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}
}
