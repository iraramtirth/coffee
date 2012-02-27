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

import com.googlecode.xmpplib.StreamProcessor;
import com.googlecode.xmpplib.XmppServer;

public class Stream extends PacketProcessor {
	private String to;

	public Stream(StreamProcessor handler) {
		super(handler);
	}

	@Override
	protected void handleStartTag() throws XmlPullParserException, IOException {
		super.handleStartTag();
		
		to = getXmlPullParser().getAttributeValue(null, "to");
		handleOpenStream(to);
	}

	@Override
	protected void handleEndTag() throws XmlPullParserException, IOException {
		handleCloseStream();
	}

	@Override
	protected void handleEvent() throws XmlPullParserException, IOException {
		super.handleEvent();
		
		if (type == XmlPullParser.START_TAG) {
			String tag = getXmlPullParser().getName();
			String ns = getXmlPullParser().getAttributeValue(null, "xmlns");
			System.out.println("xmlns=" + ns);
			
			if (ns != null && ns.equals("urn:ietf:params:xml:ns:xmpp-sasl")) {
				System.out.println("found sasl-auth in stream, parse it.");
				
				handler.authSasl.parse();
				
			} else if (tag.equals("iq")) {
				handler.iq.parse();
			} else if (tag.equals("presence") && (ns == null || (ns != null && ns.equals("jabber:client")))) {
				handler.roster.parse();
			} else if (tag.equals("message")) {
				handler.message.parse();
			} else {
				System.err.println("unknown tag in stream: " + tag + " ns: " + ns);
			}
			
		} else {
			System.out.println("stream ignore type " + type);
		}
	}
	
	private void handleOpenStream(String to) throws IOException {
		StringBuffer xmpp = new StringBuffer(512);
		xmpp.append("<?xml version='1.0' encoding='UTF-8'?>");
		// Open the stream.
		xmpp.append("<stream:stream");
		xmpp.append(" xmlns='jabber:client'");
		xmpp.append(" xmlns:stream='http://etherx.jabber.org/streams'");
		// writer.write(" allowMultiple='true'");
		xmpp.append(" to='").append(to).append("'");
		xmpp.append(" from='").append(to).append("'");
		xmpp.append(" id='").append(handler.createNextId()).append("'");
		xmpp.append(" version='1.0'>");
		
		XmppServer xmppServer = handler.getXmppServer();
		
		boolean tls = xmppServer.isTlsSupported();
		boolean sasl = xmppServer.isSaslSupported()
				&& (xmppServer.isSaslDigestMd5()
						|| xmppServer.isSaslCramMd5()
						|| xmppServer.isSaslPlain()
						|| xmppServer.isSaslAnonymous());
		
		if (handler.authSasl.isLoggedIn()) {
			xmpp.append("<stream:features>");
			xmpp.append("<bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'/>");
			xmpp.append("<session xmlns='urn:ietf:params:xml:ns:xmpp-session'/>");
			xmpp.append("</stream:features>");
		}
		
		if (!handler.authSasl.isLoggedIn() && (tls || sasl)) {
			xmpp.append("<stream:features>");
			
			// tls
			if (tls) {
				xmpp.append("<starttls xmlns='urn:ietf:params:xml:ns:xmpp-tls'>");
				xmpp.append("<required/>");
				xmpp.append("</starttls>");
			}
			
			// sasl
			if (sasl) {
				xmpp.append("<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
				if (xmppServer.isSaslDigestMd5()) {
					xmpp.append("<mechanism>DIGEST-MD5</mechanism>");
				}
				if (xmppServer.isSaslCramMd5()) {
					xmpp.append("<mechanism>CRAM-MD5</mechanism>");
				}
				if (xmppServer.isSaslPlain()) {
					xmpp.append("<mechanism>PLAIN</mechanism>");
				}
				if (xmppServer.isSaslAnonymous()) {
					xmpp.append("<mechanism>ANONYMOUS</mechanism>");
				}
				xmpp.append("</mechanisms>");
			}
			
			xmpp.append("</stream:features>");
		}

		handler.getXmlWriter().write(xmpp.toString());
	}

	private void handleCloseStream() throws IOException {
		handler.getXmlWriter().write("</stream:stream>");
	}
}
