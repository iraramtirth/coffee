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

public abstract class MessageController implements MessageListener {
	private List<MessageListener> listeners = new LinkedList<MessageListener>();

	public void addListener(MessageListener listener) {
		listeners.add(listener);
	}

	public void removeListener(MessageListener listener) {
		listeners.remove(listener);
	}

	public void removeAllListener() {
		listeners.clear();
	}

	public void handleSendSuccessful(Serializable xmppData) {
		for (MessageListener listener : listeners) {
			listener.handleSendSuccessful(xmppData);
		}
	}

	public void handleSendFailure(Serializable xmppData) {
		for (MessageListener listener : listeners) {
			listener.handleSendFailure(xmppData);
		}
	}

	public void handleIncomingMessage(Serializable xmppData, Message message) {
		for (MessageListener listener : listeners) {
			listener.handleIncomingMessage(xmppData, message);
		}
	}
}
