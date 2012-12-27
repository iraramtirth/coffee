/*
 * 文件名: SessionLifeManager.java 版 权： Copyright Huawei Tech. Co. Ltd. All Rights
 * Reserved. 描 述: [该类的简要描述] 创建人: wangtao 创建时间:2012-6-21 修改人： 修改时间: 修改内容：[修改内容]
 */
package coffee.test;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;

import coffee.test.VoipCmdGoing.Step;

/**
 * [一句话功能简述]<BR>
 * [功能详细描述]
 * 
 * @author zhouxin
 * @version [ME WOYOUClient_Handset V100R001C04SPC002, 2012-6-21]
 */
public class SessionLifeManager {
	private static String TAG = "SessionLifeManager";

	private SessionLifeManager() {
	}

	/**
	 * 当前正在通话的session列表， 列表中的sessionId为有效ID，不在该列表中的来电直接pass掉 @ 目前只支持单路通话
	 * 
	 * FASTXMPP_VOIP_CMD_INITIAL 该命令执行完后记录当前sessionID。表示只接受该Session的通话
	 * FASTXMPP_VOIP_NTF_INFORM ......
	 * 
	 */
	private static Set<Session> initialSessions = new HashSet<Session>();

	private static Set<Session> replySessions = new HashSet<Session>();

	/*****************************************/
	private static Timer sessionTimer;

	public enum SessionLife {
		LIFE_30(30), LIFE_60(60);

		private int codeValue;

		private SessionLife(int codeValue) {
			this.codeValue = codeValue;
		}

		public int getCodeValue() {
			return this.codeValue;
		}
	}

	public enum SessionType {
		INITIAL, REPLY,
	}

	static class Session {
		// ID当前set集合如果session只能
		String woyouId;

		// 开始的时间
		long startTime;
		// 生命周期
		int period;
		/**
		 * session失效后是否通知界面
		 */
		boolean isNotifyToUser;

		/**
		 * [构造简要说明]
		 * 
		 * @param woyouId
		 * @param sessionId
		 * @param startTime
		 * @param period
		 */
		public Session(String woyouId, SessionLife sessionLife,
				boolean isNotifyToUser) {
			super();
			this.woyouId = woyouId;
			this.startTime = System.currentTimeMillis();
			if (sessionLife != null) {
				this.period = sessionLife.getCodeValue();
			} else {
				this.period = 0;
			}
			this.isNotifyToUser = isNotifyToUser;
		}

		@Override
		public int hashCode() {
			return 0x0;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Session other = (Session) obj;

			if (woyouId == null) {
				if (other.woyouId != null)
					return false;
			} else if (!woyouId.equals(other.woyouId)) {
				return false;
			}

			return true;
		}

		@Override
		public String toString() {
			return "Session [" + woyouId + "_" + startTime + "_" + period + "_"
					+ isNotifyToUser + "]";
		}

	}

	/**
	 * 判断当前session是否生效
	 */
	public static boolean isEffectSession(String woyouAccount,
			SessionType sessionType) {
		System.out.println("VOIP_Session：验证session是否生效: " + woyouAccount + ","
				+ sessionType);
		System.out.println("VOIP_session>>" + initialSessions + ","
				+ replySessions);
		// return this.callSessions.contains(sessionId);
		switch (sessionType) {
		case INITIAL:
			return initialSessions.contains(new Session(woyouAccount, null,
					true));
		case REPLY:
			return replySessions
					.contains(new Session(woyouAccount, null, true));
		default:
			break;
		}
		return false;
	}

	public static int getSessionSize(SessionType type) {
		switch (type) {
		case INITIAL:
			return initialSessions.size();
		case REPLY:
			return replySessions.size();
		default:
			return 0;
		}

	}

	/**
	 * 
	 * @param woyouAccount
	 *            : 沃友账号
	 * @param sessionId
	 *            ：
	 * @param period
	 *            ：
	 * @param isNotifyToUser
	 *            : 是否通知用户
	 */
	public static void addToSessionSet(String woyouAccount, SessionType type,
			SessionLife sessionLife, boolean isNotifyToUser) {

		if (woyouAccount != null) {
			System.out.println("VoIP_Session>>新增缓存" + woyouAccount + "," + type
					+ "," + sessionLife + "," + isNotifyToUser);
			// 加入session
			switch (type) {
			case INITIAL:
				initialSessions.add(new Session(woyouAccount, sessionLife,
						isNotifyToUser));
				break;
			case REPLY:
				replySessions.add(new Session(woyouAccount, sessionLife,
						isNotifyToUser));
				break;
			default:
				break;
			}

			if (sessionTimer != null) {
				sessionTimer.cancel();
				sessionTimer = null;
			}
			// 开启计时器
			if (sessionTimer == null) {
				sessionTimer = new Timer();
				sessionTimer.scheduleAtFixedRate(new TimerTask() {
					@Override
					public void run() {
						// 遍历 initialSessions
						for (Session ss : initialSessions) {
							if(ss == null)
							{
								continue;
							}
							
							if (System.currentTimeMillis() - ss.startTime > 1000 * ss.period) {
								// 失效
								removeSession(initialSessions, ss);
							}
						}
						// 遍历 replySessions
						for (Session ss : replySessions) {
							if (System.currentTimeMillis() - ss.startTime > 1000 * ss.period) {
								// 失效
								removeSession(replySessions, ss);
							}
						}
						if (initialSessions.size() == 0
								&& replySessions.size() == 0) {
							VoipCmdGoing.setCurrentStep(Step.GOING_NONE);
							if (sessionTimer != null) {
								sessionTimer.cancel();
								sessionTimer = null;
							}
						}
					}
				}, 0, 1000 * 1);
			} else {
				// ignore
			}

//			System.out.println("VOIP_Session:设置当前的Session：" + woyouAccount
//					+ "," + sessionLife.getCodeValue() + "秒超时" + ",initial>> "
//					+ initialSessions + ", reply " + replySessions);

		} else {
			System.out.println("VOIP_Session:当前session无效>>" + woyouAccount + ","
					+ type + "," + sessionLife + "," + isNotifyToUser);
		}
	}

	/**
	 * 
	 * [一句话功能简述]<BR>
	 * [功能详细描述]
	 * 
	 * @param woyouAccount
	 *            : 沃友账号 （主叫或者被叫方）
	 * @param sessionId
	 *            ： xmpp协商的会话sessionId
	 * @param sessionType
	 *            :
	 */
	public static void removeSession(String woyouAccount, String sessionId,
			SessionType sessionType) {

		Set<Session> set = getSessionSet(sessionType);
		if (set != null) {
			boolean result = set.remove(new Session(woyouAccount, null, false));
			if (result) {
				System.out.println("VOIP_Session>> 删除Session成功" + woyouAccount
						+ "," + sessionId);
			} else {
				System.out.println("VOIP_Session>> 删除Session失败" + woyouAccount
						+ "," + sessionId);
			}
		} else {
			System.out.println("VOIP_Session>> 该session不存在>> " + woyouAccount + ","
					+ sessionId);
		}

	}

	private static void removeSession(Set<Session> sessionSet, Session session) {
		// session失效
		sessionSet.remove(session);

		System.out.println("VOIP_Session >> 删除session" + session);

	}

	/**
	 * 清空session并且停止计时器
	 */
	public static void clearSessionsAndStopTimer() {
		System.out.println("VOIP_Session: 关闭计时器,并清空所有sessions>>"
				+ initialSessions + "," + replySessions);
		// 先停止计时
		if (sessionTimer != null) {
			sessionTimer.cancel();
		}
		// 清空所有session
		initialSessions.clear();
		replySessions.clear();

		System.out.println("VOIP_session已经建立, 停止计时");
	}

	/**
	 * 停止计时，但是还保存着sessionId
	 */
	public static void stopSessionTimer() {
		// 先停止计时
		if (sessionTimer != null) {
			sessionTimer.cancel();
		}
	}

	//
	private static Set<Session> getSessionSet(SessionType sessionType) {
		switch (sessionType) {
		case INITIAL:
			return initialSessions;
		case REPLY:
			return replySessions;
		default:
			break;
		}
		return null;
	}

	
	public static void main(String[] args) {
		SessionLifeManager.initialSessions.add(null);
		SessionLifeManager.addToSessionSet("111", SessionType.INITIAL, SessionLife.LIFE_30, true);
//		boolean bool = SessionLifeManager.isEffectSession("111", SessionType.INITIAL);
		SessionLifeManager.addToSessionSet("111", SessionType.INITIAL, SessionLife.LIFE_60, true);
//		System.out.println(bool);
		System.out.println(initialSessions);
	}
}
