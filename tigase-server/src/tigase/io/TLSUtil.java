/*
 * Tigase Jabber/XMPP Server
 * Copyright (C) 2004-2012 "Artur Hefczyc" <artur.hefczyc@tigase.org>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. Look for COPYING file in the top folder.
 * If not, see http://www.gnu.org/licenses/.
 *
 * $Rev: 2996 $
 * Last modified by $Author: wojtek $
 * $Date: 2012-08-21 06:29:57 +0800 (Tue, 21 Aug 2012) $
 */

package tigase.io;

//~--- non-JDK imports --------------------------------------------------------

import static tigase.io.SSLContextContainerIfc.*;

//~--- JDK imports ------------------------------------------------------------

import java.security.KeyStore;
import java.security.cert.CertificateParsingException;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;

//~--- classes ----------------------------------------------------------------

/**
 * Describe class TLSUtil here.
 *
 *
 * Created: Mon Jan 23 14:21:31 2006
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public abstract class TLSUtil {
	private static final Logger log = Logger.getLogger(TLSUtil.class.getName());

//private static Map<String, SSLContextContainerIfc> sslContexts =
//  new HashMap<String, SSLContextContainerIfc>();
	private static SSLContextContainerIfc sslContextContainer = null;

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param params
	 *
	 * @throws CertificateParsingException
	 */
	public static void addCertificate(Map<String, String> params) throws CertificateParsingException {
		sslContextContainer.addCertificates(params);
	}

	/**
	 * Method description
	 *
	 *
	 * @param params
	 */
	public static void configureSSLContext(Map<String, Object> params) {
		String sslCC_class = (String) params.get(SSL_CONTAINER_CLASS_KEY);

		if (sslCC_class == null) {
			sslCC_class = SSL_CONTAINER_CLASS_VAL;
		}

		try {
			sslContextContainer = (SSLContextContainerIfc) Class.forName(sslCC_class).newInstance();
			sslContextContainer.init(params);
		} catch (Exception e) {
			log.log(Level.SEVERE, "Can not initialize SSL Container: " + sslCC_class, e);
			sslContextContainer = null;
		}
	}

	//~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param protocol
	 * @param hostname
	 *
	 * @return
	 */
	public static SSLContext getSSLContext(String protocol, String hostname) {
		return sslContextContainer.getSSLContext(protocol, hostname);
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	public static KeyStore getTrustStore() {
		return sslContextContainer.getTrustStore();
	}
}    // TLSUtil


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
