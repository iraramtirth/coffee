package org.coffee.jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.coffee.jdbc.Configuration.DialectType;

/**
 * 数据链连接
 * 该类不直接调用，由Session访问
 * @author coffee
 */
public class SqlConnection {

	private static String driver = null;
	private static String url = null;
	private static String username = null;
	private static String password = null;
	
	public static ConnectionPool cp;

	/**
	 * 初始化连接池
	 * @param cfgFile :  配置文件路径， 默认  jdbc.properties
	 */
	private static void initConnectionPool(String cfgFile) throws SQLException {
		Properties prop = new Properties();
		try {
			/**
			 * 注意不能写成**SqlConnection.class.getClass().getResource("/")
			 */
			prop.load(new FileInputStream(SqlConnection.class.getResource("/").getPath()+ cfgFile));
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			driver = prop.getProperty("driver");
			String debugStr = prop.getProperty("debug");
			try{
				Configuration.debug = Boolean.valueOf(debugStr);
			}catch(Exception e){
				Configuration.debug = false;
			}
			if (driver.toUpperCase().contains("ORACLE")) {
				Configuration.dialect = DialectType.ORACLE;
			} else if (driver.toUpperCase().contains("MYSQL")) {
				Configuration.dialect = DialectType.MYSQL;
			} else {
				Configuration.dialect = DialectType.HSQLDB;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 默认采用Hsqldb数据库
			String[] args = "--database.0 file:mydb --dbname.0 xdb".split(" ");
			// 启动hsqldb数据库
			org.hsqldb.server.Server.main(args);
			url = "jdbc:hsqldb:hsql://localhost/xdb";
			driver = "org.hsqldb.jdbc.JDBCDriver";
			username = "SA";
			password = "";
			Configuration.dialect = DialectType.HSQLDB;
		}
		cp = new ConnectionPool(driver, url, username, password);
	}

	public Connection getConnection(String cfgFile) {
		try {
			if (cp == null) {
				initConnectionPool(cfgFile);
			}
			return cp.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Connection get() {
		return new SqlConnection().getConnection("jdbc.properties");
	}
	public static Connection get(String cfgFile) {
		return new SqlConnection().getConnection(cfgFile);
	}


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
