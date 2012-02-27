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

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.xmpplib.utils.Digest;


public class DigestTest {
	@Test
	public void testSha1() {
		Assert.assertEquals("48fc78be9ec8f86d8ce1c39c320c97c21d62334d", Digest
				.toHex(Digest.SHA1("3EE948B0", "Calli0pe")));
	}

	@Test
	public void testHex() {
		for (int i = 0; i <= 255; i++) {
			byte[] originalData = new byte[] { (byte) i };
			String hexString = Digest.toHex(originalData);
			Assert.assertEquals(2, hexString.length());
			byte[] parsedData = Digest.parseHex(hexString);
			Assert.assertEquals(1, parsedData.length);
			Assert.assertEquals((byte) i, parsedData[0]);
		}
	}

	@Test
	public void testMd5Hex() {
		byte[] data = new byte[32];
		byte[] md5;
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			random.nextBytes(data);
			String hex = Digest.toHex(data);
			Assert.assertEquals(64, hex.length());
			md5 = Digest.MD5(data);
			Assert.assertEquals(16, md5.length);
			Assert.assertEquals(32, Digest.toHex(md5).length());
			Assert.assertArrayEquals(data, Digest.parseHex(hex));
		}
	}
}
