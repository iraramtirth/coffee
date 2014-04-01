package coffee.persistence.impl;

import static coffee.persistence.impl.EntityManagerExtUtils.getEntityProperty;
import static coffee.persistence.impl.EntityManagerExtUtils.getInsertSql;
import static coffee.persistence.impl.EntityManagerExtUtils.getPrimaryKeyColumnName;
import static coffee.persistence.impl.EntityManagerExtUtils.getTableName;
import static coffee.persistence.impl.EntityManagerExtUtils.getUpdateSql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.Metamodel;
import javax.sql.rowset.CachedRowSet;

import coffee.persistence.impl.EntityManagerExtUtils.EntityProperty;

/**
 * 每个EntityManager关联着一个数据库连接，一个事务 
 * @author coffee
 */
public class EntityManagerExtImpl implements EntityManagerExt{
	/**
	 * 与该EntityManager关联的数据库连接
	 */
	protected Connection conn;
	/**
	 * 与该EntityManager相关联的事务
	 */
	private EntityTransaction transaction;
	
	private static Logger log = Logger.getLogger("jdbc");
	static{
		log.setLevel(Level.INFO);
	}
	
	
	public EntityManagerExtImpl(Connection conn){
		this.conn = conn;
		this.transaction = new JDBCTransaction(conn);
	}
	
	// 批量删除
	
	public <T> void deleteBatch(Class<T> clazz,String[] ids) throws SQLException{
		if(ids == null || ids.length == 0){
			return;
		}
		try {
			String sql = "delete from "+EntityManagerExtUtils.getTableName(clazz) +" where id=?";
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
	 *  返回 Integer Long  String 等基本数据类型的包装类型 
	 */
	@SuppressWarnings("unchecked")
	
	public <T> T queryForObject(Class<T> clazz, String sql) throws SQLException {
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
	 
	
	//分页查询
	
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
			ls = EntityManagerExtUtils.processResultSetToList(rs,clazz);
			rs.close();
			stmt.close();
		} catch (Exception e) {
			System.out.println(sql);
			e.printStackTrace();
		} 
		return ls;
	}

	
	@Override
	public void clear() {
		
		
	}

	@Override
	public boolean contains(Object entity) {
		
		return false;
	}

	@Override
	public Query createNamedQuery(String name) {
		
		return null;
	}

	@Override
	public Query createNativeQuery(String sqlString) {
		
		return null;
	}

	@Override
	public Query createNativeQuery(String sqlString, Class<?> resultClass) {
		Query query = new CoffeeQueryImpl(this.conn,sqlString,resultClass);
		return query;
	}

	@Override
	public Query createNativeQuery(String sqlString, String resultSetMapping) {
		
		return null;
	}

	@Override
	public Query createQuery(String qlString) {
		
		return null;
	}

	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey) {
		T t = null;
		try {
			t = entityClass.newInstance();
			String sql = "select * from " + getTableName(entityClass) +
						 " where " +getPrimaryKeyColumnName(entityClass)+" = " + primaryKey;
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			List<T> ls = EntityManagerExtUtils.processResultSetToList(rs, entityClass);
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

	@Override
	public void flush() {
		
		
	}

	@Override
	public Object getDelegate() {
		
		return null;
	}

	@Override
	public FlushModeType getFlushMode() {
		
		return null;
	}

	@Override
	public <T> T getReference(Class<T> entityClass, Object primaryKey) {
		
		return null;
	}

	@Override
	public EntityTransaction getTransaction() {
		return this.transaction;
	}

	@Override
	public boolean isOpen() {
		
		return false;
	}

	@Override
	public void joinTransaction() {
		
		
	}

	@Override
	public void lock(Object entity, LockModeType lockMode) {
		
		
	}

	@Override
	public <T> T merge(T entity) {
		
		return null;
	}

	@Override
	public void persist(Object entity){
		try{
			if(entity == null){
				throw new SQLException("插入数据失败，实体为null");
			}
			String sql = getInsertSql(entity);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void refresh(Object entity) {
		try{
			String sql = getUpdateSql(entity);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void remove(Object entity) {
		try {
			Map<EntityProperty,Object> props = getEntityProperty(entity);
			String sql = "delete from " + props.get(EntityProperty.TableName) + 
						 " where " + props.get(EntityProperty.PkName) +
						 " = " + props.get(EntityProperty.PkValue);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			log.info(sql);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public void setFlushMode(FlushModeType flushMode) {
		
		
	}

	/**
	 * 关闭数据库连接
	 */
	@Override
	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
		
		return null;
	}

	@Override
	public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void detach(Object entity) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public <T> T find(Class<T> entityClass, Object primaryKey,
			LockModeType lockMode) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LockModeType getLockMode(Object entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Metamodel getMetamodel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void lock(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object entity, Map<String, Object> properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object entity, LockModeType lockMode,
			Map<String, Object> properties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProperty(String propertyName, Object value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1, Map<String, Object> arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T find(Class<T> arg0, Object arg1, LockModeType arg2,
			Map<String, Object> arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
