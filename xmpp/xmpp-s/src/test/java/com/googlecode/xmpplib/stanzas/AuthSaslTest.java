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
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;

import org.apache.commons.codec.binary.Base64;
import org.junit.ComparisonFailure;
import org.junit.Test;

import com.googlecode.xmpplib.AssertionReader;
import com.googlecode.xmpplib.AutoFlushWriter;
import com.googlecode.xmpplib.provider.impl.SimpleAuthenticationController;

public class AuthSaslTest {
	protected AuthSasl createAuthSasl() {
		SimpleAuthenticationController authenticationController = new SimpleAuthenticationController();
		authenticationController.addUser("unittestusername", "unittestpassword");
		
		AuthSasl authSasl = new AuthSasl(null, authenticationController);
		authenticationController.addListener(authSasl);
		return authSasl;
	}

	@Test
	public void testAuthWithoutMechanism() throws Exception {
		AuthSasl authSasl = createAuthSasl();
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(authSasl, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"</auth>");
			assertionReader.assertEquals("<error/>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testAuthWithUnsupportedMechanism() throws Exception {
		AuthSasl authSasl = createAuthSasl();
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(authSasl, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<auth mechanism='UNKOWN' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"</auth>");
			assertionReader.assertEquals("<error/>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testAnonymous() throws Exception {
		AuthSasl authSasl = createAuthSasl();
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(authSasl, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<auth mechanism='ANONYMOUS' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"</auth>");
			assertionReader.assertRegex(
					"<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'></success>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testPlain() throws Exception {
		AuthSasl authSasl = createAuthSasl();
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(authSasl, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			String response = new String(Base64.encodeBase64("unittestusername\0unittestusername\0unittestpassword".getBytes()));
			
			autoFlushWriter.write(
					"<auth mechanism='PLAIN' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					response +
					"</auth>");
			assertionReader.assertRegex(
					"<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'></success>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testCramMd5() throws Exception {
		AuthSasl authSasl = createAuthSasl();
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(authSasl, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<auth mechanism='CRAM-MD5' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"</auth>");
			assertionReader.assertRegex(
					"<challenge xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"[A-Za-z0-9/+=]+" +
					"</challenge>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testDigestMd5() throws Exception {
		AuthSasl authSasl = createAuthSasl();
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(authSasl, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<auth mechanism='DIGEST-MD5' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"</auth>");
			String challange = new String(Base64.decodeBase64(assertionReader.assertRegex(
					"<challenge xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>(" +
					"[A-Za-z0-9/+=]+" +
					")</challenge>").group(1).getBytes()));
			if (!challange.matches("charset=utf-8,realm=\"buenocode.com\",nonce=\"[A-Za-z0-9/+=]+\",qop=\"auth\",algorithm=md5-sess")) {
				throw new ComparisonFailure("Regex does not match: ", "charset=utf-8,realm=\"buenocode.com\",nonce=\"[A-Za-z0-9/+=]+\",qop=\"auth\",algorithm=md5-sess", challange);
			}
			
			String[] mechanisms = { "DIGEST-MD5" };
			Map<String,String> props = new HashMap<String,String>();
			SaslClient saslClient = Sasl.createSaslClient(mechanisms, "unittestusername", "xmpp", "buenocode.com", props, new CallbackHandler() {
				public void handle(Callback[] callbacks) throws IOException,
						UnsupportedCallbackException {
					for (Callback callback : callbacks) {
						if (callback instanceof NameCallback) {
							NameCallback ncb = (NameCallback)callback;
							ncb.setName("unittestusername");
						} else if (callback instanceof PasswordCallback) {
							PasswordCallback pcb = (PasswordCallback)callback;
							pcb.setPassword("unittestpassword".toCharArray());
						} else if (callback instanceof RealmCallback) {
							RealmCallback rcb = (RealmCallback)callback;
							rcb.setText("buenocode.com");
						} else {
							throw new UnsupportedCallbackException(callback);
						}
					}
				}
			});
			String response = new String(Base64.encodeBase64((saslClient.evaluateChallenge(challange.getBytes()))));
			
			autoFlushWriter.write(
					"<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					response +
					"</response>");
			assertionReader.assertRegex(
					"<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
//					"rspauth=[A-Za-z0-9/+=]+" +
					"</success>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testDigestMd5WithUnknownUser() throws Exception {
		AuthSasl authSasl = createAuthSasl();
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(authSasl, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<auth mechanism='DIGEST-MD5' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"</auth>");
			String challange = new String(Base64.decodeBase64(assertionReader.assertRegex(
					"<challenge xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>(" +
					"[A-Za-z0-9/+=]+" +
					")</challenge>").group(1).getBytes()));
			if (!challange.matches("charset=utf-8,realm=\"buenocode.com\",nonce=\"[A-Za-z0-9/+=]+\",qop=\"auth\",algorithm=md5-sess")) {
				throw new ComparisonFailure("Regex does not match: ", "charset=utf-8,realm=\"buenocode.com\",nonce=\"[A-Za-z0-9/+=]+\",qop=\"auth\",algorithm=md5-sess", challange);
			}
			
			String[] mechanisms = { "DIGEST-MD5" };
			Map<String,String> props = new HashMap<String,String>();
			SaslClient saslClient = Sasl.createSaslClient(mechanisms, "wronguser", "xmpp", "buenocode.com", props, new CallbackHandler() {
				public void handle(Callback[] callbacks) throws IOException,
						UnsupportedCallbackException {
					for (Callback callback : callbacks) {
						if (callback instanceof NameCallback) {
							NameCallback ncb = (NameCallback)callback;
							ncb.setName("wronguser");
						} else if (callback instanceof PasswordCallback) {
							PasswordCallback pcb = (PasswordCallback)callback;
							pcb.setPassword("unittestpassword".toCharArray());
						} else if (callback instanceof RealmCallback) {
							RealmCallback rcb = (RealmCallback)callback;
							rcb.setText("buenocode.com");
						} else {
							throw new UnsupportedCallbackException(callback);
						}
					}
				}
			});
			String response = new String(Base64.encodeBase64((saslClient.evaluateChallenge(challange.getBytes()))));
			
			autoFlushWriter.write(
					"<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					response +
					"</response>");
			assertionReader.assertRegex(
					"<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"<not-authorized/>" +
					"</failure>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testDigestMd5WithIncorrectPassword() throws Exception {
		AuthSasl authSasl = createAuthSasl();
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(authSasl, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<auth mechanism='DIGEST-MD5' xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"</auth>");
			String challange = new String(Base64.decodeBase64(assertionReader.assertRegex(
					"<challenge xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>(" +
					"[A-Za-z0-9/+=]+" +
					")</challenge>").group(1).getBytes()));
			if (!challange.matches("charset=utf-8,realm=\"buenocode.com\",nonce=\"[A-Za-z0-9/+=]+\",qop=\"auth\",algorithm=md5-sess")) {
				throw new ComparisonFailure("Regex does not match: ", "charset=utf-8,realm=\"buenocode.com\",nonce=\"[A-Za-z0-9/+=]+\",qop=\"auth\",algorithm=md5-sess", challange);
			}
			
			String[] mechanisms = { "DIGEST-MD5" };
			Map<String,String> props = new HashMap<String,String>();
			SaslClient saslClient = Sasl.createSaslClient(mechanisms, "unittestusername", "xmpp", "buenocode.com", props, new CallbackHandler() {
				public void handle(Callback[] callbacks) throws IOException,
						UnsupportedCallbackException {
					for (Callback callback : callbacks) {
						if (callback instanceof NameCallback) {
							NameCallback ncb = (NameCallback)callback;
							ncb.setName("unittestusername");
						} else if (callback instanceof PasswordCallback) {
							PasswordCallback pcb = (PasswordCallback)callback;
							pcb.setPassword("wrongpassword".toCharArray());
						} else if (callback instanceof RealmCallback) {
							RealmCallback rcb = (RealmCallback)callback;
							rcb.setText("buenocode.com");
						} else {
							throw new UnsupportedCallbackException(callback);
						}
					}
				}
			});
			String response = new String(Base64.encodeBase64((saslClient.evaluateChallenge(challange.getBytes()))));
			
			autoFlushWriter.write(
					"<response xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					response +
					"</response>");
			assertionReader.assertRegex(
					"<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"<not-authorized/>" +
					"</failure>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}
}
