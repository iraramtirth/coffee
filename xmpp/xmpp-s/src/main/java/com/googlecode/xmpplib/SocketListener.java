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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParserException;

public class SocketListener implements Runnable {
	Logger logger = Logger.getLogger(SocketListener.class.getName());
	
	private boolean stillRunning = true;
	private XmppServer xmppServer;
	private XmppFactory xmppFactory;
	private ServerSocket serverSocket;

	public SocketListener(
			XmppServer xmppServer,
			XmppFactory xmppFactory,
			ServerSocket serverSocket) throws IOException {
		this.xmppServer = xmppServer;
		this.xmppFactory = xmppFactory;
		this.serverSocket = serverSocket;
	}

	
	public void run() {
		while (stillRunning) {
			try {
				final Socket socket = serverSocket.accept();
				final StreamProcessor streamProcessor = new StreamProcessor(
						xmppServer,
						xmppFactory,
						socket.getInputStream(),
						socket.getOutputStream());
				new Thread(new Runnable() {
					public void run() {
						try {
							streamProcessor.parse();
						} catch (XmlPullParserException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						try {
							socket.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		stillRunning = false;
	}
}
