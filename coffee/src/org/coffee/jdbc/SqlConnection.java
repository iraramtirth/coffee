package org.coffee.jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.coffee.jdbc.dao.util.Configuration;
import org.coffee.jdbc.dao.util.Configuration.Dialect;

/**
 * 数据链连接
 * 
 * @author wangtao
 */
public class SqlConnection {

	private static String driver = null;
	private static String url = null;
	private static String username = null;
	private static String password = null;

	public static ConnectionPool cp;

	
	private static void initConnectionPool() throws SQLException {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(SqlConnection.class.getClassLoader()
					.getResource("/").getPath()	+ "jdbc.properties"));
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			driver = prop.getProperty("driver");
			if (driver.toUpperCase().contains("ORACLE")) {
				Configuration.setDialect(Dialect.ORACLE);
			} else if (driver.toUpperCase().contains("MYSQL")) {
				Configuration.setDialect(Dialect.MYSQL);
			} else {
				Configuration.setDialect(Dialect.HSQLDB);
			}
		} catch (Exception e) {
			System.out.println(e.getClass()+"...."+e.getStackTrace()[0].getClassName());
			// 默认采用Hsqldb数据库
			String[] args = "--database.0 file:mydb --dbname.0 xdb".split(" ");
			// 启动hsqldb数据库
			org.hsqldb.server.Server.main(args);
			url = "jdbc:hsqldb:hsql://localhost/xdb";
			driver = "org.hsqldb.jdbc.JDBCDriver";
			username = "SA";
			password = "";
			Configuration.setDialect(Dialect.HSQLDB);
		}
		cp = new ConnectionPool(driver, url, username, password);
	}

	public Connection getConnection() {
		try {
			if (cp == null) {
				initConnectionPool();
			}
			return cp.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection get() {
		return new SqlConnection().getConnection();
	}

	// 以下是使用数据源

	// // tomcat 数据源
	// private DataSource ds;
	// // 初始化数据源
	// private void initDataSource() {
	// Context context;
	// try {
	// context = new InitialContext();
	// ds = (DataSource) context.lookup("java:comp/env/jdbc/mysqlds");
	// } catch (NamingException e) {
	// e.printStackTrace();
	// }
	// }
	// public Connection getConnection() {
	// if (ds == null) {
	// initDataSource();
	// }
	// Connection conn = null;
	// try {
	// conn = ds.getConnection();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	// return conn;
	// }
}
