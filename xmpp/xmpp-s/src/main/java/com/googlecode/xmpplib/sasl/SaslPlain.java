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

public class SaslPlain {
	private String username;
	private String password;

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

	public String getClientResponse() {
		if (username == null) {
			throw new RuntimeException("Could not create challange without username.");
		}
		if (password == null) {
			throw new RuntimeException("Could not create challange without password.");
		}
		return username + '\0' + username + '\0' + password;
	}

	public boolean validateClientResponse(String response) {
		return getClientResponse().equals(response);
	}

	public static String getUserFromClientResponse(String response) {
		int pos = response.indexOf('\0');
		return response.substring(0, pos);
	}
}
