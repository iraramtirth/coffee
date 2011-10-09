package coffee.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DbHelper{
	
	private Connection conn;
	
	/**
	 * 打开数据库 
	 */
	public void openDatabase(String dbname){
		//this.getWritableDatabase();//创建db文件
	}
	/**
	 * 删除数据库 
	 */
	public void deleteDateBase(String dbname){
		//context.deleteDatabase(dbname);
	}
	/**
	 * 按照执行的beanClass创建数据库
	 * 如果数据表存且数据为空则删除，否则抛异常 
	 */
	public void createTable(Class<?> beanClass){
		try {
			String sql = TSqliteUtils.generateTableSql(beanClass);
			//conn.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void dropTable(Class<?> beanClass){
		String tableName = TSqliteUtils.getTableName(beanClass);
		//conn.execSQL("drop table " + tableName);
	}
	
	/**
	 * 新增记录
	 */
	public <T> void insert(T bean){
		String sql = null;
		try {
			sql = TSqliteUtils.getInsertSql(bean);
			//conn.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除记录 
	 */
	public <T> void delete(Class<T> beanClass, long pk){
		String tableName = TSqliteUtils.getTableName(beanClass); 
		//conn.delete(tableName, "id=?", new String[]{pk+""});
	}
	/**
	 * 更新记录
	 */
	public <T> void update(T bean){
		try {
			String sql = TSqliteUtils.getUpdateSql(bean);
			//conn.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	/**
	 * 查询记录 
	 */
	public <T> T queryForObject(Class<T> beanClass, int id){
		ResultSet c = null;
		try {
			String tableName = TSqliteUtils.getTableName(beanClass);
			String sql = "select * from " + tableName +" where id=?";
			//c = conn.rawQuery(sql, new String[]{id+""});
			List<T> lst = TSqliteUtils.processResultSetToList(c, beanClass);
			if(lst.size() > 0){
				return lst.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(c != null){
				//c.close();
			}
		}
		return null;
	}
	/**
	 * 查询记录 
	 */
	public <T> T queryForObject(String sql,Class<T> beanClass){
		List<T> lst = queryForList(sql, beanClass);
		if(lst.size() > 0){
			return lst.get(0);
		}
		return null;
	}
	/**
	 *  返回列表 
	 *  @return ： 如果无记录，则返回一个size==0的list
	 */
	public <T> List<T> queryForList(String sql,Class<T> beanClass){
		List<T> lst = new ArrayList<T>();
		ResultSet c = null;
		try {
			//c = conn.rawQuery(sql, new String[]{});
			lst = TSqliteUtils.processResultSetToList(c, beanClass);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
//			if(c != null && !c.isClosed()){
//				c.close();
//			}
		}
		return lst;
	}
	/**
	 *  单列查询
	 */
	public List<String> queryForList(String sql){
		List<String> lst = new ArrayList<String>();
		try {
//			ResultSet c = conn.rawQuery(sql, new String[]{});
//			lst = TSqliteUtils.processToStringList(c);
//			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lst;
	}
	
	/**
	 *  单列查询
	 *  @return : 如果无记录， 则返回 null 而不是 字符串"null"
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryForColumn(String sql, Class<T> ... primtiveType){
		T colValue = null;
		try {
			String value = null;
			//ResultSet c = conn.rawQuery(sql, new String[]{});
//			if(c.next()){
//				value = c.getString(0);
//			}
			//c.close();
			if("null".equals(colValue)){
				return null;
			}
			//int型号
			if(primtiveType.length > 0){
				if(primtiveType[0] == Integer.class || primtiveType[0] == int.class){
					return (T) Integer.valueOf(value);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return colValue;
	}
	
	public <T> List<T> queryForList(Class<T> beanClass, String orderBy){
		String sql = "select * from " + TSqliteUtils.getTableName(beanClass);
		if(orderBy != null){
			orderBy = "order by " + orderBy;
		}
		return this.queryForList(sql, beanClass);
	}
	
	public void execSQL(String sql){
		try{
			//conn.execSQL(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库
	 */
	public void close(){
		if(this.conn != null){
			//this.conn.close();
		}
	}
	
}
