package org.coffee.hibernate.dao.util;


public class Configuration {
	/**
	 * 对SQL语句关键字的处理
	 */
	private static final String TABLE_TOKEN_ORACLE = "\"";
	private static final String TABLE_TOKEN_MYSQL = "`";
	private static final String TABLE_TOKEN_HSQLDB = "";
	// 数据库方言；默认是 Oracle
	public static String DIALECT = "ORACLE";

	public static String getDialect() {
		return DIALECT;
	}
	public static void setDialect(String dialect) {
		DIALECT = dialect;
	}
	
	/**
	 *  获取 
	 */
	public static String getToken(String dialect){
		try {
			if(dialect.toUpperCase().contains("ORACLE")){
				return TABLE_TOKEN_ORACLE;
			}else if(dialect.toUpperCase().contains("MYSQL")){
				return TABLE_TOKEN_MYSQL;
			}else{
				return TABLE_TOKEN_HSQLDB;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return DIALECT;
	}
	
	public enum MappedType {
		Long,			// java.lang.Long
		Integer,		// java.lang.Integer
		Date,			// java.util.Date
		String			// java.util.String
	}

}
