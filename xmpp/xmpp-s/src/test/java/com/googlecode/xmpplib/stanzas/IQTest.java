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

public class IQTest {
	@Test
	public void test() throws Exception {
		IQ iq = new IQ(null);
		
		PacketProcessorTestThread packetProcessorTestThread = new PacketProcessorTestThread(iq, true);
		packetProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(packetProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(packetProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<iq xmlns='jabber:client' type='result' id=''" +
					" from='127.0.0.1'" +
					" to='christoph@127.0.0.1/nimo-13mbp'" +
					"/>");
			assertionReader.assertEquals(
					"<iq xmlns='jabber:client' type='result' id='null'" +
					" from='127.0.0.1'" +
					" to='christoph@127.0.0.1/nimo-13mbp'" +
					"/>");
			packetProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			packetProcessorTestThread.throwInThreadException();
			throw e;
		}
	}
}
