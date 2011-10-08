package coffee.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {
	
	private Context context;
	private SQLiteDatabase db;
	/**
	 * 默认的数据库名
	 */
	private static String DB_NAME = "TEST.DB";
	
	 /**
     * @param name of the database file, or null for an in-memory database
     * ---------------------------------------------------------------
     * 		： [name 用于指定该DbHelper对象用于操作的数据库名称，即只能操作指定的database]
     * ----------------------------------------------------------------
     */
	public DbHelper(Context context, String name){
		//该操作不会立即生成db文件
		super(context, name==null? DB_NAME:name, null,1);
		//* 当调用 getWritableDatabase的时候 才开始创建数据库(db文件)
		this.context = context;
		//打开数据库
		openDatabase(name==null? DB_NAME:name);
		this.db = getWritableDatabase();
	}
	
	/**
	
	/**
	 * {@link #getWritableDatabase()}
	 * Create and/or open a database that will be used for reading and writing.
	 * 数据库创建， onCreate执行， 注意该方法只执行一次
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;	
		//该代码仅执行一次
		//.....
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("drop table if exists user");
	}
	
	
	///////// 以下是扩展方法 \\\\\\\\\\\\\\\\\\
	/**
	 * 打开数据库 
	 */
	public void openDatabase(String dbname){
		this.getWritableDatabase();//创建db文件
	}
	/**
	 * 删除数据库 
	 */
	public void deleteDateBase(String dbname){
		context.deleteDatabase(dbname);
	}
	/**
	 * 按照执行的beanClass创建数据库
	 * 如果数据表存且数据为空则删除，否则抛异常 
	 */
	public void createTable(Class<?> beanClass){
		try {
			String sql = TSqliteUtils.generateTableSql(beanClass);
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void dropTable(Class<?> beanClass){
		String tableName = TSqliteUtils.getTableName(beanClass);
		db.execSQL("drop table " + tableName);
	}
	
	/**
	 * 新增记录
	 */
	public <T> void insert(T bean){
		String sql = null;
		try {
			sql = TSqliteUtils.getInsertSql(bean);
			Log.e("DEBUG", sql);
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除记录 
	 */
	public <T> void delete(Class<T> beanClass, long pk){
		String tableName = TSqliteUtils.getTableName(beanClass); 
		db.delete(tableName, "id=?", new String[]{pk+""});
	}
	/**
	 * 更新记录
	 */
	public <T> void update(T bean){
		try {
			String sql = TSqliteUtils.getUpdateSql(bean);
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	/**
	 * 查询记录 
	 */
	public <T> T queryForObject(Class<T> beanClass, int id){
		Cursor c = null;
		try {
			String tableName = TSqliteUtils.getTableName(beanClass);
			String sql = "select * from " + tableName +" where id=?";
			c = db.rawQuery(sql, new String[]{id+""});
			List<T> lst = TSqliteUtils.processResultSetToList(c, beanClass);
			if(lst.size() > 0){
				return lst.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(c != null){
				c.close();
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
		Cursor c = null;
		try {
			c = db.rawQuery(sql, new String[]{});
			lst = TSqliteUtils.processResultSetToList(c, beanClass);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(c != null && !c.isClosed()){
				c.close();
			}
		}
		return lst;
	}
	/**
	 *  单列查询
	 */
	public List<String> queryForList(String sql){
		List<String> lst = new ArrayList<String>();
		try {
			Cursor c = db.rawQuery(sql, new String[]{});
			lst = TSqliteUtils.processToStringList(c);
			c.close();
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
			Cursor c = db.rawQuery(sql, new String[]{});
			if(c.moveToNext()){
				value = c.getString(0);
			}
			c.close();
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
			db.execSQL(sql);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 关闭数据库
	 */
	public void close(){
		if(this.db != null){
			this.db.close();
		}
	}
	
}
