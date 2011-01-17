package org.coffee.jdbc.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;
/**
 * 系统DAO的公共/通用父类接口 
 * @author wangtao
 */
public interface TDao {
	//新增
	public <T> void  insert(T t) throws SQLException;
	//更新(null字段不进行处理)
	public <T> void update(T t) throws SQLException;
	//删除记录
	public <T> void delete(Class<T> clazz, long id) throws SQLException;
	public <T> void deleteBatch(Class<T> clazz, String[] ids) throws SQLException;
	
	//获取离线数据集
	public CachedRowSet queryForResultSet(String sql) throws SQLException;
	// 返回Integer Long String 等类型
	public <T> T queryForObject(Class<T> clazz,String sql) throws SQLException;
	//查询全部记录总数
	public <T> List<T> queryForList(String sql,Class<T> clazz) throws SQLException;
	//分页查询记录
	public <T> List<T> queryForList(String sql, long start, int size,Class<T> clazz) throws SQLException;
	//查询单个实体
	public <T> T queryForObject(Class<T> clazz, long id) throws SQLException;
	// 执行指定SQL
	public int executeUpdate(String sql) throws SQLException;
	
	public void close() throws SQLException;
}
