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

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

//~--- classes ----------------------------------------------------------------

/**
 * Describe class SSLContextContainerOLD here.
 *
 *
 * Created: Mon Jan 23 14:47:55 2006
 *
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class SSLContextContainerOLD implements SSLContextContainerIfc {
	private static final Logger log = Logger.getLogger(SSLContextContainerOLD.class.getName());

	//~--- fields ---------------------------------------------------------------

	private String def_cert_alias = null;
	private SecureRandom secureRandom = null;
	private Map<String, SSLContext> sslContexts = new HashMap<String, SSLContext>(10);
	private Map<String, KeyManagerFactory> kmfs = new HashMap<String, KeyManagerFactory>(10);

//private KeyManagerFactory kmf = null;
	private TrustManagerFactory tmf = null;

	//~--- constructors ---------------------------------------------------------

	/**
	 * Constructs ...
	 *
	 */
	public SSLContextContainerOLD() {

//  log.config("Initializing SSL library (trust all certs mode)...");
//  init(null, null, null, null);
	}

	//~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 *
	 *
	 * @param params
	 */
	@Override
	public void addCertificates(Map<String, String> params) {

		// Do nothing, not supported yet
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
	@Override
	public SSLContext getSSLContext(final String protocol, String hostname) {
		if (hostname == null) {
			hostname = def_cert_alias;
		}    // end of if (hostname == null)

		String map_key = hostname + protocol;
		SSLContext sslContext = sslContexts.get(map_key);

		if (sslContext == null) {
			try {
				sslContext = SSLContext.getInstance(protocol);

				KeyManagerFactory kmf = kmfs.get(hostname);

				if (kmf == null) {
					kmf = kmfs.get(def_cert_alias);
				}    // end of if (kmf == null)

				if ((kmf != null) && (tmf != null)) {

//        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
//          secureRandom);
					sslContext.init(kmf.getKeyManagers(), new X509TrustManager[] { new FakeTrustManager() },
							secureRandom);
				} else {
					if (kmf == null) {
						log.warning("No certificate found for host: " + hostname);
					}    // end of if (kmf == null)

					sslContext.init((kmf != null) ? kmf.getKeyManagers() : null,
							new X509TrustManager[] { new FakeTrustManager() }, secureRandom);
				}    // end of if (kmf != null && tmf != null) else

				sslContexts.put(map_key, sslContext);
				log.config("Created SSL context for: " + sslContext.getProtocol());
			}      // end of try
					catch (Exception e) {
				log.log(Level.SEVERE, "Can not initialize SSLContext", e);
				sslContext = null;
			}      // end of try-catch
		}        // end of if (sslContext == null)

		return sslContext;
	}

	/**
	 * Method description
	 *
	 *
	 * @return
	 */
	@Override
	public KeyStore getTrustStore() {
		KeyStore trustKeyStore = null;

		try {
			trustKeyStore = KeyStore.getInstance("JKS");
			trustKeyStore.load(null, new char[0]);
		} catch (Exception ex) {
			trustKeyStore = null;
		}

		return trustKeyStore;
	}

	//~--- methods --------------------------------------------------------------

//public SSLContextContainerOLD(String k_store, String k_passwd,
// //     String def_cert_alias) {
// //     log.config("Initializing SSL library (trust all certs mode)...");
// //     this.def_cert_alias = def_cert_alias;
// //     init(k_store, k_passwd, null, null);
//}
//public SSLContextContainerOLD(String k_store, String k_passwd,
//  String t_store, String t_passwd, String def_cert_alias) {
//  log.config("Initializing SSL library...");
//  this.def_cert_alias = def_cert_alias;
//  init(k_store, k_passwd, t_store, t_passwd);
//}

	/**
	 * Method description
	 *
	 *
	 * @param params
	 */
	@Override
	public void init(Map<String, Object> params) {
		String k_store = (String) params.get(JKS_KEYSTORE_FILE_KEY);
		String k_passwd = (String) params.get(JKS_KEYSTORE_PWD_KEY);
		String t_store = (String) params.get(TRUSTSTORE_FILE_KEY);
		String t_passwd = (String) params.get(TRUSTSTORE_PWD_KEY);

		init(k_store, k_passwd, t_store, t_passwd);
	}

	private void init(String k_store, String k_passwd, String t_store, String t_passwd) {
		try {
			if ((k_store != null) && (k_passwd != null)) {
				final KeyStore keys = KeyStore.getInstance("JKS");
				final char[] keys_password = k_passwd.toCharArray();

				keys.load(new FileInputStream(k_store), keys_password);

				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

				kmf.init(keys, keys_password);
				kmfs.put(null, kmf);

				Enumeration<String> aliases = keys.aliases();
				ArrayList<String> certlist = null;

				if (aliases != null) {
					certlist = new ArrayList<String>();

					while (aliases.hasMoreElements()) {
						String alias = aliases.nextElement();

						if (keys.isCertificateEntry(alias)) {
							certlist.add(alias);
						}    // end of if (keys.isCertificateEntry(alias))
					}
				}

				aliases = keys.aliases();

				KeyStore.PasswordProtection pass_param = new KeyStore.PasswordProtection(keys_password);

				if (aliases != null) {
					while (aliases.hasMoreElements()) {
						String alias = aliases.nextElement();

						if (keys.isKeyEntry(alias)) {
							KeyStore.Entry entry = keys.getEntry(alias, pass_param);

							// KeyStore.Entry entry = keys.getEntry(alias, null);
							KeyStore alias_keys = KeyStore.getInstance("JKS");

							alias_keys.load(null, keys_password);

							if (certlist != null) {
								for (String certal : certlist) {
									alias_keys.setCertificateEntry(certal, keys.getCertificate(certal));
								}    // end of for (String certal: certlist)
							}      // end of if (root != null)

							alias_keys.setEntry(alias, entry, pass_param);

							// alias_keys.setEntry(alias, entry, null);
							kmf = KeyManagerFactory.getInstance("SunX509");
							kmf.init(alias_keys, keys_password);
							kmfs.put(alias, kmf);
						}    // end of if (!alias.equals("root"))
					}      // end of while (aliases.hasMoreElements())
				}        // end of if (aliases != null)
			}          // end of if (k_store != null && k_passwd != null)

			if ((t_store != null) && (t_passwd != null)) {
				final KeyStore trusts = KeyStore.getInstance("JKS");
				final char[] trusts_password = t_passwd.toCharArray();

				trusts.load(new FileInputStream(t_store), trusts_password);
				tmf = TrustManagerFactory.getInstance("SunX509");
				tmf.init(trusts);
			}          // end of if (t_store != null && t_passwd != null)

			secureRandom = new SecureRandom();
			secureRandom.nextInt();
		}            // end of try
				catch (Exception e) {
			log.log(Level.SEVERE, "Can not initialize SSL library", e);
		}            // end of try-catch
	}

	//~--- inner classes --------------------------------------------------------

	private static class FakeTrustManager implements X509TrustManager {
		private X509Certificate[] acceptedIssuers = null;

		//~--- constructors -------------------------------------------------------

		/**
		 * Constructs ...
		 *
		 */
		public FakeTrustManager() {}

		/**
		 * Constructs ...
		 *
		 *
		 * @param ai
		 */
		public FakeTrustManager(X509Certificate[] ai) {
			acceptedIssuers = ai;
		}

		//~--- methods ------------------------------------------------------------

		// Implementation of javax.net.ssl.X509TrustManager

		/**
		 * Method description
		 *
		 *
		 * @param x509CertificateArray
		 * @param string
		 *
		 * @throws CertificateException
		 */
		@Override
		public void checkClientTrusted(final X509Certificate[] x509CertificateArray,
				final String string)
				throws CertificateException {}

		/**
		 * Method description
		 *
		 *
		 * @param x509CertificateArray
		 * @param string
		 *
		 * @throws CertificateException
		 */
		@Override
		public void checkServerTrusted(final X509Certificate[] x509CertificateArray,
				final String string)
				throws CertificateException {}

		//~--- get methods --------------------------------------------------------

		/**
		 * Method description
		 *
		 *
		 * @return
		 */
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return acceptedIssuers;
		}
	}
}    // SSLContextContainerOLD


//~ Formatted in Sun Code Convention


//~ Formatted by Jindent --- http://www.jindent.com
