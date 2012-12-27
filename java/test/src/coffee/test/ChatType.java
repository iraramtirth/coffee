package coffee.test;

public enum ChatType {
	TYPE_1TO1(0), // 0 1对1聊天
	TYPE_GROUP(1), // 1 群组聊天
	TYPE_TEMP_GROUP(2), // 2 聊吧、 多人会话聊天
	TYPE_TOGETHER_SEND(3), // 3 群发
	TYPE_MULTI_SEND(4), // 4 短信群发
	TYPE_STRANGER(5), // 5 陌生人
	TYPE_SHARED(6); // 6 分享

	public int codeValue;

	ChatType(int codeValue) {
		this.codeValue = codeValue;
	}

	public int getCodeValue() {
		return codeValue;
	}

}
