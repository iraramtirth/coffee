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
import java.io.Serializable;

import org.apache.commons.codec.binary.Base64;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.googlecode.xmpplib.Stream;
import com.googlecode.xmpplib.provider.AuthenticationController;
import com.googlecode.xmpplib.provider.AuthenticationListener;
import com.googlecode.xmpplib.sasl.SaslAnonymous;
import com.googlecode.xmpplib.sasl.SaslCramMd5;
import com.googlecode.xmpplib.sasl.SaslDigestMd5;
import com.googlecode.xmpplib.sasl.SaslPlain;

/**
 * This class handles the SASL authentication based on RFC 3920. Please read
 * http://xmpp.org/rfcs/rfc3920.html for more information.
 */
public class AuthSasl extends XmlPuller implements AuthenticationListener {
	private AuthenticationController authenticationController;

	private int currentTag = -1;

	private Object saslInstance;
	private String response;

	public AuthSasl(Stream handler, AuthenticationController authenticationController) {
		super(handler);
		this.authenticationController = authenticationController;
	}

	@Override
	protected void handleStartTag() throws XmlPullParserException, IOException {
		String tag = getXmlPullParser().getName();
		
		if (authenticationController == null) {
			currentTag = -1;
		} else if (tag.equals("auth")) {
			String mechanism = getXmlPullParser().getAttributeValue(null, "mechanism");
			if (mechanism == null || mechanism.length() == 0) {
				// Nothing.
			} else {
				mechanism = mechanism.toUpperCase();
				if (mechanism.equals("DIGEST-MD5")) {
					System.out.println("Create new DIGEST-MD5 server instance...");
					saslInstance = new SaslDigestMd5("buenocode.com", true);
					currentTag = 0;
				} else if (mechanism.equals("CRAM-MD5")) {
					System.out.println("Create new CRAM-MD5 server instance...");
					saslInstance = new SaslCramMd5("buenocode.com", true);
					currentTag = 0;
				} else if (mechanism.equals("PLAIN")) {
					System.out.println("Create new PLAIN server instance...");
					saslInstance = new SaslPlain();
					currentTag = 1;
				} else if (mechanism.equals("ANONYMOUS")) {
					System.out.println("Create new ANONYMOUS server instance...");
					saslInstance = new SaslAnonymous();
					currentTag = 1;
				} else {
					System.out.println("Unsupported sasl-mechanism: " + mechanism);
					saslInstance = null;
				}
			}
		} else if (tag.equals("response")) {
			currentTag = 1;
		} else {
			System.out.println("unknown sasl tag: " + tag);
			currentTag = -1;
		}
	}

	@Override
	protected void handleEndTag() throws XmlPullParserException, IOException {
		// handler.handleAuthRequest();
		
		if (currentTag == 0 && saslInstance != null) {
			currentTag = -1;
			
			if (saslInstance instanceof SaslDigestMd5) {
				String challange = ((SaslDigestMd5) saslInstance).getChallange();
				StringBuffer xmpp = new StringBuffer();
				xmpp.append("<challenge xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
				xmpp.append(new String(Base64.encodeBase64(challange.getBytes())));
				xmpp.append("</challenge>");
				handler.getXmlWriter().write(xmpp.toString());
			} else if (saslInstance instanceof SaslCramMd5) {
				String challange = ((SaslCramMd5) saslInstance).getChallange();
				StringBuffer xmpp = new StringBuffer();
				xmpp.append("<challenge xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
				xmpp.append(new String(Base64.encodeBase64(challange.getBytes())));
				xmpp.append("</challenge>");
				handler.getXmlWriter().write(xmpp.toString());
			} else {
				handler.getXmlWriter().write("<error/>");
			}
		} else if (currentTag == 1) {
			currentTag = -1;
			System.out.println("finish auth! receive response: " + response);
			if (saslInstance instanceof SaslDigestMd5) {
				String clientUsername = SaslDigestMd5.getUserFromClientResponse(response);
				authenticationController.requestPassword(null, clientUsername);
				
				SaslDigestMd5 saslDigestMd5 = (SaslDigestMd5) saslInstance;
				if (saslDigestMd5.getUsername() != null && saslDigestMd5.getPassword() != null) {
					if (saslDigestMd5.validateClientResponse(response)) {
						StringBuffer xmpp = new StringBuffer();
						xmpp.append("<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
		//				xmpp.append(saslDigestMd5.getServerDigestResponse(response));
						xmpp.append("</success>");
						//xmpp.append("</stream:stream>");
						handler.getXmlWriter().write(xmpp.toString());
//						throw new RenewStreamException();
					} else {
						handler.getXmlWriter().write("<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><not-authorized/></failure>");
					}
				} else {
					handler.getXmlWriter().write("<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><not-authorized/></failure>");
				}
			} else if (saslInstance instanceof SaslCramMd5) {
				String clientUsername = SaslCramMd5.getUserFromClientResponse(response);
				authenticationController.requestPassword(null, clientUsername);
				
				SaslCramMd5 saslCramMd5 = (SaslCramMd5) saslInstance;
				if (saslCramMd5.getUsername() != null && saslCramMd5.getPassword() != null) {
					if (saslCramMd5.validateClientResponse(response)) {
						StringBuffer xmpp = new StringBuffer();
						xmpp.append("<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
		//				xmpp.append(saslDigestMd5.getServerDigestResponse(response));
						xmpp.append("</success>");
						//xmpp.append("</stream:stream>");
						handler.getXmlWriter().write(xmpp.toString());
//						throw new RenewStreamException();
					} else {
						handler.getXmlWriter().write("<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><not-authorized/></failure>");
					}
				} else {
					handler.getXmlWriter().write("<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><not-authorized/></failure>");
				}
			} else if (saslInstance instanceof SaslPlain) {
				String clientUsername = SaslPlain.getUserFromClientResponse(response);
				authenticationController.requestPassword(null, clientUsername);
				
				SaslPlain saslPlain = (SaslPlain) saslInstance;
				if (saslPlain.getUsername() != null && saslPlain.getPassword() != null) {
					if (saslPlain.validateClientResponse(response)) {
						StringBuffer xmpp = new StringBuffer();
						xmpp.append("<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
		//				xmpp.append(saslDigestMd5.getServerDigestResponse(response));
						xmpp.append("</success>");
						//xmpp.append("</stream:stream>");
						handler.getXmlWriter().write(xmpp.toString());
//						throw new RenewStreamException();
					} else {
						handler.getXmlWriter().write("<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><not-authorized/></failure>");
					}
				} else {
					handler.getXmlWriter().write("<failure xmlns='urn:ietf:params:xml:ns:xmpp-sasl'><not-authorized/></failure>");
				}
			} else if (saslInstance instanceof SaslAnonymous) {
				// TODO validate if anonymous login is allowed
				
				StringBuffer xmpp = new StringBuffer();
				xmpp.append("<success xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>");
//				xmpp.append(saslDigestMd5.getServerDigestResponse(response));
				xmpp.append("</success>");
				//xmpp.append("</stream:stream>");
				handler.getXmlWriter().write(xmpp.toString());
//				throw new RenewStreamException();
			} else {
				handler.getXmlWriter().write("<error/>");
			}
		} else {
			currentTag = -1;
			handler.getXmlWriter().write("<error/>");
		}
	}

	@Override
	protected void handleEvent() throws XmlPullParserException, IOException {
		super.handleEvent();

		if (currentTag == 1 && type == XmlPullParser.TEXT) {
			response = new String(Base64.decodeBase64(getXmlPullParser().getText().getBytes()));
		}
	}

	public boolean isLoggedIn() {
		return saslInstance != null;
	}

	public void receivePasswordFormat(Serializable xmppData, String username,
			String passwordFormat) {
		// Nothing yet
	}

	public void receivePassword(Serializable xmppData, String username,
			String password) {
		System.out.println("receivePassword in AuthSasl: " + username + " / " + password);
		if (saslInstance instanceof SaslDigestMd5) {
			((SaslDigestMd5) saslInstance).setUsername(username);
			((SaslDigestMd5) saslInstance).setPassword(password);
		} else if (saslInstance instanceof SaslCramMd5) {
			((SaslCramMd5) saslInstance).setUsername(username);
			((SaslCramMd5) saslInstance).setPassword(password);
		} else if (saslInstance instanceof SaslPlain) {
			((SaslPlain) saslInstance).setUsername(username);
			((SaslPlain) saslInstance).setPassword(password);
		} else if (saslInstance instanceof SaslAnonymous) {
			// Nothing todo
		} else {
			throw new IllegalArgumentException("Unexpected sasl instance: " + saslInstance);
		}
	}

	public void receivePasswordError(Serializable xmppData, String username) {
		System.out.println("receivePasswordError in AuthSasl: " + username);
		if (saslInstance instanceof SaslDigestMd5) {
			((SaslDigestMd5) saslInstance).setUsername(null);
			((SaslDigestMd5) saslInstance).setPassword(null);
		} else if (saslInstance instanceof SaslCramMd5) {
			((SaslCramMd5) saslInstance).setUsername(null);
			((SaslCramMd5) saslInstance).setPassword(null);
		} else if (saslInstance instanceof SaslPlain) {
			((SaslPlain) saslInstance).setUsername(null);
			((SaslPlain) saslInstance).setPassword(null);
		} else if (saslInstance instanceof SaslAnonymous) {
			// Nothing todo
		} else {
			throw new IllegalArgumentException("Unexpected sasl instance: " + saslInstance);
		}
	}
}
