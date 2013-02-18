package coffee.frame.logic;

import coffee.frame.constant.ConstMsg;
import coffee.frame.logic.base.BaseLogic;

/**
 * 主要用于TestBean相关的操作
 * 
 * @author coffee <br>
 *         2013-1-11上午11:11:02
 */
public class TestLogic extends BaseLogic {

	private static TestLogic instance;

	private TestLogic() {

	}

	public static TestLogic getInstance() {
		if (instance == null) {
			instance = new TestLogic();
		}
		return instance;
	}

	public boolean login(String name, String pwd) {
		sendMessage(ConstMsg.MSG_FRIEND_INVITE, "发送好友请求");
		return false;
	}

}
