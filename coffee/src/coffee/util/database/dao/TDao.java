package coffee.util.database.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import coffee.util.database.Pager;

/**
 * 系统DAO的公共/通用父类接口
 * 
 * @author coffee
 */
public interface TDao {
	// 获取当前数据库连接
	public Connection currentConnection();

	// 新增
	public <T> void insert(T t) throws SQLException;

	// 批量插入
	public <T> void insert(List<T> ts) throws SQLException;

	// 更新(null字段不进行处理)
	public <T> void update(T t) throws SQLException;

	// 删除记录
	public <T> void delete(long id, Class<T> clazz) throws SQLException;

	// 批量删除
	public <T> void deleteBatch(String[] ids, Class<T> clazz)
			throws SQLException;

	// 获取离线数据集
	public CachedRowSet queryForResultSet(String sql) throws SQLException;

	/**
	 * 返回Integer Long String 等类型
	 * 
	 * @param clazz
	 *            : 返回值类型
	 * @param sql
	 *            ： sql语句，支持预编译sql
	 * @param args
	 *            : 当sql为预编译语句时，args为参数
	 * @return
	 * @throws SQLException
	 */
	public <T> T queryForColumn(Class<T> clazz, String sql, Object... args)
			throws SQLException;

	/**
	 * @see 具体参数定义见 #queryForColumn(Class, String, Object...)
	 * @param clazz
	 * @param sql
	 * @param args
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> queryForColumnList(Class<T> clazz, String sql,
			Object... args) throws SQLException;

	// 查询全部记录
	public <T> List<T> queryForList(Class<T> clazz, String sql, Object... args)
			throws SQLException;

	// 分页查询记录
	public <T> List<T> queryForList(String sql, long start, int size,
			Class<T> clazz) throws SQLException;

	// 查询单个实体
	public <T> T queryForEntity(Object id, Class<T> clazz) throws SQLException;

	public <T> T queryForEntity(Class<T> clazz, String sql, Object... args)
			throws SQLException;

	// 查二维数组
	public Object[][] queryForArray(String sql) throws SQLException;

	// 加载实体；第一次家在完成之后将缓存该记录
	public <T> T loadForEntity(Object id, Class<T> clazz);

	// 执行指定SQL
	public int executeUpdate(String sql) throws SQLException;

	// 执行指定路径的sql脚本,sql命令以';'分割
	public void executeScript(String scriptPath) throws SQLException;

	public <T> Pager<T> queryForPager(String sql, int offset, int size,
			Class<T> clazz) throws SQLException;

}
