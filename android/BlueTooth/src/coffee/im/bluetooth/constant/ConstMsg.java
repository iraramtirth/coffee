package coffee.im.bluetooth.constant;

/**
 * Handler发送的Message的what变量
 * 
 * @author coffee <br>
 *         2013-1-14下午4:09:09
 */
public class ConstMsg {

	private static final int MSG_APP_BASE = 0X00101;
	private static final int MSG_FRIEND_BASE = 0X00201;
	// IM
	private static final int MSG_IM_BASE = 0X00301;
	// 登录到tcp服务器
	public static final int LOGIN_TCP = MSG_FRIEND_BASE + 1;
	public static final int MSG_FRIEND_INVITE = MSG_FRIEND_BASE + 2;
	public static final int MSG_FRIEND_REMOVE = MSG_FRIEND_BASE + 3;

	public static final int MSG_APP_EXIT = MSG_APP_BASE + 1;

	/********************** IM **************************/
	public static final int MSG_IM_RECV_MESSAGE = MSG_IM_BASE + 1;
}
