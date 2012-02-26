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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.googlecode.xmpplib.AssertionReader;
import com.googlecode.xmpplib.AutoFlushWriter;
import com.googlecode.xmpplib.provider.MessageListener;
import com.googlecode.xmpplib.provider.impl.SimpleMessageController;

public class MessageTest {
	@Test
	public void testReceiveMessage() throws Exception {
		final List<MessageListener.Message> incomingMessages = new LinkedList<MessageListener.Message>();
		
		MessageListener messageListener = new MessageListener() {
			public void handleSendSuccessful(Serializable xmppData) {
			}
			
			public void handleSendFailure(Serializable xmppData) {
			}
			
			public void handleIncomingMessage(Serializable xmppData, MessageListener.Message message) {
				incomingMessages.add(message);
			}
		};
		SimpleMessageController messageController = new SimpleMessageController();
		messageController.addListener(messageListener);
		
		Message message = new Message(null, messageController);
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(message, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			Assert.assertEquals(0, incomingMessages.size());
			
			autoFlushWriter.write(
					"<message xmlns='jabber:client' id=''" +
					" from='127.0.0.1'" +
					" to='christoph@127.0.0.1/nimo-13mbp'>" +
					"<body>Just a simple text message.</body>" +
					"</message>");
			// TODO
//			assertionReader.assertEquals(
//					"");
			
			// TODO sometimes the server thread need a small time to process the message above
			Thread.sleep(200);
			
			Assert.assertEquals(1, incomingMessages.size());
			MessageListener.Message message2 = incomingMessages.get(0);
			Assert.assertEquals("Just a simple text message.", message2.text);
			
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testSendMessage() throws Exception {
		SimpleMessageController messageController = new SimpleMessageController();
		
		Message message = new Message(null, messageController);
		messageController.addListener(message);
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(message, true);
		packetProcessorTestThread.start();
		
		try {
			//AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			MessageListener.Message message2 = new MessageListener.Message();
			message2.text = "Just a simple text message.";
			
			messageController.handleIncomingMessage(null, message2);
			
			// TODO change id to a random code here
			assertionReader.assertEquals(
					"<message xmlns='jabber:client'" +
					" id='null'" +
					" from='null' to='null'>" +
					"<body>Just a simple text message.</body>" +
					"</message>              ");
			
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}
}
