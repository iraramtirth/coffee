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

import org.apache.commons.codec.binary.Base64;

import com.googlecode.xmpplib.utils.Digest;

public class SaslDigestMd5 {
	private String charset = "utf-8";
	private String realm;
	private String username;
	private String password;
	private byte[] serverNonce;

	public SaslDigestMd5(String realm) {
		this.realm = realm;
	}

	public SaslDigestMd5(String realm, boolean initServer) {
		this.realm = realm;
		if (initServer) {
			initServer();
		}
	}

	public void initServer() {
		// TODO if we set the nonce parameter on 32 we could not validate our
		// implementation with the #testWithJavaxSecuritySaslClient Test! Why?
		// It's this a bug in the jvm md5 code or in the apache commons base64?
		// I do not understand this... But with 30 bytes it's work... ;-)
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
		StringBuffer buffer = new StringBuffer();
		buffer.append("charset=").append(charset);
		buffer.append(",realm=\"").append(realm).append("\"");
		buffer.append(",nonce=\"").append(new String(Base64.encodeBase64(serverNonce))).append("\"");
		buffer.append(",qop=\"auth\",algorithm=md5-sess");
		return buffer.toString();
	}

	public static String getUserFromClientResponse(String response) {
		return getPart(response, "username");
	}

	public boolean validateClientResponse(String response) {
		String algorithm = "md5-sess";
		String clientNonceCount = getPart(response, "nc");
		String clientNonce = getPart(response, "cnonce");
		// TODO assert that qop is auth.
		String expectedResponse = getClientResponse(
				algorithm,
				clientNonceCount,
				clientNonce,
				"xmpp/" + realm);
		
		if (!realm.equals(getPart(response, "realm"))) {
			throw new SecurityException("Illegal realm \"" + getPart(response, "realm") + "\" for current challange.");
		}
		if (!username.equals(getPart(response, "username"))) {
			throw new SecurityException("Illegal username \"" + getPart(response, "username") + "\" for current challange.");
		}
		
		String challangeResponse = getPart(response, "response");
		if (challangeResponse == null) {
			throw new SecurityException("Could not validate challange without cnonce.");
		}
		
		return expectedResponse.equals(challangeResponse);
	}

	public String getClientResponse(String algorithm, String clientNonceCount, String clientNonce, String digestUri) {
		if (realm == null) {
			throw new RuntimeException("Could not create challange without realm.");
		}
		if (username == null) {
			throw new RuntimeException("Could not create challange without username.");
		}
		if (password == null) {
			throw new RuntimeException("Could not create challange without password.");
		}
		if (serverNonce == null) {
			throw new RuntimeException("Could not create challange without serverNonce.");
		}
		
		byte[] ha1 = Digest.MD5(username, ":", realm, ":", password);
		
		if (algorithm != null && algorithm.equals("md5-sess")) {
			ha1 = Digest.MD5(ha1, ":".getBytes(), Base64.encodeBase64(serverNonce), ":".getBytes(), clientNonce.getBytes(), ":".getBytes(), username.getBytes());
		}
		
		byte[] ha2 = Digest.MD5("AUTHENTICATE", ":", digestUri);
		
		if (clientNonceCount == null || clientNonce == null) {
			return Digest.toHex(Digest.MD5(
					Digest.toHex(ha1),
					":",
					new String(Base64.encodeBase64(serverNonce)),
					":",
					Digest.toHex(ha2)));
		} else {
			return Digest.toHex(Digest.MD5(
					Digest.toHex(ha1),
					":",
					new String(Base64.encodeBase64(serverNonce)),
					":",
					clientNonceCount,
					":",
					clientNonce,
					":",
					"auth",
					":",
					Digest.toHex(ha2)));
		}
	}

	public String getServerDigestResponse(String response) {
		return "rspauth=" + getServerResponse(response);
	}

	protected String getServerResponse(String response) {
		String clientNonceCount = getPart(response, "nc");
		String clientNonce = getPart(response, "cnonce");
		// TODO assert that qop is auth.
		return getServerResponse(
				clientNonceCount,
				clientNonce,
				"xmpp/" + realm);
	}

	protected String getServerResponse(String clientNonceCount, String clientNonce, String digestUri) {
		if (realm == null) {
			throw new RuntimeException("Could not create challange without realm.");
		}
		if (username == null) {
			throw new RuntimeException("Could not create challange without username.");
		}
		if (password == null) {
			throw new RuntimeException("Could not create challange without password.");
		}
		if (serverNonce == null) {
			throw new RuntimeException("Could not create challange without serverNonce.");
		}
		
		byte[] ha1 = Digest.MD5(username, ":", realm, ":", password);
		byte[] ha2 = Digest.MD5(":", digestUri);
		
		if (clientNonceCount == null || clientNonce == null) {
			return Digest.toHex(Digest.MD5(
					ha1,
					":".getBytes(),
					serverNonce,
					":".getBytes(),
					ha2));
		} else {
			return Digest.toHex(Digest.MD5(
					Digest.toHex(ha1),
					":",
					Digest.toHex(serverNonce),
					":",
					clientNonceCount,
					":",
					clientNonce,
					":",
					"auth",
					":",
					Digest.toHex(ha2)));
		}
	}

	public static String getPart(String input, String key) {
		if (!input.contains(key)) {
			return null;
		}
		return getPart(input.trim(), key + "=", 0);		
	}

	private static String getPart(String input, String key, int offset) {
		if (input.startsWith(key, offset)) {
			offset += key.length();
			if (input.charAt(offset) == '"') {
				for (int i = offset + 1; i < input.length(); i++) {
					if (input.charAt(i) == '"') {
						return input.substring(offset + 1, i);
					}
				}
				return input.substring(offset);
			} else if (input.charAt(offset) == '\'') {
				for (int i = offset + 1; i < input.length(); i++) {
					if (input.charAt(i) == '\'') {
						return input.substring(offset + 1, i);
					}
				}
				return input.substring(offset);
			} else {
				for (int i = offset; i < input.length(); i++) {
					if (input.charAt(i) == ',') {
						return input.substring(offset, i);
					}
				}
				return input.substring(offset);
			}
		} else {
			for (int i = offset; i < input.length(); i++) {
				if (input.charAt(i) == ',') {
					return getPart(input, key, offset + 1);
				}
			}
			return null;
		}
	}
}
