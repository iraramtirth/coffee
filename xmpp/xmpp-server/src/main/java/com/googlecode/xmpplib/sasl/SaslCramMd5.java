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

import java.util.Random;

import com.googlecode.xmpplib.utils.Digest;

public class SaslCramMd5 {
	private String realm;
	private String username;
	private String password;
	private byte[] serverNonce;

	public SaslCramMd5(String realm) {
		this.realm = realm;
	}

	public SaslCramMd5(String realm, boolean initServer) {
		this.realm = realm;
		if (initServer) {
			initServer();
		}
	}

	public void initServer() {
		serverNonce = new byte[30];
		new Random().nextBytes(serverNonce);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setServerNonce(byte[] serverNonce) {
		this.serverNonce = serverNonce;
	}

	public byte[] getServerNonce() {
		return serverNonce;
	}

	public String getChallange() {
		if (realm == null) {
			throw new RuntimeException("Could not create challange without realm.");
		}
		if (serverNonce == null) {
			throw new RuntimeException("Could not create challange without serverNonce.");
		}
		return "<" + Digest.toHex(serverNonce) + "@" + realm + ">";
	}

	public boolean validateClientResponse(String response) {
		return getClientResponse().equals(response);
	}

	public String getClientResponse() {
		byte[] challange = getChallange().getBytes();
		
		if (password == null) {
			throw new RuntimeException("Could not create client response without password.");
		}
		byte[] key = password.getBytes();
		if (key.length > 64) {
			key = Digest.MD5(key);
		}
		
		byte[] opad = new byte[64];
		byte[] ipad = new byte[64];
		
		System.arraycopy(key, 0, opad, 0, key.length);
		System.arraycopy(key, 0, ipad, 0, key.length);
		
		for (int i = 0; i < 64; i++) {
			opad[i] ^= 0x5c;
			ipad[i] ^= 0x36;
		}
		
		byte[] result = Digest.MD5(opad, Digest.MD5(ipad, challange));
		
		return this.username + " " + Digest.toHex(result);
	}

	public static String getUserFromClientResponse(String response) {
		int pos = response.lastIndexOf(" ");
		return response.substring(0, pos);
	}
}
