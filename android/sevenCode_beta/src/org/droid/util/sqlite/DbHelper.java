package org.droid.util.sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import coffee.seven.SysConfig;
import coffee.seven.bean.GoodsBean;
import coffee.seven.bean.GoodsImageBean;
import coffee.seven.bean.GoodsInfoBean;
import coffee.seven.bean.OrderBean;
import coffee.seven.bean.SaleBean;
import coffee.seven.bean.VersionBean;

public class DbHelper extends SQLiteOpenHelper {
	
	private SQLiteDatabase db;
	 /**
     * @param name of the database file, or null for an in-memory database
     * ---------------------------------------------------------------
     * 		： [name 用于指定该DbHelper对象用于操作的数据库名称，即只能操作指定的database]
     * ----------------------------------------------------------------
     */
	@SuppressWarnings("static-access")
	public DbHelper(Context context, String name){
		//该操作不会立即生成db文件
		super(context, name==null?SysConfig.DB_NAME:name, null, 6);
		//* 当调用 getWritableDatabase的时候 才开始创建数据库(db文件)
		synchronized (this) {
			//打开数据库
			this.db = getWritableDatabase();
			if(!db.isOpen()){
				this.db.openDatabase(context.getDatabasePath(name).getPath(), 
						null, SQLiteDatabase.OPEN_READWRITE);
			}
		}
	}
	/**
	 * {@link #getWritableDatabase()}
	 * Create and/or open a database that will be used for reading and writing.
	 * 数据库创建， onCreate执行， 注意该方法只执行一次
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;	//该代码仅执行一次
		this.createTable(SaleBean.class);
		this.createTable(GoodsBean.class);
		this.createTable(GoodsInfoBean.class);
		this.createTable(GoodsImageBean.class);
		this.createTable(OrderBean.class);
		this.createTable(VersionBean.class);
	}
	/**
	 CREATE TABLE IF NOT EXISTS mmb_sale(
		pic VARCHAR(255),
		goodsName VARCHAR(30),
		lastUpdateTime VARCHAR(30),
		startTime,
		endTime,
		oriPrice FLOAT ,
		price FLOAT ,
		refresh INTEGER,
		id INTEGER PRIMARY KEY AUTOINCREMENT
	)
	CREATE TABLE IF NOT EXISTS goodsbean(
		code VARCHAR(255),
		lastTime VARCHAR(255),
		title VARCHAR(255),
		saleId INTEGER,
		remainCount INTEGER,
		maxCount INTEGER
	)
	*/

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//db.execSQL("drop table if exists user");
		this.db = db;
	}
	
	
	///////// 以下是扩展方法 \\\\\\\\\\\\\\\\\\
	/**
	 * 按照执行的beanClass创建数据库
	 * 如果数据表存且数据为空则删除，否则抛异常 
	 */
	public synchronized void reCreateTable(Class<?> beanClass){
		try {
			//删除数据库
			String sql = "drop table " + TSqliteUtils.getTableName(beanClass);
			db.execSQL(sql);
			//重新创建数据库
			sql = TSqliteUtils.generateTableSql(beanClass);
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 按照执行的beanClass创建数据库
	 * 如果数据表存且数据为空则删除，否则抛异常 
	 */
	private synchronized void createTable(Class<?> beanClass){
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
	public synchronized void dropTable(String tableName){
		try{
			db.execSQL("drop table " + tableName);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增记录
	 */
	public synchronized <T> void insert(T bean){
		String sql = null;
		try {
			sql = TSqliteUtils.getInsertSql(bean);
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除记录 
	 */
	public synchronized <T> void delete(Class<T> beanClass, long pk){
		String tableName = TSqliteUtils.getTableName(beanClass); 
		db.delete(tableName, "id=?", new String[]{pk+""});
	}
	/**
	 * 更新记录
	 */
	public synchronized <T> void update(T bean){
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
	
	public <T> List<T> queryForList(Class<T> beanClass, String condition, String orderBy){
		String sql = "select * from " + TSqliteUtils.getTableName(beanClass);
		if(condition != null && !"true".equals(condition)){
			sql += " where " + condition;
		}
		if(orderBy != null){
			sql += " order by " + orderBy;
		}
		return this.queryForList(sql, beanClass);
	}
	
	/**
	 *  单列查询
	 *  @return : 如果无记录， 则返回 null 而不是 字符串"null"
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryForColumn(String sql, Class<T> primtiveType){
		T colValue = null;
		try {
			String value = null;
			Cursor c = db.rawQuery(sql, new String[]{});
			if(c.moveToNext()){
				value = c.getString(0);
			}
			c.close();//
			if("null".equals(value) || value == null){
				return null;
			}else if(primtiveType == Integer.class || primtiveType == int.class){
				colValue = (T) Integer.valueOf(value);
			}else if(primtiveType == Long.class || primtiveType == long.class){
				colValue = (T) Long.valueOf(value);
			}else{
				colValue = (T)value;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return colValue;
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
