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

import com.googlecode.xmpplib.StreamProcessorTestThread;

public class PacketProcessorTestThread extends StreamProcessorTestThread {
	protected final PacketProcessor packetProcessor;
	protected final boolean skipUnitStartTag;

	protected boolean again;

	public PacketProcessorTestThread(PacketProcessor packetProcessor) throws IOException, XmlPullParserException {
		this(packetProcessor, false);
	}

	public PacketProcessorTestThread(PacketProcessor packetProcessor, boolean skipUnitStartTag) throws IOException, XmlPullParserException {
		this.packetProcessor = packetProcessor;
		this.packetProcessor.handler = streamProcessor;
		this.skipUnitStartTag = skipUnitStartTag;
	}

	public void start() {
		again = true;
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					XmlPullParser xmlPullParser = packetProcessor.getXmlPullParser();
					while (again) {
						try {
							while (xmlPullParser.nextTag() != XmlPullParser.START_TAG) {
								System.out.println("stream processor#parse ignore " + xmlPullParser.getEventType());
							}
							packetProcessor.parse();
						} catch (RenewStreamException e) {
							System.out.println("renew stream!");
							
							xmlPullParser = xmppFactory.resetXmlPullParser(xmlPullParser, PacketProcessorTestThread.this.serverInput);
						}
					}
				} catch (Exception e) {
					inThreadException = e;
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
	}

	public void shutdown() {
		System.out.println("disconnect client connection");
		again = false;
		try {
			getServerOutput().close();
		} catch (IOException e) {
			// Nothing todo here
		}
	}
}
