package org.coffee.persistence.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityTransaction;
/**
 * JDBC的事务实现 
 * @author wangtao
 */
public class JDBCTransaction implements EntityTransaction{

	/**
	 * 与当前事务相关联的数据库连接
	 */
	private Connection conn;
	
	public JDBCTransaction(Connection conn){
		this.conn = conn;
	}
	
	/**
	 * 起开事务
	 * 设置事务保存点
	 * @Override
	 */
	public void begin() {
		try {
			this.conn.setAutoCommit(false);
			this.conn.setSavepoint();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 提交事务
	 * @Override
	 */
	public void commit() {
		try {
			this.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean getRollbackOnly() {
		
		return false;
	}
	/**
	 * 
	 * @Override
	 */
	public boolean isActive() {
		
		return false;
	}

	/**
	 *  回滚事务
	 *  @Override
	 */
	public void rollback() {
		try {
			this.conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setRollbackOnly() {
		
		
	}

}
