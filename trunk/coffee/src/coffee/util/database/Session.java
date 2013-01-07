package coffee.util.database;

import java.sql.Connection;
import java.sql.SQLException;

import coffee.util.database.dao.TDaoImpl;

/**
 * 相对于TDaoImpl Session主要增加了对Connection的管理(创建、关闭,开启关闭事务)
 * 
 * @author coffee 20122012-11-7上午11:27:12
 */
public class Session extends TDaoImpl {

	public Session() {
		super.conn = SqlConnection.get();
	}

	/**
	 * 传入一个指定的conn连接
	 * 
	 * @param conn
	 */
	public Session(Connection conn) {
		super.conn = conn;
	}

	/**
	 * 开启事务
	 */
	public void beginTransaction() {
		try {
			super.conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 提交事务
	 */
	public void commit() {
		try {
			super.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 事务回滚
	 */
	public void roolback() {
		try {
			super.conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭数据库连接
	 */
	public void close() {
		try {
			// this.conn.close();
			SqlConnection.close(super.conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
