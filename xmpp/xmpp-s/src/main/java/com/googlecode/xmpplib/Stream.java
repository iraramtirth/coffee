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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParserException;

import com.googlecode.xmpplib.provider.AuthenticationController;
import com.googlecode.xmpplib.provider.MessageController;
import com.googlecode.xmpplib.provider.RosterController;
import com.googlecode.xmpplib.stanzas.AuthQuery;
import com.googlecode.xmpplib.stanzas.AuthSasl;
import com.googlecode.xmpplib.stanzas.Bind;
import com.googlecode.xmpplib.stanzas.IQ;
import com.googlecode.xmpplib.stanzas.Message;
import com.googlecode.xmpplib.stanzas.Roster;
import com.googlecode.xmpplib.stanzas.XmlPuller;
import com.googlecode.xmpplib.utils.XmlWriter;

public class Stream {
	Logger logger = Logger.getAnonymousLogger();
	/**
	 * 
	 */
	private long id = 0;
	/**
	 * XmppFactory.
	 */
	private XmppFactory xmppFactory;
	/**
	 * The socket reader.
	 */
	private Reader reader;
	/**
	 * The socket writer.
	 */
	private Writer writer;
	/**
	 * The xml writer for the xmpp stream.
	 */
	private XmlWriter xmlWriter;
	/**
	 * 
	 */
	public XmlPuller processor = new XmlPuller(this);
	/**
	 * The current IQ tag.
	 */
	public IQ iq = new IQ(this);
	/**
	 * The current auth query.
	 */
	public AuthQuery authQuery;
	/**
	 * The current auth sasl.
	 */
	public AuthSasl authSasl;
	/**
	 * The current bind.
	 */
	public Bind bind = new Bind(this);
	/**
	 * The current roster.
	 */
	public Roster roster = null;
	
	public Message message = null;

	public Stream(
			Reader reader, Writer writer) throws IOException,
			XmlPullParserException {
		
		if (reader instanceof BufferedReader) {
			this.reader = reader;
		} else {
			this.reader = new BufferedReader(reader);
		}
		
		if (writer instanceof BufferedWriter) {
			this.writer = writer;
		} else {
			this.writer = new BufferedWriter(writer);
		}
		
//		if(reader.markSupported()){
//			reader.mark(10024 * 100);
//			String xml = "";
//			char[] data = new char[1024];
//			int len = -1;
//			try {
//				while((len=reader.read(data)) != -1){
//					xml += new String(data, 0, len);
//				}
//				logger.info("input stream ==> "+xml);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			reader.reset();
//		}
		
		
		xmppFactory = Xmpp.xmppFactory;
		
		AuthenticationController authenticationController = xmppFactory.createAuthenticationController();
		authQuery = new AuthQuery(this, authenticationController);
		authSasl = new AuthSasl(this, authenticationController);
		authenticationController.addListener(authQuery);
		authenticationController.addListener(authSasl);
		
		RosterController rosterController = xmppFactory.createRosterController();
		roster = new Roster(this, rosterController);
		
		MessageController messageController = xmppFactory.createMessageController();
		message = new Message(this, messageController);
		
	
		xmlWriter = new XmlWriter(this.writer);
	}

	public Stream(
			InputStream inputStream,
			OutputStream outputStream) throws IOException,
			XmlPullParserException {
		this(
				new InputStreamReader(inputStream, "UTF-8"),
				new OutputStreamWriter(outputStream, "UTF-8"));
		
	}
	
	public XmlWriter getXmlWriter() {
		return xmlWriter;
	}

	public Reader getReader(){
		return this.reader;
	}
	
	public long createNextId() {
		return id++;
	}

	/**
	 * Started via {@link Thread#start()} and handles the complete stream. The
	 * thread dies if the stream is finished or an error occurs.
	 */
	public void parse() throws XmlPullParserException, IOException {
		new XmlPuller(this).parse();
	}
}