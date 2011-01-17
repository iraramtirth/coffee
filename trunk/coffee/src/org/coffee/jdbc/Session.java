package org.coffee.jdbc;

import java.sql.SQLException;

import org.coffee.jdbc.dao.impl.TDaoImpl;
import org.coffee.spring.ioc.annotation.Repository;
/**
 * 相对于TDaoImpl
 * Session主要增加了对Connection的管理(创建、关闭)
 * @author wangtao
 */
@Repository(name="session")
public class Session extends TDaoImpl{
	// 创建Connection
	public void open(){
		super.conn = SqlConnection.get();
	}
	// 关闭Connection
	public void close(){
		try {
			super.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
