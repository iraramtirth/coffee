package coffee.jdbc;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import coffee.jdbc.dao.util.Configuration;
import coffee.jdbc.dao.util.Configuration.DialectType;

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
			/**
			 * 注意不能写成**SqlConnection.class.getClass().getResource("/")
			 */
			prop.load(new FileInputStream(SqlConnection.class.getResource("/")
					.getPath() + "jdbc.properties"));
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			driver = prop.getProperty("driver");
			if (driver.toUpperCase().contains("ORACLE")) {
				Configuration.dialect = DialectType.ORACLE;
			} else if (driver.toUpperCase().contains("MYSQL")) {
				Configuration.dialect = DialectType.MYSQL;
			} else {
				Configuration.dialect = DialectType.HSQLDB;
			}
		} catch (Exception e) {
			e.printStackTrace();
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
