package com.googlecode.xmpplib;

import java.io.InputStream;
import java.io.Reader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class Xmpp {
	public static XmppFactory xmppFactory = new XmppFactory() {
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
	
	public static XmppServer xmppServer = new XmppServer(); 
}
