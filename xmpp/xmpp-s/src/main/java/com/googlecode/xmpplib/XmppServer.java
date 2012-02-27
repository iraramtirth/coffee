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
import java.net.ServerSocket;
import java.util.logging.Logger;

/**
 * <p>XmppServer is the main configuration class for the xmpplib server.</p>
 * 
 * <p>If you extend this class please note that you can completly ignore the
 * private attributes {@link #socketListener} and {@link #socketListenerThread}
 * if this three methods:</p>
 * 
 * <p><ul>
 * <li>{@link #start()}</li>
 * <li>{@link #shutdown()}</li>
 * <li>{@link #isRunning()}</li>
 * </ul></p>
 * 
 * <p>You should reuse the methods {@link #createServerSocket()} and
 * {@link #createSocketListener()} (which impl. call createServerSocket).</p>
 * 
 * @author Christoph Jerolimov
 */
public class XmppServer {
	private Logger logger = Logger.getLogger(XmppServer.class.getName());
	
	private XmppFactory xmppFactory;

	private boolean tlsSupported = false;
	private boolean tlsRequired = false;
	private boolean saslSupported = true;
	private boolean saslRequired = true;

	private boolean saslDigestMd5 = true;
	private boolean saslCramMd5 = true;
	private boolean saslPlain = false;
	private boolean saslAnonymous = false;

	/**
	 * Default (null) is the loopback address.
	 */
	private String bindIP = null;
	/**
	 * Default is 5222.
	 */
	private Integer bindPort = 5222;

	private SocketListener socketListener;
	private Thread socketListenerThread;

	public XmppServer(XmppFactory xmppFactory) {
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

	public boolean isSaslDigestMd5() {
		return saslDigestMd5;
	}

	public void setSaslDigestMd5(boolean saslDigestMd5) {
		this.saslDigestMd5 = saslDigestMd5;
	}

	public boolean isSaslCramMd5() {
		return saslCramMd5;
	}

	public void setSaslCramMd5(boolean saslCramMd5) {
		this.saslCramMd5 = saslCramMd5;
	}

	public boolean isSaslPlain() {
		return saslPlain;
	}

	public void setSaslPlain(boolean saslPlain) {
		this.saslPlain = saslPlain;
	}

	public boolean isSaslAnonymous() {
		return saslAnonymous;
	}

	public void setSaslAnonymous(boolean saslAnonymous) {
		this.saslAnonymous = saslAnonymous;
	}

	public String getBindIP() {
		return bindIP;
	}

	public void setBindIP(String bindIP) {
		this.bindIP = bindIP;
	}

	public Integer getBindPort() {
		return bindPort;
	}

	public void setBindPort(Integer bindPort) {
		this.bindPort = bindPort;
	}

	protected ServerSocket createServerSocket() throws IOException {
		int port = getBindPort() != null ? getBindPort() : 5222;
		int backlog = 20;
		InetAddress bindAddr = InetAddress.getByName(bindIP);
		return new ServerSocket(port, backlog, bindAddr);
	}

	public void start() throws IOException {
		if (isRunning()) {
			throw new IllegalStateException("XmppServer is already running.");
		}
		ServerSocket serverSocket = createServerSocket();
		socketListener = new SocketListener(this, xmppFactory, serverSocket);
		socketListenerThread = new Thread(socketListener);
		socketListenerThread.start();
		logger.info("server startup....");
	}

	public void shutdown() {
		if (socketListener != null) {
			socketListener.shutdown();
		}
	}

	public boolean isRunning() {
		return socketListenerThread != null && socketListenerThread.isAlive();
	}
}
