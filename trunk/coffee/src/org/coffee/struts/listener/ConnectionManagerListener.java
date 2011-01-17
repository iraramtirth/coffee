package org.coffee.struts.listener;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.coffee.jdbc.ConnectionPool;
import org.coffee.jdbc.SqlConnection;
import org.coffee.jdbc.dao.util.Configuration;

/**
 * 初始化数据库
 * 管理数据库连接
 * @author wangtao
 */
@WebListener
public class ConnectionManagerListener implements ServletContextListener {

	private static String driver = null;
	private static String url = null;
	private static String username = null;
	private static String password = null;

	private Connection conn;
	
	private boolean isHsqldb = false;
	
	// 初始化数据库参数 param
	private void intiParam() {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(SqlConnection.class.getClassLoader()
					.getResource("/").getPath()	+ "jdbc.properties"));
			url = prop.getProperty("url");
			username = prop.getProperty("username");
			password = prop.getProperty("password");
			driver = prop.getProperty("driver");
			if (driver.toUpperCase().contains("ORACLE")) {
				Configuration.setDialect("ORACLE");
			} else if (driver.toUpperCase().contains("MYSQL")) {
				Configuration.setDialect("MYSQL");
			} else {
				Configuration.setDialect("HSQLDB");
			}
		} catch (Exception e) {
			this.isHsqldb = true;
			System.out.println(e.getClass()+"...."+e.getStackTrace()[0].getClassName());
			// 默认采用Hsqldb数据库
			String[] args = "--database.0 file:mydb --dbname.0 xdb".split(" ");
			// 启动hsqldb数据库
			org.hsqldb.server.Server.main(args);
			url = "jdbc:hsqldb:hsql://localhost/xdb";
			driver = "org.hsqldb.jdbc.JDBCDriver";
			username = "SA";
			password = "";
			Configuration.setDialect("HSQLDB");
		}
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event){
		// 初始化数据库参数
		intiParam();
		// 创建数据库连接池
		SqlConnection.cp = new ConnectionPool(driver, url, username, password);
		// 初始化数据库数据
		try {
			conn = SqlConnection.cp.getConnection();
			String tableName = "users";
			ResultSet rs = conn.getMetaData().getTables(null, null,	tableName.toUpperCase(), null);
			if (rs.next()) {
				System.out.println("数据表 " + rs.getObject(3).toString() + "已经存在");
//				//删除数据表
//				String delSql = "drop table " + tableName;
//				Statement stmt = conn.createStatement();
//				stmt.execute(delSql);
//				stmt.close();
			} else { // 数据表不存在，则创建
				Statement stmt = conn.createStatement();
				String sql = "create table "
					+ tableName
					+ "("
					+ "ID   BIGINT GENERATED BY DEFAULT AS IDENTITY (start with 1), "
					+ "USERNAME VARCHAR(20) not null,"
					+ "PASSWORD VARCHAR(20) not null," + "AGE      INT,"
					+ "BIRTHDAY TIMESTAMP," + "DESCRIBE VARCHAR(20),"
					+ "PHOTO    VARCHAR(20)" + ")";
				stmt.execute(sql);
				stmt.close();
			}
			rs.close();
		} catch(SQLException e){
			e.printStackTrace();	
		} finally{
			//将数据库放入
			event.getServletContext().setAttribute("conn", conn);
		}
			
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		if(isHsqldb){
			try {
				conn = SqlConnection.get();
				Statement stmt = conn.createStatement();
				//关闭HsqlDB数据库
				stmt.executeUpdate("SHUTDOWN");
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				try{
					conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}

}
