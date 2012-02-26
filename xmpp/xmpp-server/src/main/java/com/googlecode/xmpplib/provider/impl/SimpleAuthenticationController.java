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
package com.googlecode.xmpplib.provider.impl;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.googlecode.xmpplib.provider.AuthenticationController;

public class SimpleAuthenticationController extends AuthenticationController {
	private Map<String, String> userdata = new LinkedHashMap<String, String>();

	public void addUser(String username, String password) {
		userdata.put(username, password);
	}

	public void removeUser(String username) {
		userdata.remove(username);
	}

	public void removeAllUsers() {
		userdata.clear();
	}

	@Override
	public void requestPasswordFormat(Serializable xmppData, String username) {
		if (userdata.containsKey(username)) {
			receivePasswordFormat(xmppData, username, "PLAIN");
		} else {
			receivePasswordFormat(xmppData, username, null);
		}
	}

	@Override
	public void requestPassword(Serializable xmppData, String username) {
		if (userdata.containsKey(username)) {
			receivePassword(xmppData, username, userdata.get(username));
		} else {
			receivePasswordError(xmppData, username);
		}
	}
}
