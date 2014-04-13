package coffee.server.tcp.base;

/**
 * 主要处理消息的解析
 * 
 * @author coffee <br>
 *         2014年4月11日上午11:38:50
 */
public abstract class MessageParser {

	/**
	 * 消息的行为
	 */
	public interface Action {
		String SERV = "serv";
		String ONLINE = "online";
		String ONLINE_ACK = "online-ack";
		String MESSAGE = "message";
		String TAG = ":";
	}

	/**
	 * 获取消息的来源
	 * 
	 * @param message
	 * @return
	 */
	public static String getUserFrom(String message) {
		String[] str = message.split(":");
		if (str.length > 1) {
			String username = str[1];
			return username;
		} else {
			return "no-name";
		}
	}

	public static String getMessageBody(String message) {
		String[] str = message.split(":");
		if (str.length > 3) {
			String body = str[3];
			return body;
		} else {
			return message;
		}
	}

	/**
	 * 获取消息的去向
	 * 
	 * @return
	 */
	public String getUserTo(String message) {
		String[] str = message.split(":");
		String username = str[2];
		return username;
	}

	public String getOnlineState(String message) {
		String[] str = message.split(":");
		String state = str[3];
		return state;
	}

	/**
	 * 消息类型<br>
	 * 
	 * @return
	 */
	public String getMessageAction(String message) {
		int index = message.indexOf(":");
		if (index != -1) {
			String action = message.substring(0, index);
			return action;
		} else {
			return "";
		}
	}

	/**
	 * online:coffee:serv::
	 * 
	 * @return
	 */
	public String getOnlineToServer(int onlineState) {
		String onlineToServer = getMessage(Action.ONLINE, getUsername(), Action.SERV, "" + onlineState, "");
		return onlineToServer;
	}

	/**
	 * online-ack:client:coffee:1:
	 * 
	 * @param toUser
	 * @param myUsername
	 * @return
	 */
	public String getOnlineAck(String toUser, int onlineState) {
		return getMessage(Action.ONLINE_ACK, getUsername(), toUser, "" + onlineState, "");
	}

	public static String getMessageSend(String toUser, String messageBody) {
		return getMessage(Action.MESSAGE, getUsername(), toUser, messageBody);
	}

	private static String getMessage(String... segment) {
		StringBuilder sb = new StringBuilder();
		for (String str : segment) {
			sb.append(str).append(Action.TAG);
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static String getUsername() {
		String username = MessageParser.username;
		return username;
	}

	private static String username = "";

	public static void setUsername(String username) {
		MessageParser.username = username;
	}
}
