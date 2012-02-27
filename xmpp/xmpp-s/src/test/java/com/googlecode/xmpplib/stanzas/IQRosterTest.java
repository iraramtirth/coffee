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

import org.junit.Test;

import com.googlecode.xmpplib.AssertionReader;
import com.googlecode.xmpplib.AutoFlushWriter;
import com.googlecode.xmpplib.provider.RosterListener;
import com.googlecode.xmpplib.provider.impl.SimpleRosterController;

public class IQRosterTest {
	@Test
	public void test() throws Exception {
		IQ iq = new IQ(null);
		
		SimpleRosterController rosterController = new SimpleRosterController();
		rosterController.addContact(new RosterListener.Contact("ABC", "123"));
		rosterController.addContact(new RosterListener.Contact("DEF", "456"));
		rosterController.addContact(new RosterListener.Contact("GHI", "789"));
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(iq, true);
		packetProcessorTestThread.packetProcessor.handler.roster = new Roster(packetProcessorTestThread.packetProcessor.handler, rosterController);
		packetProcessorTestThread.start();
		
		rosterController.addListener(packetProcessorTestThread.packetProcessor.handler.roster);
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<iq xmlns='jabber:client' type='result' id='1234567890'" +
					" from='127.0.0.1'" +
					" to='christoph@127.0.0.1/nimo-13mbp'>" +
					"<query xmlns='jabber:iq:roster'></query>" +
					"</iq>");
			assertionReader.assertEquals(
					"<iq xmlns='jabber:client' type='result' id='null'" +
					" from='127.0.0.1'" +
					" to='christoph@127.0.0.1'>" +
					"<query xmlns='jabber:iq:roster'>\n" +
					"<item jid='123@127.0.0.1' name='ABC' subscription='both'>" +
					"<group>Android</group></item>\n" +
					"<item jid='456@127.0.0.1' name='DEF' subscription='both'>" +
					"<group>Android</group></item>\n" +
					"<item jid='789@127.0.0.1' name='GHI' subscription='both'>" +
					"<group>Android</group></item>\n" +
					"</query>" +
					"</iq>");
			assertionReader.assertRegex("" +
					"<presence xmlns='jabber:client' type='result'" +
					" id='[0-9]+'" +
					" from='123@127.0.0.1'" +
					" to='christoph@127.0.0.1'>" +
					"<show>away</show>" +
					"<status>be right back</status>" +
					"<priority>0</priority>" +
					"</presence>" +
					"<presence xmlns='jabber:client' type='result'" +
					" id='[0-9]+'" +
					" from='456@127.0.0.1'" +
					" to='christoph@127.0.0.1'>" +
					"<show>away</show>" +
					"<status>be right back</status>" +
					"<priority>0</priority>" +
					"</presence>" +
					"<presence xmlns='jabber:client' type='result'" +
					" id='[0-9]+'" +
					" from='789@127.0.0.1'" +
					" to='christoph@127.0.0.1'>" +
					"<show>away</show>" +
					"<status>be right back</status>" +
					"<priority>0</priority>" +
					"</presence>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}
}
