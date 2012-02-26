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

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.ComparisonFailure;

public class AssertionReader extends Reader {
	private Reader delegate;

	private int timeout = 1000;

	public AssertionReader(Reader delegate) {
		this.delegate = delegate;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getTimeout() {
		return timeout;
	}

	@Override
	public int read() throws IOException {
		if (timeout <= 0) {
			int len = delegate.read();
			if (len == -1) {
				Assert.fail("End of stream reached.");
			}
			return len;
		} else {
			final Thread currentThread = Thread.currentThread();
			final int[] len = new int[1];
			final IOException[] ioe = new IOException[1];
			new Thread(new Runnable() {
				public void run() {
					try {
						len[0] = delegate.read();
					} catch (IOException e) {
						ioe[0] = e;
					}
					currentThread.interrupt();
				}
			}).start();
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				if (ioe[0] != null) {
					throw ioe[0];
				}
				if (len[0] == -1) {
					Assert.fail("End of stream reached.");
				}
				return len[0];
			}
			throw new IOException("Timeout reached after " + timeout + "ms.");
		}
	}

	@Override
	public int read(final char[] cbuf) throws IOException {
		if (timeout <= 0) {
			int len = delegate.read(cbuf);
			if (len == -1) {
				Assert.fail("End of stream reached.");
			}
			return len;
		} else {
			final Thread currentThread = Thread.currentThread();
			final int[] len = new int[1];
			final IOException[] ioe = new IOException[1];
			new Thread(new Runnable() {
				public void run() {
					try {
						len[0] = delegate.read(cbuf);
					} catch (IOException e) {
						ioe[0] = e;
					}
					currentThread.interrupt();
				}
			}).start();
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				if (ioe[0] != null) {
					throw ioe[0];
				}
				if (len[0] == -1) {
					Assert.fail("End of stream reached.");
				}
				return len[0];
			}
			throw new IOException("Timeout reached after " + timeout + "ms.");
		}
	}

	@Override
	public int read(final char[] cbuf, final int offset, final int length) throws IOException {
		if (timeout <= 0) {
			int len = delegate.read(cbuf, offset, length);
			if (len == -1) {
				Assert.fail("End of stream reached.");
			}
			return len;
		} else {
			final Thread currentThread = Thread.currentThread();
			final int[] len = new int[1];
			final IOException[] ioe = new IOException[1];
			new Thread(new Runnable() {
				public void run() {
					try {
						len[0] = delegate.read(cbuf, offset, length);
					} catch (IOException e) {
						ioe[0] = e;
					}
					currentThread.interrupt();
				}
			}).start();
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				if (ioe[0] != null) {
					throw ioe[0];
				}
				if (len[0] == -1) {
					Assert.fail("End of stream reached.");
				}
				return len[0];
			}
			throw new IOException("Timeout reached after " + timeout + "ms.");
		}
	}
	
	@Override
	public int read(final CharBuffer target) throws IOException {
		if (timeout <= 0) {
			int len = delegate.read(target);
			if (len == -1) {
				Assert.fail("End of stream reached.");
			}
			return len;
		} else {
			final Thread currentThread = Thread.currentThread();
			final int[] len = new int[1];
			final IOException[] ioe = new IOException[1];
			new Thread(new Runnable() {
				public void run() {
					try {
						len[0] = delegate.read(target);
					} catch (IOException e) {
						ioe[0] = e;
					}
					currentThread.interrupt();
				}
			}).start();
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				if (ioe[0] != null) {
					throw ioe[0];
				}
				if (len[0] == -1) {
					Assert.fail("End of stream reached.");
				}
				return len[0];
			}
			throw new IOException("Timeout reached after " + timeout + "ms.");
		}
	}
	
	@Override
	public void close() throws IOException {
		delegate.close();
	}

	public void assertEquals(String expected) throws IOException {
		if (expected == null) {
			throw new NullPointerException("Expected string must not be null.");
		} else if (expected.length() == 0) {
			throw new IllegalArgumentException("Expected string must not be empty.");
		}
		
		int offset = 0;
		char[] cbuf = new char[expected.length()];
		try {
			while (offset < cbuf.length) {
				offset += read(cbuf, offset, cbuf.length - offset);
			}
			Assert.assertEquals(expected, new String(cbuf));
		} catch (IOException e) {
			Assert.assertEquals(e.getMessage(), expected, new String(cbuf));
//			throw new RuntimeException("Already loaded [" + new String(cbuf, 0, offset) + "]", e);
		}
	}

	public Matcher assertRegex(String expectedRegex) throws IOException {
		if (expectedRegex == null) {
			throw new NullPointerException("Expected string must not be null.");
		} else if (expectedRegex.length() == 0) {
			throw new IllegalArgumentException("Expected string must not be empty.");
		}
		Pattern pattern = Pattern.compile(expectedRegex);
		Matcher matcher;
		StringWriter stringWriter = new StringWriter();
		try {
			int len;
			char[] cbuf = new char[1024];
			while ((len = read(cbuf)) != -1) {
				stringWriter.write(cbuf, 0, len);
				matcher = pattern.matcher(stringWriter.toString());
				if (matcher.matches()) {
					return matcher;
				}
			}
			matcher = pattern.matcher(stringWriter.toString());
			if (matcher.matches()) {
				return matcher;
			} else {
				throw new ComparisonFailure("Regex does not match: ", expectedRegex, stringWriter.toString());
			}
		} catch (IOException e) {
			matcher = pattern.matcher(stringWriter.toString());
			if (matcher.matches()) {
				return matcher;
			} else {
				throw new ComparisonFailure("Regex does not match: ", expectedRegex, stringWriter.toString());
			}
//			throw new RuntimeException("Already loaded [" + new String(cbuf, 0, offset) + "]", e);
		}
	}

	public void assertEnd() throws IOException {
		// TODO remove this workaround for threading problems...
		try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			// Nothing
		}
		
		if (timeout <= 0) {
//			try {
				StringBuffer actual = new StringBuffer();
				char[] buf = new char[1024];
				int len;
				// We stop also if there are no more data.
				while ((len = delegate.read(buf)) > 0) {
					actual.append(buf, 0, len);
				}
				if (actual.length() != 0 || len != -1) {
					throw new ComparisonFailure("End of stream not reached.", "", actual.toString());
				}
//			} catch (IOException e) {
//				// Expected
//			}
		} else {
			final Thread currentThread = Thread.currentThread();
			final StringBuffer actual = new StringBuffer();
			final int[] len = new int[1];
			final IOException[] ioe = new IOException[1];
			final RuntimeException[] re = new RuntimeException[1];
			new Thread(new Runnable() {
				public void run() {
					try {
						char[] buf = new char[1024];
						// We stop also if there are no more data.
						while ((len[0] = delegate.read(buf)) > 0) {
							actual.append(buf, 0, len[0]);
						}
					} catch (IOException e) {
						ioe[0] = e;
					} catch (RuntimeException e) {
						re[0] = e;
					}
					currentThread.interrupt();
				}
			}).start();
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				// Expected
			}
			
			if (ioe[0] != null) {
				throw ioe[0];
			}
			if (re[0] != null) {
				throw re[0];
			}
			if (actual.length() != 0 || len[0] != -1) {
				throw new ComparisonFailure("End of stream not reached.", "", actual.toString());
			}
		}
	}
}
