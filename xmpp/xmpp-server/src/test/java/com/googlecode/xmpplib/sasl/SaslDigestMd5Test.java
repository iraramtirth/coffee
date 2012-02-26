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
import javax.security.sasl.RealmCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import com.googlecode.xmpplib.utils.Digest;

public class SaslDigestMd5Test {
	@Test
	public void testAuthRequestPattern() {
		Pattern pattern = Pattern.compile("charset=utf-8,realm=\"buenocode.com\",nonce=\"[A-Za-z0-9/+=]+\",qop=\"auth\",algorithm=md5-sess");
		
		for (int i = 0; i < 100; i++) {
			SaslDigestMd5 saslDigestMd5 = new SaslDigestMd5("buenocode.com");
			saslDigestMd5.initServer();
			String challange = saslDigestMd5.getChallange();
			Assert.assertTrue("Unexpected auth request: " + challange, pattern.matcher(challange).matches());
		}
	}

	@Test
	public void testValidateClientResponse() {
		SaslDigestMd5 saslDigestMd5 = new SaslDigestMd5("testrealm@host.com");
		saslDigestMd5.setUsername("Mufasa");
		saslDigestMd5.setPassword("Circle Of Life");
		saslDigestMd5.setServerNonce(Digest.parseHex("dcd98b7102dd2f0e8b11d0f600bfb0c093"));
		
		Assert.assertEquals("charset=utf-8,realm=\"testrealm@host.com\",nonce=\"3NmLcQLdLw6LEdD2AL+wwJM=\",qop=\"auth\",algorithm=md5-sess", saslDigestMd5.getChallange());
		
		saslDigestMd5.validateClientResponse("username=\"Mufasa\","
				+ "realm=\"testrealm@host.com\","
				+ "nonce=\"dcd98b7102dd2f0e8b11d0f600bfb0c093\","
				+ "uri=\"/dir/index.html\",qop=auth,nc=00000001,"
				+ "cnonce=\"0a4f113b\","
				+ "response=\"6629fae49393a05397450978507c4ef1\","
				+ "opaque=\"5ccc069c403ebaf9f0171e9517f40e41\"");
	}

	@Test
	public void testWithJavaxSecuritySaslServer() throws SaslException {
		for (int i = 0; i < 100; i++) {
			String mechanism = "DIGEST-MD5";
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
			
			SaslDigestMd5 saslDigestMd5 = new SaslDigestMd5("buenocode.com");
			saslDigestMd5.setUsername("unittestusername");
			saslDigestMd5.setPassword("unittestpassword");
			saslDigestMd5.setServerNonce(Base64.decodeBase64(SaslDigestMd5.getPart(response, "nonce").getBytes()));
			
//			Assert.assertEquals(response, saslDigestMd5.getChallange());
		}
	}

	@Test
	public void testWithJavaxSecuritySaslClient() throws SaslException {
		for (int i = 0; i < 100; i++) {
			SaslDigestMd5 saslDigestMd5 = new SaslDigestMd5("buenocode.com");
			saslDigestMd5.setUsername("unittestusername");
			saslDigestMd5.setPassword("unittestpassword");
			saslDigestMd5.initServer();
			
			String challange = saslDigestMd5.getChallange();
			
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
			String response = new String(saslClient.evaluateChallenge(challange.getBytes()));
			
			Assert.assertEquals("unittestusername", SaslDigestMd5.getUserFromClientResponse(response));
			
			Assert.assertTrue(saslDigestMd5.validateClientResponse(response));
		}
	}

	@Test
	public void testParseChallange() {
		String challangeBase64 =
			"cmVhbG09InNvbWVyZWFsbSIsbm9uY2U9Ik9BNk1HOXRFUUdtMmhoIixxb3A9ImF1dGgi" +
			"LGNoYXJzZXQ9dXRmLTgsYWxnb3JpdGhtPW1kNS1zZXNzCg==";
		
		String callange = new String(Base64.decodeBase64(challangeBase64.getBytes()));
		
		Assert.assertEquals("realm=\"somerealm\",nonce=\"OA6MG9tEQGm2hh\",qop=\"auth\",charset=utf-8,algorithm=md5-sess", callange.trim());
		Assert.assertEquals("somerealm", SaslDigestMd5.getPart(callange, "realm"));
		Assert.assertEquals("OA6MG9tEQGm2hh", SaslDigestMd5.getPart(callange, "nonce"));
		Assert.assertEquals("auth", SaslDigestMd5.getPart(callange, "qop"));
		Assert.assertEquals("utf-8", SaslDigestMd5.getPart(callange, "charset"));
		Assert.assertEquals("md5-sess", SaslDigestMd5.getPart(callange, "algorithm"));
	}
}
