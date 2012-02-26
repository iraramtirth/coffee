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

/**
 * Notice: This class is not-threadsafe.
 * 
 * @author Christoph Jerolimov
 */
public class PacketProcessor {
	protected StreamProcessor handler;
	protected int type = -1;

	public PacketProcessor(StreamProcessor handler) {
		this.handler = handler;
	}

	public void parse() throws XmlPullParserException,
			IOException {
		this.type = -1;
		parseTag();
	}

	public XmlPullParser getXmlPullParser() {
		return handler.getXmlPullParser();
	}

	/**
	 * Parse a whole xml tag. The current event must already the start tag. The
	 * method will be leaved with the end tag event. For the current event we
	 * call {@link #handleStartTag()}, for the last {@link #handleEndTag()},
	 * and for all other events {@link #handleEvent()}.
	 */
	protected void parseTag() throws XmlPullParserException, IOException {

		if (getXmlPullParser().getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException("Unexpected event type "
					+ getXmlPullParser().getEventType());
		}
		handleStartTag();
//		System.out.println(toString() + " - s: " + getXmlPullParser().getDepth() + " / " + count);
		
		final int startDeath = getXmlPullParser().getDepth();
		
		while (true) {
			type = getXmlPullParser().nextToken();
			if (type == XmlPullParser.START_TAG) {
//				count++;
//				System.out.println(toString() + " - ++ " + getXmlPullParser().getDepth() + " / " + count);
			} else if (type == XmlPullParser.END_TAG) {
//				count--;
//				System.out.println(toString() + " - -- " + getXmlPullParser().getDepth() + " / " + count);
				if (startDeath == getXmlPullParser().getDepth()) {
//					System.out.println(toString() + " - e: " + getXmlPullParser().getDepth() + " / " + count);
					handleEndTag();
					return;
				}
			} else if (type == XmlPullParser.START_DOCUMENT) {
				System.out.println("start document !!!");
			} else if (type == XmlPullParser.END_DOCUMENT) {
				System.out.println("found end document!!!");
				return;
			}
			handleEvent();
		}
	}

	/**
	 * Called when we start the current top tag.
	 */
	protected void handleStartTag() throws XmlPullParserException, IOException {
		System.out.print("<" + getXmlPullParser().getName());
		for (int i = 0; i < getXmlPullParser().getAttributeCount(); i++) {
			System.out.print(" " + getXmlPullParser().getAttributeName(i) + "='" + getXmlPullParser().getAttributeValue(i) + "'");
		}
		System.out.println(">");
	}

	/**
	 * Called when we finished the current top tag.
	 */
	protected void handleEndTag() throws XmlPullParserException, IOException {
		String tag = getXmlPullParser().getName();
		System.out.println("</" + tag + ">");
	}

	/**
	 * Handles each sub event.
	 */
	protected void handleEvent() throws XmlPullParserException, IOException {
		if (type == XmlPullParser.START_TAG) {
			String tag = getXmlPullParser().getName();
			System.out.print("2<" + tag);
			for (int i = 0; i < getXmlPullParser().getAttributeCount(); i++) {
				System.out.print(" " + getXmlPullParser().getAttributeName(i) + "=" + getXmlPullParser().getAttributeValue(i));
			}
			System.out.println(">");
		} else if (type == XmlPullParser.END_TAG) {
			String tag = getXmlPullParser().getName();
			System.out.println("</" + tag + ">");
		} else {
			System.out.println(getXmlPullParser().getText());
		}
	}
}
