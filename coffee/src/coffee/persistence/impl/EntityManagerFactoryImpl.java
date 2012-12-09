package coffee.persistence.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import coffee.persistence.pool.ConnectionPool;

public class EntityManagerFactoryImpl implements EntityManagerFactory{

	/**
	 *  数据库连接
	 */
	private ConnectionPool cp;
	
	public EntityManagerFactoryImpl(Map<String, String> map){
		cp = new ConnectionPool(map.get("driver"), map.get("url"),
				map.get("username"), map.get("password"));
	}
	
	@Override
	public void close() {
		
	}

	@Override
	public EntityManager createEntityManager() {
		Connection conn = null;
		try {
			conn = cp.getConnection();
			//设置开启事务模式
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		EntityManager em = new EntityManagerExtImpl(conn);
		return em;
	}

	@Override
	public EntityManager createEntityManager(Map<String, String> map) {
			
		
		
		return null;
	}

	@Override
	public boolean isOpen() {
		
		return false;
	}

	public Cache getCache() {
		// TODO Auto-generated method stub
		return null;
	}

	public CriteriaBuilder getCriteriaBuilder() {
		// TODO Auto-generated method stub
		return null;
	}

	public Metamodel getMetamodel() {
		// TODO Auto-generated method stub
		return null;
	}

	public PersistenceUnitUtil getPersistenceUnitUtil() {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<String, Object> getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
