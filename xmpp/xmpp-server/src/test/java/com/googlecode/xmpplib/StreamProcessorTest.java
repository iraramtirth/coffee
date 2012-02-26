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
package com.googlecode.xmpplib;

import org.junit.Test;

public class StreamProcessorTest {
	@Test
	public void testDefaultSasl() throws Exception {
		StreamProcessorTestThread streamProcessorTestThread = new StreamProcessorTestThread();
		streamProcessorTestThread.start();
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(streamProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(streamProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<stream:stream" +
					" xmlns='jabber:component:accept'" +
					" xmlns:stream='http://etherx.jabber.org/streams'" +
			// 		" allowMultiple='true'" +
			// 		" to='").append(to).append("'" +
			// 		" from='").append(to).append("'" +
					" id='" + Long.toString(System.currentTimeMillis()) + "'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<?xml version='1.0' encoding='UTF-8'?>" +
					"<stream:stream" +
					" xmlns='jabber:client'" +
					" xmlns:stream='http://etherx.jabber.org/streams' to='null'" +
					" from='null'" +
					" id='0'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<stream:features>" +
					"<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"<mechanism>DIGEST-MD5</mechanism>" +
					"<mechanism>CRAM-MD5</mechanism>" +
					"</mechanisms>" +
					"</stream:features>");
			autoFlushWriter.write(
					"</stream:stream>");
			assertionReader.assertEquals(
					"</stream:stream>");
			streamProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			streamProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testOnlyAnonoumysSasl() throws Exception {
		StreamProcessorTestThread streamProcessorTestThread = new StreamProcessorTestThread();
		streamProcessorTestThread.start();
		
		XmppServer xmppServer = streamProcessorTestThread.getXmppServer();
		xmppServer.setSaslAnonymous(true);
		xmppServer.setSaslPlain(false);
		xmppServer.setSaslCramMd5(false);
		xmppServer.setSaslDigestMd5(false);
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(streamProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(streamProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<stream:stream" +
					" xmlns='jabber:component:accept'" +
					" xmlns:stream='http://etherx.jabber.org/streams'" +
			// 		" allowMultiple='true'" +
			// 		" to='").append(to).append("'" +
			// 		" from='").append(to).append("'" +
					" id='" + Long.toString(System.currentTimeMillis()) + "'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<?xml version='1.0' encoding='UTF-8'?>" +
					"<stream:stream" +
					" xmlns='jabber:client'" +
					" xmlns:stream='http://etherx.jabber.org/streams' to='null'" +
					" from='null'" +
					" id='0'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<stream:features>" +
					"<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"<mechanism>ANONYMOUS</mechanism>" +
					"</mechanisms>" +
					"</stream:features>");
			autoFlushWriter.write(
					"</stream:stream>");
			assertionReader.assertEquals(
					"</stream:stream>");
			streamProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			streamProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testOnlyPlainSasl() throws Exception {
		StreamProcessorTestThread streamProcessorTestThread = new StreamProcessorTestThread();
		streamProcessorTestThread.start();

		XmppServer xmppServer = streamProcessorTestThread.getXmppServer();
		xmppServer.setSaslAnonymous(false);
		xmppServer.setSaslPlain(true);
		xmppServer.setSaslCramMd5(false);
		xmppServer.setSaslDigestMd5(false);
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(streamProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(streamProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<stream:stream" +
					" xmlns='jabber:component:accept'" +
					" xmlns:stream='http://etherx.jabber.org/streams'" +
			// 		" allowMultiple='true'" +
			// 		" to='").append(to).append("'" +
			// 		" from='").append(to).append("'" +
					" id='" + Long.toString(System.currentTimeMillis()) + "'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<?xml version='1.0' encoding='UTF-8'?>" +
					"<stream:stream" +
					" xmlns='jabber:client'" +
					" xmlns:stream='http://etherx.jabber.org/streams' to='null'" +
					" from='null'" +
					" id='0'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<stream:features>" +
					"<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"<mechanism>PLAIN</mechanism>" +
					"</mechanisms>" +
					"</stream:features>");
			autoFlushWriter.write(
					"</stream:stream>");
			assertionReader.assertEquals(
					"</stream:stream>");
			streamProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			streamProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testOnlyCramMd5Sasl() throws Exception {
		StreamProcessorTestThread streamProcessorTestThread = new StreamProcessorTestThread();
		streamProcessorTestThread.start();

		XmppServer xmppServer = streamProcessorTestThread.getXmppServer();
		xmppServer.setSaslAnonymous(false);
		xmppServer.setSaslPlain(false);
		xmppServer.setSaslCramMd5(true);
		xmppServer.setSaslDigestMd5(false);
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(streamProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(streamProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<stream:stream" +
					" xmlns='jabber:component:accept'" +
					" xmlns:stream='http://etherx.jabber.org/streams'" +
			// 		" allowMultiple='true'" +
			// 		" to='").append(to).append("'" +
			// 		" from='").append(to).append("'" +
					" id='" + Long.toString(System.currentTimeMillis()) + "'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<?xml version='1.0' encoding='UTF-8'?>" +
					"<stream:stream" +
					" xmlns='jabber:client'" +
					" xmlns:stream='http://etherx.jabber.org/streams' to='null'" +
					" from='null'" +
					" id='0'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<stream:features>" +
					"<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"<mechanism>CRAM-MD5</mechanism>" +
					"</mechanisms>" +
					"</stream:features>");
			autoFlushWriter.write(
					"</stream:stream>");
			assertionReader.assertEquals(
					"</stream:stream>");
			streamProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			streamProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testOnlyDigestMd5Sasl() throws Exception {
		StreamProcessorTestThread streamProcessorTestThread = new StreamProcessorTestThread();
		streamProcessorTestThread.start();

		XmppServer xmppServer = streamProcessorTestThread.getXmppServer();
		xmppServer.setSaslAnonymous(false);
		xmppServer.setSaslPlain(false);
		xmppServer.setSaslCramMd5(false);
		xmppServer.setSaslDigestMd5(true);
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(streamProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(streamProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<stream:stream" +
					" xmlns='jabber:component:accept'" +
					" xmlns:stream='http://etherx.jabber.org/streams'" +
			// 		" allowMultiple='true'" +
			// 		" to='").append(to).append("'" +
			// 		" from='").append(to).append("'" +
					" id='" + Long.toString(System.currentTimeMillis()) + "'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<?xml version='1.0' encoding='UTF-8'?>" +
					"<stream:stream" +
					" xmlns='jabber:client'" +
					" xmlns:stream='http://etherx.jabber.org/streams' to='null'" +
					" from='null'" +
					" id='0'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<stream:features>" +
					"<mechanisms xmlns='urn:ietf:params:xml:ns:xmpp-sasl'>" +
					"<mechanism>DIGEST-MD5</mechanism>" +
					"</mechanisms>" +
					"</stream:features>");
			autoFlushWriter.write(
					"</stream:stream>");
			assertionReader.assertEquals(
					"</stream:stream>");
			streamProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			streamProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testWithoutAnySaslMechanism() throws Exception {
		StreamProcessorTestThread streamProcessorTestThread = new StreamProcessorTestThread();
		streamProcessorTestThread.start();

		XmppServer xmppServer = streamProcessorTestThread.getXmppServer();
		xmppServer.setSaslAnonymous(false);
		xmppServer.setSaslPlain(false);
		xmppServer.setSaslCramMd5(false);
		xmppServer.setSaslDigestMd5(false);
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(streamProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(streamProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<stream:stream" +
					" xmlns='jabber:component:accept'" +
					" xmlns:stream='http://etherx.jabber.org/streams'" +
			// 		" allowMultiple='true'" +
			// 		" to='").append(to).append("'" +
			// 		" from='").append(to).append("'" +
					" id='" + Long.toString(System.currentTimeMillis()) + "'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<?xml version='1.0' encoding='UTF-8'?>" +
					"<stream:stream" +
					" xmlns='jabber:client'" +
					" xmlns:stream='http://etherx.jabber.org/streams' to='null'" +
					" from='null'" +
					" id='0'" +
					" version='1.0'>");
			autoFlushWriter.write(
					"</stream:stream>");
			assertionReader.assertEquals(
					"</stream:stream>");
			streamProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			streamProcessorTestThread.throwInThreadException();
			throw e;
		}
	}

	@Test
	public void testWithDisabledSaslSupport() throws Exception {
		StreamProcessorTestThread streamProcessorTestThread = new StreamProcessorTestThread();
		streamProcessorTestThread.start();

		XmppServer xmppServer = streamProcessorTestThread.getXmppServer();
		xmppServer.setSaslSupported(false);
		
		try {
			AutoFlushWriter autoFlushWriter = new AutoFlushWriter(streamProcessorTestThread.getClientOutput());
			AssertionReader assertionReader = new AssertionReader(streamProcessorTestThread.getClientInput());
			
			autoFlushWriter.write(
					"<stream:stream" +
					" xmlns='jabber:component:accept'" +
					" xmlns:stream='http://etherx.jabber.org/streams'" +
			// 		" allowMultiple='true'" +
			// 		" to='").append(to).append("'" +
			// 		" from='").append(to).append("'" +
					" id='" + Long.toString(System.currentTimeMillis()) + "'" +
					" version='1.0'>");
			assertionReader.assertEquals(
					"<?xml version='1.0' encoding='UTF-8'?>" +
					"<stream:stream" +
					" xmlns='jabber:client'" +
					" xmlns:stream='http://etherx.jabber.org/streams' to='null'" +
					" from='null'" +
					" id='0'" +
					" version='1.0'>");
			autoFlushWriter.write(
					"</stream:stream>");
			assertionReader.assertEquals(
					"</stream:stream>");
			streamProcessorTestThread.shutdown();
			assertionReader.assertEnd();
			
		} catch (AssertionError e) {
			streamProcessorTestThread.throwInThreadException();
			throw e;
		}
	}
}
