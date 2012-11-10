package coffee.cms.admin.bean;

import coffee.database.annotation.Bean;
import coffee.database.annotation.Column;
import coffee.database.annotation.Id;

/**
 * Tigase XMPP Server创建的User相关联的外键表
 * 
 * @author coffee 20122012-11-10上午9:34:28
 */
@Bean(name = "tig_nodes")
public class TigNodeBean {
	
	@Id
	@Column(name="nid")
	private int id;
	@Column(name="parent_nid")
	private int parentNid;
	/**
	 * 外键
	 * 
	 * @see TigUserBean.id
	 */
	private int uid;
	private String node = "root";// root

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentNid() {
		return parentNid;
	}

	public void setParentNid(int parentNid) {
		this.parentNid = parentNid;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}
}
