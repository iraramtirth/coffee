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
import java.util.Collection;

public interface RosterListener {
	public void completeUpdate(Serializable xmppData,
			Collection<RosterListener.Contact> contacts,
			Collection<Integer> status);

	public void receiveStatusUpdate(Serializable xmppData,
			RosterListener.Contact contact, Integer status);

	public static class Contact {
		private String name;
		private String number;

		public Contact() {
			// nothing
		}

		public Contact(String name, String number) {
			this.name = name;
			this.number = number;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getName() {
			return name;
		}

		public String getNumber() {
			return number;
		}
	}
}
