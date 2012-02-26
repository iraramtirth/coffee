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
package com.googlecode.xmpplib.sasl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.junit.Assert;
import org.junit.Test;

public class SaslCramMd5Test {
	@Test
	public void testAuthRequestPattern() {
		Pattern pattern = Pattern.compile("<[0-9a-f]+@buenocode.com>");
		
		for (int i = 0; i < 100; i++) {
			SaslCramMd5 saslCramMd5 = new SaslCramMd5("buenocode.com");
			saslCramMd5.initServer();
			String challange = saslCramMd5.getChallange();
			Assert.assertTrue("Unexpected auth request: " + challange, pattern.matcher(challange).matches());
		}
	}

	@Test
	public void testWithJavaxSecuritySaslServer() throws SaslException {
		for (int i = 0; i < 100; i++) {
			String mechanism = "CRAM-MD5";
			Map<String,String> props = new HashMap<String,String>();
			SaslServer saslServer = Sasl.createSaslServer(mechanism, "xmpp", "buenocode.com", props, new CallbackHandler() {
				public void handle(Callback[] callbacks) throws IOException,
						UnsupportedCallbackException {
					for (Callback callback : callbacks) {
						throw new UnsupportedCallbackException(callback);
					}
				}
			});
			String response = new String(saslServer.evaluateResponse(new byte[0]));
			
//			System.out.println(response);
			Assert.assertNotNull(response);
		}
	}

	@Test
	public void testWithJavaxSecuritySaslClient() throws SaslException {
		for (int i = 0; i < 100; i++) {
			SaslCramMd5 saslCramMd5 = new SaslCramMd5("buenocode.com");
			saslCramMd5.setUsername("unittestusername");
			saslCramMd5.setPassword("unittestpassword");
			saslCramMd5.initServer();
			
			String challange = saslCramMd5.getChallange();
			
			String[] mechanisms = { "CRAM-MD5" };
			Map<String,String> props = new HashMap<String,String>();
			SaslClient saslClient = Sasl.createSaslClient(mechanisms, "unittestusername", "", "", props, new CallbackHandler() {
				public void handle(Callback[] callbacks) throws IOException,
						UnsupportedCallbackException {
					for (Callback callback : callbacks) {
						if (callback instanceof NameCallback) {
							NameCallback ncb = (NameCallback)callback;
							ncb.setName("unittestusername");
						} else if (callback instanceof PasswordCallback) {
							PasswordCallback pcb = (PasswordCallback)callback;
							pcb.setPassword("unittestpassword".toCharArray());
						} else {
						   throw new UnsupportedCallbackException(callback);
						}
					}
				}
			});
			
			String response = new String(saslClient.evaluateChallenge(challange.getBytes()));
			
			Assert.assertEquals("unittestusername", SaslCramMd5.getUserFromClientResponse(response));
			
			Assert.assertTrue(saslCramMd5.validateClientResponse(response));
		}
	}
}
