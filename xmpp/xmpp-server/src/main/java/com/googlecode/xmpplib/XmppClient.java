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
import java.net.InetAddress;
import java.net.Socket;

public class XmppClient {
	private XmppFactory xmppFactory;

	private boolean tlsSupported = false;
	private boolean tlsRequired = false;
	private boolean saslSupported = true;
	private boolean saslRequired = true;

	private String serverIP = null;
	private Integer serverPort = null;

	private boolean autoReconnect = true;
	private boolean autoReconnectOnIOException = true;
	private int timeBeforeReconnect;

	public XmppClient(XmppFactory xmppFactory) {
		this.xmppFactory = xmppFactory;
	}

	public XmppFactory getXmppFactory() {
		return xmppFactory;
	}

	public void setXmppFactory(XmppFactory xmppFactory) {
		this.xmppFactory = xmppFactory;
	}

	public boolean isTlsSupported() {
		return tlsSupported;
	}

	public void setTlsSupported(boolean tlsSupported) {
		this.tlsSupported = tlsSupported;
	}

	public boolean isTlsRequired() {
		return tlsRequired;
	}

	public void setTlsRequired(boolean tlsRequired) {
		this.tlsRequired = tlsRequired;
	}

	public boolean isSaslSupported() {
		return saslSupported;
	}

	public void setSaslSupported(boolean saslSupported) {
		this.saslSupported = saslSupported;
	}

	public boolean isSaslRequired() {
		return saslRequired;
	}

	public void setSaslRequired(boolean saslRequired) {
		this.saslRequired = saslRequired;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public Integer getServerPort() {
		return serverPort;
	}

	public void setServerPort(Integer serverPort) {
		this.serverPort = serverPort;
	}

	public boolean isAutoReconnect() {
		return autoReconnect;
	}

	public void setAutoReconnect(boolean autoReconnect) {
		this.autoReconnect = autoReconnect;
	}

	public boolean isAutoReconnectOnIOException() {
		return autoReconnectOnIOException;
	}

	public void setAutoReconnectOnIOException(boolean autoReconnectOnIOException) {
		this.autoReconnectOnIOException = autoReconnectOnIOException;
	}

	public int getTimeBeforeReconnect() {
		return timeBeforeReconnect;
	}

	public void setTimeBeforeReconnect(int timeBeforeReconnect) {
		this.timeBeforeReconnect = timeBeforeReconnect;
	}

	public void connect() throws IOException {
		InetAddress address = InetAddress.getByName(serverIP);
		int port = getServerPort() != null ? getServerPort() : 5222;
		Socket socket = new Socket(address, port);
		socket.close();
	}

	public void disconnect() {
	}

	public boolean isConnected() {
		return false;
	}
}
