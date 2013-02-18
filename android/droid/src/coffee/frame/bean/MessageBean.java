package coffee.frame.bean;

/**
 * 聊天信息
 * 
 * @author coffee<br>
 *         2013下午1:41:17
 */
public class MessageBean {
	private String name; // 聊天人姓名
	private String content; // 聊天内容

	public MessageBean() {
	}

	public MessageBean(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return this.name + ": " + this.content;
	}
}
