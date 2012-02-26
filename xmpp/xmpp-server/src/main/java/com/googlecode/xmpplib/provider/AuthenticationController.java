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
package com.googlecode.xmpplib.provider;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class AuthenticationController implements
		AuthenticationListener {
	private List<AuthenticationListener> listeners = new LinkedList<AuthenticationListener>();

	public void addListener(AuthenticationListener listener) {
		listeners.add(listener);
	}

	public void removeListener(AuthenticationListener listener) {
		listeners.remove(listener);
	}

	public void removeAllListener() {
		listeners.clear();
	}

	public void receivePasswordFormat(Serializable xmppData, String username,
			String passwordFormat) {
		for (AuthenticationListener listener : listeners) {
			listener.receivePasswordFormat(xmppData, username, passwordFormat);
		}
	}

	public void receivePassword(Serializable xmppData, String username,
			String password) {
		for (AuthenticationListener listener : listeners) {
			listener.receivePassword(xmppData, username, password);
		}
	}

	public void receivePasswordError(Serializable xmppData, String username) {
		for (AuthenticationListener listener : listeners) {
			listener.receivePasswordError(xmppData, username);
		}
	}

	public abstract void requestPasswordFormat(Serializable xmppData,
			String username);

	public abstract void requestPassword(Serializable xmppData, String username);
}
