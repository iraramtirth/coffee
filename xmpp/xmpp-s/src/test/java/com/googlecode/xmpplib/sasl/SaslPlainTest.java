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

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

import org.junit.Assert;
import org.junit.Test;

public class SaslPlainTest {
	@Test
	public void testWithJavaxSecuritySaslClient() throws SaslException {
		SaslPlain saslPlain = new SaslPlain();
		saslPlain.setUsername("unittestusername");
		saslPlain.setPassword("unittestpassword");
		
		String[] mechanisms = { "PLAIN" };
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
		String response = new String(saslClient.evaluateChallenge(new byte[0]));
		
		Assert.assertEquals("unittestusername", SaslPlain.getUserFromClientResponse(response));
		
		Assert.assertTrue(saslPlain.validateClientResponse(response));
	}
}
