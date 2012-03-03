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
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.xmlpull.v1.XmlPullParserException;

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
public class XmppServer implements Runnable{
	private Logger logger = Logger.getLogger(XmppServer.class.getName());
	
	private boolean tlsSupported = false;
	private boolean tlsRequired = false;
	private boolean saslSupported = true;
	private boolean saslRequired = true;

	private boolean saslDigestMd5 = true;
	private boolean saslCramMd5 = true;
	private boolean saslPlain = false;
	private boolean saslAnonymous = false;

	
	private boolean stillRunning = true;
	private ServerSocket serverSocket;
	
	/**
	 * Default (null) is the loopback address.
	 */
	private String bindIP = null;
	/**
	 * Default is 5222.
	 */
	private Integer bindPort = 5222;

	private Thread socketListenerThread;


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


	public void start() throws IOException {
		if (isRunning()) {
			throw new IllegalStateException("XmppServer is already running.");
		}
		int port = getBindPort() != null ? getBindPort() : 5222;
		int backlog = 20;
		InetAddress bindAddr = InetAddress.getByName(bindIP);
		//创建Server端口
		serverSocket = new ServerSocket(port, backlog, bindAddr);
		socketListenerThread = new Thread(this);
		socketListenerThread.start();
		
		logger.info("server startup....");
	}

	private ThreadPoolExecutor poolExecutor;
	
	public XmppServer(){
		poolExecutor = new ThreadPoolExecutor(2, 10, 10, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(3));
	}
	
	public void run() {
		while (stillRunning) {
			try {
				final Socket socket = serverSocket.accept();
				final Stream stream = new Stream(
						socket.getInputStream(),
						socket.getOutputStream());
				poolExecutor.execute(new Runnable() {
					public void run() {
						try {
							stream.parse();
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
				});
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
	
	public boolean isRunning() {
		return socketListenerThread != null && socketListenerThread.isAlive();
	}
}