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
package com.googlecode.xmpplib.utils;

import java.io.IOException;
import java.io.Writer;

public class XmlWriter {
	private Writer delegate;

	public XmlWriter(Writer delegate) {
		this.delegate = delegate;
	}

	/**
	 * TODO mark this method as deprecated. But first choose a final xml reader and writer implementation.
	 */
	public void write(String xml) throws IOException {
		delegate.write(xml);
		delegate.flush();
	}

//	public void emptyTag(String tag, String[] keyValuePairs) throws IOException {
//		delegate.write("<");
//		delegate.write(tag);
//		for (int i = 0; i < keyValuePairs.length; i += 2) {
//			delegate.write(" ");
//			delegate.write(keyValuePairs[i]);
//			delegate.write("='");
//			delegate.write(keyValuePairs[i + 1]);
//			delegate.write("'");
//		}
//		delegate.write("/>");
//		delegate.flush();
//	}
//
//	public void openTag(String tag, String[] keyValuePairs) throws IOException {
//		delegate.write("<");
//		delegate.write(tag);
//		for (int i = 0; i < keyValuePairs.length; i += 2) {
//			delegate.write(" ");
//			delegate.write(keyValuePairs[i]);
//			delegate.write("='");
//			delegate.write(keyValuePairs[i + 1]);
//			delegate.write("'");
//		}
//		delegate.write(">");
//		delegate.flush();
//	}
//
//	public void closeTag(String tag) throws IOException {
//		delegate.write("</");
//		delegate.write(tag);
//		delegate.write(">");
//		delegate.flush();
//	}
}
