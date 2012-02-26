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

import org.apache.harmony.java.io.PipedReader;
import org.apache.harmony.java.io.PipedWriter;
import org.xmlpull.v1.XmlPullParserException;

import com.googlecode.xmpplib.provider.impl.SimpleAuthenticationController;

public class StreamProcessorTestThread {
	protected final PipedWriter clientOutput;
	protected final PipedReader serverInput;
	protected final PipedReader clientInput;
	protected final PipedWriter serverOutput;

	protected final SimpleAuthenticationController authenticationController;
//	protected final SimpleRosterController rosterController;
//	protected final SimpleMessageController messageController;

	protected final XmppFactory xmppFactory;
	protected final XmppServer xmppServer;

	protected final StreamProcessor streamProcessor;

	protected Exception inThreadException = null;

	protected Thread thread;

	public StreamProcessorTestThread() throws IOException, XmlPullParserException {
		clientOutput = new PipedWriter();
		serverInput = new PipedReader(clientOutput);
		clientInput = new PipedReader();
		serverOutput = new PipedWriter(clientInput);
		
		xmppFactory = new DebugXmppFactory();
		xmppServer = new XmppServer(xmppFactory);
		
		authenticationController = (SimpleAuthenticationController) xmppFactory.createAuthenticationController();
		
		streamProcessor = new StreamProcessor(xmppServer, xmppFactory,
				serverInput, serverOutput);
	}

	public void start() {
		thread = new Thread(new Runnable() {
			public void run() {
				try {
					streamProcessor.parse();
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
		try {
			getServerOutput().close();
		} catch (IOException e) {
			// Nothing todo here
		}
	}

	public void throwInThreadException() throws Exception {
		if (inThreadException != null) {
			throw inThreadException;
		}
	}

	public PipedWriter getClientOutput() {
		return clientOutput;
	}

	public PipedReader getServerInput() {
		return serverInput;
	}

	public PipedReader getClientInput() {
		return clientInput;
	}

	public PipedWriter getServerOutput() {
		return serverOutput;
	}

	public SimpleAuthenticationController getAuthenticationController() {
		return authenticationController;
	}

//	public SimpleRosterController getRosterController() {
//		return rosterController;
//	}
//
//	public SimpleMessageController getMessageController() {
//		return messageController;
//	}

	public XmppFactory getXmppFactory() {
		return xmppFactory;
	}

	public XmppServer getXmppServer() {
		return xmppServer;
	}
}
