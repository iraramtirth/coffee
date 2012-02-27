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
package com.googlecode.xmpplib;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Main {
	public static void main(String[] args) {
		try {
			XmppFactory xmppFactory = new XmppFactory() {
				@Override
				public XmlPullParser createXmlPullParser(Reader reader) throws XmlPullParserException {
					XmlPullParser xmlPullParser = new KXmlParser();
					xmlPullParser.setInput(reader);
					return xmlPullParser;
				}
				
				@Override
				public XmlPullParser createXmlPullParser(InputStream inputStream) throws XmlPullParserException {
					XmlPullParser xmlPullParser = new KXmlParser();
					xmlPullParser.setInput(inputStream, "UTF-8");
					return xmlPullParser;
				}
			};
			
			XmppServer xmppServer = new XmppServer(xmppFactory);
			
			xmppServer.setBindIP("127.0.0.1");
			xmppServer.setBindPort(5222);
			
			xmppServer.start();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
