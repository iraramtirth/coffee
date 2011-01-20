package org.coffee.jdbc;

import java.sql.SQLException;

import org.coffee.jdbc.dao.impl.TDaoImpl;
import org.coffee.jdbc.dao.util.Configuration.Dialect;
import org.coffee.spring.ioc.annotation.Repository;
/**
 * 相对于TDaoImpl
 * Session主要增加了对Connection的管理(创建、关闭,开启关闭事务)
 * @author wangtao
 */
@Repository(name="session")
public class Session extends TDaoImpl{
	/**
	 * 创建Connection
	 * 设置数据库dialect
	 * 进行数据库操作之前必须open连接
	 */
	public void open(){
		super.conn = SqlConnection.get();
		if(conn.getClass().toString().contains("mysql")){
			this.setDialect(Dialect.MYSQL);
		}
	}
	/**
	 * 开启事务
	 */
	public void beginTransaction(){
		try {
			super.conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 提交事务
	 */
	public void commit(){
		try {
			super.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 事务回滚
	 */
	public void roolback(){
		try {
			super.conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  关闭Connection
	 */
	public void close(){
		try {
			super.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
