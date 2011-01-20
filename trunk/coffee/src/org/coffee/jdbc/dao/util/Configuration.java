package org.coffee.jdbc.dao.util;

/**
 * 设定数据库中的一些配置信息，主要用来给TDao服务的
 * @author wangtao
 */
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
	 * 主要用于处理sql语句中的关键字
	 * 比如说 order 在mysql中用 `order` ; 即用`处理
	 * 而在oracle中用"order" : 即用 "处理
	 * @param dialect ：方言
	 * @return 返回
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
		/**
		 *	java.lang.Long
		 */
		Long,
		/**
		 *  java.lang.Integer
		 */
		Integer,		
		/**
		 *  java.util.Date
		 */
		Date,			
		/**
		 * java.util.String
		 */
		String,
		/**
		 * 无意义枚举，为了支持switch中 continue + label 的使用
		 */
		Default
	}
	/**
	 * 数据库方言；用于指定数据库类型；
	 * 主要用来处理不同数据库对sql命令中的时间类型的处理存在差异 
	 * @author wangtao
	 */
	public enum Dialect{
		ORACLE,
		MYSQL,
		HSQLDB
	}

}
