package org.coffee.util;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
/**
 * 数据链连接
 * @author wangtao
 *
 */
public class SqlConnection {
	
//	private static String driver = null;
//	private static String url = null;
//	private static String username = null;
//	private static String password = null;

//	static{
//		url = "jdbc:mysql://192.168.1.36:3306/ccibs?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
//		username = "ccibs";
//		password = "ccibs";
//		driver = "com.mysql.jdbc.Driver";
//	}

//	private static ConnectionPool cp;
	
//	private static void init(){
//		cp = new ConnectionPool(driver,url,username,password);
//	}
//	
//	public static Connection getConnection1() {
//		if (cp == null) {
//			init();
//		}
//		try {
//			return cp.getConnection();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	// tomcat 数据源
	
	private  DataSource ds;

	private   void initDataSource() {
		Context context;
		try {
			context = new InitialContext();
			ds = (DataSource) context.lookup("java:comp/env/jdbc/mysqlds");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
//	public Connection getCon() throws SQLException{
//		
//		ConnectionPool cp=new ConnectionPool("com.mysql.jdbc.Driver","jdbc:mysql://192.168.1.36:3306/ccibs?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&useServerPrepStmts=true","ccibs","ccibs");
//		return cp.getConnection();
//	}

	public Connection getConnection() {
		if (ds == null) {
			initDataSource();
		}
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
}
