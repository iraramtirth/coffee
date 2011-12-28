package coffee.util.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	
	
	private Context context;
	private SQLiteDatabase db;
	 /**
     * @param name of the database file, or null for an in-memory database
     * ---------------------------------------------------------------
     * 		： [name 用于指定该DbHelper对象用于操作的数据库名称，即只能操作指定的database]
     * ----------------------------------------------------------------
     */
	public DbHelper(Context context, String name){
		//该操作不会立即生成db文件
		super(context, name, null,1);
		//* 当调用 getWritableDatabase的时候 才开始创建数据库(db文件)
		this.context = context;
		db = this.getWritableDatabase();
	}
	/**
	 * {@link #getWritableDatabase()}
	 * Create and/or open a database that will be used for reading and writing.
	 * 数据库创建， onCreate执行， 注意该方法只执行一次
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//db.execSQL("create table user ()");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists user");
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
			String sql = TUtils.generateTableSql(beanClass);
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void dropTable(Class<?> beanClass){
		String tableName = TUtils.getTableName(beanClass);
		db.execSQL("drop table " + tableName);
	}
	
	/**
	 * 新增记录
	 */
	public <T> void insert(T bean){
		try {
			String sql = TUtils.getInsertSql(bean);
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除记录 
	 */
	public <T> void delete(Class<T> beanClass, int pk){
		String tableName = TUtils.getTableName(beanClass); 
		db.delete(tableName, "id=?", new String[]{pk+""});
	}
	/**
	 * 更新记录
	 */
	public <T> void update(T bean){
		try {
			String sql = TUtils.getUpdateSql(bean);
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	/**
	 * 查询记录 
	 */
	public <T> T queryForObject(Class<T> beanClass, int id){
		String tableName = TUtils.getTableName(beanClass);
		String sql = "select * from " + tableName +" where id=?";
		Cursor c = db.rawQuery(sql, new String[]{id+""});
		try {
			List<T> lst = TUtils.processResultSetToList(c, beanClass);
			if(lst.size() > 0){
				return lst.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		c.close();
		return null;
	}
	/**
	 *  返回列表 
	 */
	public <T> List<T> queryForList(String sql,Class<T> beanClass){
		Cursor c = db.rawQuery(sql, new String[]{});
		List<T> lst = new ArrayList<T>();
		try {
			lst = TUtils.processResultSetToList(c, beanClass);
		} catch (Exception e) {
			e.printStackTrace();
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
			lst = TUtils.processToStringList(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lst;
	}
	/**
	 * 关闭数据库
	 */
	public void close(){
		this.getReadableDatabase().close();
	}
}
