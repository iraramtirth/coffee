package org.coffee.jdbc.dao;

import java.sql.Connection;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

/**
 * 系统DAO的公共/通用父类接口 
 * @author wangtao
 */
public interface TDao {
	//获取当前数据库连接
	public Connection currentConnection();
	//新增
	public <T> void insert(T t) throws Exception;
	//批量插入
	public <T> void insert(List<T> ts) throws Exception;
	//更新(null字段不进行处理)
	public <T> void update(T t) throws Exception;
	//删除记录
	public <T> void delete(Class<T> clazz, long id) throws Exception;
	//批量删除
	public <T> void deleteBatch(Class<T> clazz, String[] ids) throws Exception;
	//获取离线数据集
	public CachedRowSet queryForResultSet(String sql) throws Exception;
	// 返回Integer Long String 等类型
	public <T> T queryForColumn(Class<T> clazz,String sql) throws Exception;
	//查询全部记录
	public <T> List<T> queryForList(String sql,Class<T> clazz) throws Exception;
	//分页查询记录
	public <T> List<T> queryForList(String sql, long start, int size,Class<T> clazz) throws Exception;
	//查询单个实体
	public <T> T queryForEntity(Class<T> clazz, Object id) throws Exception;
	//查二维数组
	public Object[][] queryForArray(String sql) throws Exception;
	//加载实体；第一次家在完成之后将缓存该记录
	public <T> T loadForEntity(Class<T> clazz, Object id);
	//执行指定SQL
	public int executeUpdate(String sql) throws Exception;
	//执行指定路径的sql脚本,sql命令以';'分割
	public void executeScript(String scriptPath) throws Exception;
	//关闭Connection对象
	public void close() throws Exception;
}
