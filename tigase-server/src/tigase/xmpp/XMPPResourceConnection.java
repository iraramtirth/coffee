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

package tigase.xmpp;

import tigase.db.AuthorizationException;
import tigase.db.TigaseDBException;
import tigase.db.AuthRepository;
import tigase.db.UserRepository;

import tigase.server.xmppsession.SessionManagerHandler;

import tigase.util.TigaseStringprepException;

import tigase.xml.Element;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Describe class XMPPResourceConnection here.
 * 
 * 
 * Created: Wed Feb 8 22:30:37 2006
 * 
 * @author <a href="mailto:artur.hefczyc@tigase.org">Artur Hefczyc</a>
 * @version $Rev: 2996 $
 */
public class XMPPResourceConnection extends RepositoryAccess {

	/**
	 * Private logger for class instances.
	 */
	private static final Logger log = Logger.getLogger(XMPPResourceConnection.class
			.getName());

	/**
	 * Constant <code>PRESENCE_KEY</code> is a key in temporary session data where
	 * the last presence sent by the user to server is stored, either initial
	 * presence or off-line presence before disconnecting.
	 */
	public static final String PRESENCE_KEY = "user-presence";
	public static final String CLOSING_KEY = "closing-conn";
	public static final String AUTHENTICATION_TIMEOUT_KEY = "authentication-timeout";
	public static final String ERROR_KEY = "error-key";

	private long authenticationTime = 0;

	/**
	 * This variable is to keep relates XMPPIOService ID only.
	 */
	private JID connectionId = null;
	private long creationTime = 0;
	private String defLang = "en";
	private long id_counter = 0;
	private long packets_counter = 0;

	/**
	 * Value of <code>System.currentTimeMillis()</code> from the time when this
	 * session last active from user side.
	 */
	private long lastAccessed = 0;
	private SessionManagerHandler loginHandler = null;
	private XMPPSession parentSession = null;
	private int priority = 0;

	/**
	 * Session resource - part of user's JID for this session
	 */
	private String resource = null;

	/**
	 * Session temporary data. All data stored in this <code>Map</code> disappear
	 * when session finishes.
	 */
	private Map<String, Object> sessionData = null;

	/**
	 * <code>sessionId</code> keeps XMPP stream session ID given at connection
	 * initialization time.
	 */
	private String sessionId = null;

	private JID userJid = null;
	
	public void incPacketsCounter() {
		++packets_counter;
	}
	
	public long getPacketsCounter() {
		return packets_counter;
	}

	/**
	 * Creates a new <code>XMPPResourceConnection</code> instance.
	 * 
	 * 
	 * @param connectionId
	 * @param rep
	 * @param authRepo
	 * @param loginHandler
	 */
	public XMPPResourceConnection(JID connectionId, UserRepository rep,
			AuthRepository authRepo, SessionManagerHandler loginHandler) {
		super(rep, authRepo);

		long currTime = System.currentTimeMillis();

		this.connectionId = connectionId;
		this.loginHandler = loginHandler;
		this.creationTime = currTime;
		this.lastAccessed = currTime;
		sessionData = new ConcurrentHashMap<String, Object>(4, 0.9f);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 */
	public List<XMPPResourceConnection> getActiveSessions() throws NotAuthorizedException {
		if (!isAuthorized()) {
			throw new NotAuthorizedException(NOT_AUTHORIZED_MSG);
		} // end of if (username == null)

		return parentSession.getActiveResources();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 * 
	 */
	public JID[] getAllResourcesJIDs() {
		return (parentSession == null) ? null : parentSession.getJIDs();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public long getAuthTime() {
		return authenticationTime - creationTime;
	}

	/**
	 * Returns user JID but without <em>resource</em> part. This is real user ID
	 * not session ID. To retrieve session ID - full JID refer to
	 * <code>getJID()</code> method.<br/>
	 * If session has not been authorized yet this method throws
	 * <code>NotAuthorizedException</code>.
	 * 
	 * @return a <code>String</code> value of user ID - this is user JID without
	 *         resource part. To obtain full user JID please refer to
	 *         <code>getJID</code> method.
	 * @exception NotAuthorizedException
	 *              when this session has not been authorized yet and some parts
	 *              of user JID are not known yet.
	 * @see #getJID()
	 */
	@Override
	public final BareJID getBareJID() throws NotAuthorizedException {
		if (!isAuthorized()) {
			throw new NotAuthorizedException(NOT_AUTHORIZED_MSG);
		} // end of if (username == null)

		return userJid.getBareJID();
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getCommonSessionData(String key) {
		return (parentSession == null) ? null : parentSession.getCommonSessionData(key);
	}

	/**
	 * Gets the value of connectionId
	 * 
	 * @return the value of connectionId
	 * @throws NoConnectionIdException
	 */
	public JID getConnectionId() throws NoConnectionIdException {
		lastAccessed = System.currentTimeMillis();

		if (this.connectionId == null) {
			throw new NoConnectionIdException(
					"Connection ID not set for this session. "
							+ "This is probably the SM session to handle traffic addressed to the server itself."
							+ " Or maybe it's a bug.");
		}

		return this.connectionId;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param jid
	 * 
	 * @return
	 * 
	 * @throws NoConnectionIdException
	 */
	public JID getConnectionId(JID jid) throws NoConnectionIdException {
		return (((parentSession == null) || (jid == null)) ? this.getConnectionId()
				: parentSession.getResourceConnection(jid).getConnectionId());
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public String getDefLang() {
		return this.defLang;
	}

	/**
	 * Returns full user JID for this session or throws
	 * <code>NotAuthorizedException</code> if session is not authorized yet and
	 * therefore user name and resource is not known yet.
	 * 
	 * @return a <code>String</code> value of calculated user full JID for this
	 *         session including resource name.
	 * @throws NotAuthorizedException
	 */
	public final JID getJID() throws NotAuthorizedException {
		if (!isAuthorized()) {
			throw new NotAuthorizedException(NOT_AUTHORIZED_MSG);
		} // end of if (username == null)

		return userJid;
	}

	/**
	 * Gets the value of lastAccessed
	 * 
	 * @return the value of lastAccessed
	 */
	public long getLastAccessed() {
		return this.lastAccessed;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public XMPPSession getParentSession() {
		return parentSession;
	}

	/**
	 * Returns last presence packet with the user presence status or
	 * <code>null</code> if the user has not yet sent an initial presence.
	 * 
	 * @return an <code>Element</code> with last presence status received from the
	 *         user.
	 */
	public Element getPresence() {
		return (Element) getSessionData(PRESENCE_KEY);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public int getPriority() {
		return priority;
	}

	/**
	 * Gets the value of resource
	 * 
	 * @return the value of resource
	 */
	public String getResource() {
		return this.resource;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public JID getSMComponentId() {
		return loginHandler.getComponentId();
	}

	/**
	 * Retrieves session data. This method gives access to temporary session data
	 * only. You can retrieve earlier saved data giving key ID to receive needed
	 * value. Please see <code>putSessionData</code> description for more details.
	 * 
	 * @param key
	 *          a <code>String</code> value of stored data ID.
	 * @return a <code>Object</code> value of data for given key.
	 * @see #putSessionData(String, Object)
	 */
	public final Object getSessionData(final String key) {
		lastAccessed = System.currentTimeMillis();

		return sessionData.get(key);
	}

	/**
	 * Gets the value of sessionId
	 * 
	 * @return the value of sessionId
	 */
	public String getSessionId() {
		return this.sessionId;
	}

	/**
	 * To get the user bare JID please use <code>getBareJID</code> method, to
	 * check the whether the user with given BareJID is owner of the session
	 * please use method <code>isUserId(...)</code>. From now one the user session
	 * may handle more than a single userId, hence getting just userId is not
	 * enough to check whether the user Id belongs to the session.
	 * 
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 * 
	 * @deprecated
	 */
	@Deprecated
	public BareJID getUserId() throws NotAuthorizedException {
		return this.getBareJID();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 */
	@Override
	public final String getUserName() throws NotAuthorizedException {
		if (!isAuthorized()) {
			throw new NotAuthorizedException(NOT_AUTHORIZED_MSG);
		} // end of if (username == null)

		return parentSession.getUserName();
	}

	// ~--- methods --------------------------------------------------------------

	/**
	 * Returns full user JID for this session without throwing the
	 * <code>NotAuthorizedException</code> exception if session is not authorized
	 * yet and therefore user name and resource is not known yet. Please note this
	 * method is for logging using only to avoid excessive use of try/catch for
	 * debugging code. It may return null.
	 * 
	 * @return a <code>String</code> value of calculated user full JID for this
	 *         session including resource name.
	 */
	public final JID getjid() {
		return userJid;
	}

	// ~--- get methods ----------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public boolean isAuthorized() {
		return super.isAuthorized() && (parentSession != null);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param outDomain
	 * @param includeComponents
	 * 
	 * @return
	 */
	public boolean isLocalDomain(String outDomain, boolean includeComponents) {
		return loginHandler.isLocalDomain(outDomain, includeComponents);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public boolean isResourceSet() {
		return this.resource != null;
	}

	/**
	 * Returns information whether this is a server (SessionManager) session or
	 * normal user session. The server session is used to handle packets addressed
	 * to the server itself (local domain name).
	 * 
	 * @return a <code>boolean</code> value of <code>true</code> if this is the
	 *         server session and <code>false</code> otherwise.
	 */
	public boolean isServerSession() {
		return false;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param bareJID
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 */
	public boolean isUserId(BareJID bareJID) throws NotAuthorizedException {
		if (!isAuthorized()) {
			throw new NotAuthorizedException(NOT_AUTHORIZED_MSG);
		} // end of if (username == null)

		return userJid.getBareJID().equals(bareJID);
	}

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 * 
	 * @param user
	 * @param digest
	 * @param id
	 * @param alg
	 * 
	 * @return
	 * 
	 * @throws AuthorizationException
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 * @throws TigaseStringprepException
	 */
	@Deprecated
	public final Authorization
			loginDigest(String user, String digest, String id, String alg)
					throws NotAuthorizedException, AuthorizationException, TigaseDBException,
					TigaseStringprepException {
		BareJID userId = BareJID.bareJIDInstance(user, getDomain().getVhost().getDomain());
		Authorization result = super.loginDigest(userId, digest, id, alg);

		if (result == Authorization.AUTHORIZED) {
			loginHandler.handleLogin(userId, this);
		} // end of if (result == Authorization.AUTHORIZED)

		return result;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param props
	 * 
	 * @return
	 * 
	 * @throws AuthorizationException
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 */
	@Override
	public final Authorization loginOther(Map<String, Object> props)
			throws NotAuthorizedException, AuthorizationException, TigaseDBException {
		Authorization result = super.loginOther(props);

		if (result == Authorization.AUTHORIZED) {
			BareJID user = (BareJID) props.get(AuthRepository.USER_ID_KEY);

			if (log.isLoggable(Level.FINEST)) {
				log.finest("UserAuthRepository.USER_ID_KEY: " + user);
			}

			loginHandler.handleLogin(user, this);
		} // end of if (result == Authorization.AUTHORIZED)

		return result;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param user
	 * @param password
	 * 
	 * @return
	 * 
	 * @throws AuthorizationException
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 * @throws TigaseStringprepException
	 */
	@Deprecated
	public final Authorization loginPlain(String user, String password)
			throws NotAuthorizedException, AuthorizationException, TigaseDBException,
			TigaseStringprepException {
		BareJID userId = BareJID.bareJIDInstance(user, getDomain().getVhost().getDomain());
		Authorization result = super.loginPlain(userId, password);

		if (result == Authorization.AUTHORIZED) {
			loginHandler.handleLogin(userId, this);
		} // end of if (result == Authorization.AUTHORIZED)

		return result;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @throws NotAuthorizedException
	 */
	@Override
	public final void logout() throws NotAuthorizedException {
		loginHandler.handleLogout(getBareJID(), this);
		streamClosed();
		super.logout();
	}

	// public boolean isDummy() {
	// return dummy;
	// }
	// public void setDummy(boolean dummy) {
	// this.dummy = dummy;
	// }

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	public String nextStanzaId() {
		return "tig" + (++id_counter);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param key
	 * @param value
	 */
	public void putCommonSessionData(String key, Object value) {
		if (parentSession != null) {
			parentSession.putCommonSessionData(key, value);
		}
	}

	// public void setOnHold() {
	// onHold = true;
	// }
	// public boolean isOnHold() {
	// return onHold;
	// }

	/**
	 * Saves given session data. Data are saved to temporary storage only and are
	 * accessible during this session life only and only from this session
	 * instance.<br/>
	 * Any <code>Object</code> can be stored and retrieved through
	 * <code>getSessionData(...)</code>.<br/>
	 * To access permanent storage to keep data between session instances you must
	 * use one of <code>get/setData...(...)</code> methods familly. They gives you
	 * access to hierachical permanent data base. Permanent data base however can
	 * be accessed after successuf authorization while session storage is availble
	 * all the time.
	 * 
	 * @param key
	 *          a <code>String</code> value of stored data key ID.
	 * @param value
	 *          a <code>Object</code> value of data stored in session.
	 * @see #getSessionData(String)
	 */
	public final void putSessionData(final String key, final Object value) {
		lastAccessed = System.currentTimeMillis();
		sessionData.put(key, value);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param authProps
	 * @throws TigaseDBException
	 */
	@Override
	public void queryAuth(Map<String, Object> authProps) throws TigaseDBException {
		super.queryAuth(authProps);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param key
	 * 
	 * @return
	 */
	public Object removeCommonSessionData(String key) {
		return (parentSession == null) ? null : parentSession.removeCommonSessionData(key);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param parent
	 */
	public void removeParentSession(final XMPPSession parent) {
		synchronized (this) {
			parentSession = null;
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param key
	 */
	public final void removeSessionData(final String key) {
		lastAccessed = System.currentTimeMillis();
		sessionData.remove(key);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param lang
	 */
	public void setDefLang(String lang) {
		this.defLang = lang;
	}

	/**
	 * Sets the value of lastAccessed
	 * 
	 * @param argLastAccessed
	 *          Value to assign to this.lastAccessed
	 */
	public void setLastAccessed(final long argLastAccessed) {
		this.lastAccessed = argLastAccessed;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param parent
	 * 
	 * @throws TigaseStringprepException
	 */
	public void setParentSession(final XMPPSession parent) throws TigaseStringprepException {
		synchronized (this) {
			if (parent != null) {
				userJid =
						JID.jidInstance(parent.getUserName(), domain.getVhost().getDomain(),
								((resource != null) ? resource : sessionId));
			}

			this.parentSession = parent;
		}
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param packet
	 */
	public void setPresence(Element packet) {
		putSessionData(PRESENCE_KEY, packet);

		// Parse resource priority:
		String pr_str = packet.getCData("/presence/priority");

		if (pr_str != null) {
			int pr = 1;

			try {
				pr = Integer.decode(pr_str);
			} catch (NumberFormatException e) {
				if (log.isLoggable(Level.FINER)) {
					log.finer("Incorrect priority value: " + pr_str + ", setting 1 as default.");
				}

				pr = 1;
			}

			setPriority(pr);
		}

		loginHandler.handlePresenceSet(this);
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param priority
	 */
	public void setPriority(final int priority) {
		this.priority = priority;
	}

	/**
	 * Sets the connection resource
	 * 
	 * @param argResource
	 *          Value to assign to this.resource
	 * @throws NotAuthorizedException
	 * @throws TigaseStringprepException
	 */
	public void setResource(final String argResource) throws NotAuthorizedException,
			TigaseStringprepException {
		if (!isAuthorized()) {
			throw new NotAuthorizedException(NOT_AUTHORIZED_MSG);
		} // end of if (username == null)

		this.resource = argResource;

		// This is really unlikely a parent session would be null here but it may
		// happen when the user disconnects just after sending resource bind.
		// Due to asynchronous nature of packets processing in the Tigase the
		// the authorization might be canceled while resource bind packet still
		// waits in the queue.....
		// This has already happened....
		if (parentSession != null) {
			parentSession.addResourceConnection(this);
		}

		userJid = userJid.copyWithResource((resource == null) ? sessionId : resource);
		loginHandler.handleResourceBind(this);
	}

	/**
	 * Sets the value of sessionId
	 * 
	 * @param argSessionId
	 *          Value to assign to this.sessionId
	 */
	public void setSessionId(final String argSessionId) {
		this.sessionId = argSessionId;
	}

	// ~--- methods --------------------------------------------------------------

	/**
	 * Method description
	 * 
	 */
	public void streamClosed() {
		synchronized (this) {
			if (parentSession != null) {
				parentSession.streamClosed(this);
				parentSession = null;
			}
		} // end of if (parentSession != null)

		resource = null;
		sessionId = null;
	}

	/**
	 * Method description
	 * 
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "user_jid=" + userJid + ", packets=" + packets_counter + ", connectioId=" + connectionId + ", domain="
				+ domain.getVhost().getDomain() + ", authState=" + getAuthState().name()
				+ ", isAnon=" + isAnonymous();
	}

	/**
	 * Method description
	 * 
	 * 
	 * @param name_param
	 * 
	 * @return
	 * 
	 * @throws NotAuthorizedException
	 * @throws TigaseDBException
	 * @throws TigaseStringprepException
	 */
	@Override
	public Authorization unregister(String name_param) throws NotAuthorizedException,
			TigaseDBException, TigaseStringprepException {
		Authorization auth_res = super.unregister(name_param);

		// if (auth_res == Authorization.AUTHORIZED) {
		// List<XMPPResourceConnection> res_conn =
		// parentSession.getActiveResources();
		// for (XMPPResourceConnection res: res_conn) {
		// if (res != this) {
		// res.logout();
		// } // end of if (res != this)
		// } // end of for (XMPPResourceConnection res: res_conn)
		// logout();
		// } // end of if (res == Authorization.AUTHORIZED)
		return auth_res;
	}

	@Override
	protected void login() {
		authenticationTime = System.currentTimeMillis();
	}
} // XMPPResourceConnection

// ~ Formatted in Sun Code Convention

// ~ Formatted by Jindent --- http://www.jindent.com
