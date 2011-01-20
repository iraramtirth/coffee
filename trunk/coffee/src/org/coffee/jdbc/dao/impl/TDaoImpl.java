package org.coffee.jdbc.dao.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;

import org.coffee.jdbc.dao.TDao;
import org.coffee.jdbc.dao.util.Configuration;
import org.coffee.jdbc.dao.util.TDaoUtil;

public class TDaoImpl implements TDao{
	protected Connection conn;
	
	private static Logger log = Logger.getLogger("jdbc");
	
	static{
		log.setLevel(Level.INFO);
	}
	
	protected void setDialect(){
		
	}
	
	
	// 删除
	@Override
	public <T> void delete(Class<T> clazz, long id) throws SQLException {
		try {
			String sql = "delete from " + TDaoUtil.getTableName(clazz) + " where id=" + id;
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	// 批量删除
	@Override
	public <T> void deleteBatch(Class<T> clazz,String[] ids) throws SQLException{
		if(ids == null || ids.length == 0){
			return;
		}
		try {
			String sql = "delete from "+TDaoUtil.getTableName(clazz) +" where id=?";
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for(String id : ids){
				int idInt = 0;
				try{
					idInt = Integer.parseInt(id);
				}catch(Exception e){
					continue;
				}
				if(idInt != 0){
					pstmt.setInt(1, idInt);
					pstmt.addBatch();
				}
			}
			pstmt.executeBatch();
			conn.commit();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// 获取离线数据集
	
	@Override
	public CachedRowSet queryForResultSet(String sql) throws SQLException {
//		CachedRowSet crs = new CachedRowSetImpl();
//		Statement stmt = conn.createStatement();
//		ResultSet rs = stmt.executeQuery(sql);
//		crs.populate(rs);
//		rs.close();
//		stmt.close();
		return null;
	}
	 
	/**
	 * 执行指定sql语句
	 */
	@Override
	public int executeUpdate(String sql) throws SQLException {
		int value = 0;
		try {
			Statement stmt = conn.createStatement();
			value = stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			conn.close();
		}
		return value;
	}
	/**
	 * 指定指定文件的脚本
	 * @param scriptPath
	 */
	@Override
	public void executeScript(String scriptPath){
		try {
			BufferedReader in = new BufferedReader(new FileReader(scriptPath));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line =in.readLine()) != null){
				sb.append(line);
			}
			String[] sqls = sb.toString().split(";");
			Statement stmt = this.conn.createStatement();
			try {
				this.conn.setAutoCommit(false);
				for(String sql : sqls){
					stmt.addBatch(sql.replace("phtgraphy", "phtgraph"));
				}
				stmt.executeBatch();
				this.conn.commit();
			} catch (SQLException e) {
				e.printStackTrace();
				this.conn.rollback();
			}
			stmt.close();
		}  catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *  返回 Integer Long  String 等基本数据类型的包装类型 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T queryForColumn(Class<T> clazz, String sql) throws SQLException {
		T t = null;;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				if(clazz.getSimpleName().equals("Integer")){
					t = (T)Integer.valueOf(rs.getInt(1));
				}
				else{
					t = (T)rs.getString(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return t;
	}
	/**
	 *   查询，返回实体对象
	 */
	@Override
	public <T> T queryForEntity(Class<T> clazz, long id) throws SQLException {
		T t = null;
		try {
			t = clazz.newInstance();
			String sql = "select * from " + TDaoUtil.getTableName(clazz) + " where id = " + id;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			List<T> ls = TDaoUtil.processResultSetToList(rs, clazz);
			if(ls == null || ls.size() == 0){
				t = null;
			}else{
				t = ls.get(0);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}
	/**
	 *  查询返回list
	 */
	@Override
	public <T> List<T> queryForList(String sql, Class<T> clazz)
			throws SQLException {
		List<T> ls = new ArrayList<T>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			ls = TDaoUtil.processResultSetToList(rs, clazz);
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ls;
	}
	//分页查询
	@Override
	public <T> List<T> queryForList(String sql, long start, int size,
			Class<T> clazz) throws SQLException {
		List<T> ls = new ArrayList<T>();
		try {
			Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			stmt.setMaxRows((int)(start + size));
			ResultSet rs = stmt.executeQuery(sql);
			// 设置分页
			rs.first();
			rs.relative((int)start - 1);
			// 处理resultSet 实现分页查询
			ls = TDaoUtil.processResultSetToList(rs,clazz);
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(sql);
			e.printStackTrace();
		} 
		return ls;
	}
	/**
	 * 插入实体
	 */
	@Override
	public <T> void insert(T t) throws SQLException {
		if(t == null){
			throw new SQLException("插入数据失败，实体为null");
		}
		try {
			String sql = TDaoUtil.getInsertSql(t,Configuration.DIALECT);
			Statement stmt = conn.createStatement();
			System.out.println(sql);
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 插入实体；以String形式返回生成的主键
	 * @param t	：实体对象
	 * @param autoGeneratedKeys ：是否返回主键
	 */
	public <T> String insert(T t , boolean autoGeneratedKeys) throws SQLException {
		Object pk = null;
		if(t == null){
			throw new SQLException("插入数据失败，实体为null");
		}
		try {
			String sql = TDaoUtil.getInsertSql(t);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			sql = "select "+TDaoUtil.getSequenceName(t.getClass()) +".currval from dual";
			stmt.close();
			// 新创建一个连接 ： 如果直接用stmt(未关闭)，
			// 则会报出一个 bind variable does not exist
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				pk = rs.getString(1);
			}
			rs.close();
			stmt.close();
			log.info(sql);
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		}
		return pk.toString();
	}
	//更新
	@Override
	public <T> void update(T t) throws SQLException {
		try{
			String sql = TDaoUtil.getUpdateSql(t,Configuration.DIALECT);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void close() throws SQLException{
		try{
			this.conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(conn != null){
				conn.close();				
			}
		}
	}
	@Override
	public <T> void insert(List<T> ts) throws SQLException {
		try {
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			for(T t : ts){
				String sql = TDaoUtil.getInsertSql(t);
				stmt.addBatch(sql);
			}
			stmt.executeBatch();
			conn.commit();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			conn.rollback();
		} 
	}

}
