package org.coffee.hibernate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqlConnection {
	private static Connection conn;
	
	private static void init(){
		String driver = "oracle.jdbc.OracleDriver";
		String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:orcl";
		String user = "scott";
		String password = "tiger";
		
		driver = "com.mysql.jdbc.Driver";
		jdbcUrl = "jdbc:mysql://localhost:3306/test";
		user = "root";
		password = "root";
		
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(jdbcUrl,user,password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		if(conn == null){
			init();
		}
		return conn;
	}

}
