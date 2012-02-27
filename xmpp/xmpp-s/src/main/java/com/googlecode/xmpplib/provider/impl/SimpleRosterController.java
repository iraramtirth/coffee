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

import com.googlecode.xmpplib.provider.RosterController;
import com.googlecode.xmpplib.provider.RosterListener;

public class SimpleRosterController extends RosterController {
	private Map<RosterListener.Contact, Integer> contacts = new LinkedHashMap<RosterListener.Contact, Integer>();

	@Override
	public void requestCompleteUpdate(Serializable xmppData) {
		completeUpdate(xmppData, contacts.keySet(), contacts.values());
	}

	@Override
	public void requestStatusUpdate(Serializable xmppData,
			RosterListener.Contact contact) {
		receiveStatusUpdate(xmppData, contact, 0);
	}

	public void addContact(RosterListener.Contact contact) {
		contacts.put(contact, null);
	}

	public void addContact(RosterListener.Contact contact, Integer status) {
		contacts.put(contact, status);
	}

	public void setStatus(RosterListener.Contact contact, Integer status) {
		if (contacts.containsKey(contact)) {
			contacts.put(contact, status);
		}
	}

	public void removeContact(RosterListener.Contact contact) {
		contacts.remove(contact);
	}
}
