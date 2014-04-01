package coffee.persistence.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.Query;
import javax.persistence.TemporalType;
/**
 * persistence接口的实现 
 * @author coffee
 */
public class CoffeeQueryImpl implements CoffeeQuery{

	/**
	 *  与此Query相关的当前数据库连接
	 *  创建QueryImpl的时候传入该对象
	 */
	private Connection currentConn;
	/**
	 *  当前查询所要执行的sql语句 
	 */
	private String sqlString;
	
	private Class<?> resultClass;
	
	public <T> CoffeeQueryImpl(Connection currentConn,String sqlString, Class<T> resultClass){
		this.currentConn = currentConn;
		this.sqlString = sqlString;
		this.resultClass = resultClass;
	}
	
	@Override
	public int executeUpdate() {
		
		return 0;
	}

	@Override
	public int getFirstResult() {
		
		return 0;
	}

	@Override
	public FlushModeType getFlushMode() {
		
		return null;
	}

	@Override
	public Map<String, Object> getHints() {
		
		return null;
	}

	@Override
	public LockModeType getLockMode() {
		
		return null;
	}

	@Override
	public int getMaxResults() {
		
		return 0;
	}

	@Override
	public Parameter<?> getParameter(String name) {
		
		return null;
	}

	@Override
	public <T> Parameter<T> getParameter(String name, Class<T> type) {
		
		return null;
	}

	@Override
	public Parameter<?> getParameter(int position) {
		
		return null;
	}

	@Override
	public <T> Parameter<T> getParameter(int position, Class<T> type) {
		
		return null;
	}

	@Override
	public <T> T getParameterValue(Parameter<T> param) {
		
		return null;
	}

	@Override
	public Object getParameterValue(String name) {
		
		return null;
	}

	@Override
	public Object getParameterValue(int position) {
		
		return null;
	}

	@Override
	public Set<Parameter<?>> getParameters() {
		
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<?> getResultList() {
		List<?> ls = new ArrayList();
		try {
			Statement stmt = this.currentConn.createStatement();
			ResultSet rs = stmt.executeQuery(this.sqlString);
			ls = EntityManagerExtUtils.processResultSetToList(rs, this.resultClass);
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ls;
	}

	@Override
	public Object getSingleResult() {
		
		return null;
	}

	@Override
	public boolean isBound(Parameter<?> param) {
		
		return false;
	}

	@Override
	public Query setFirstResult(int startPosition) {
		
		return null;
	}

	@Override
	public Query setFlushMode(FlushModeType flushMode) {
		
		return null;
	}

	@Override
	public Query setHint(String hintName, Object value) {
		
		return null;
	}

	@Override
	public Query setLockMode(LockModeType lockMode) {
		
		return null;
	}

	@Override
	public Query setMaxResults(int maxResult) {
		
		return null;
	}

	@Override
	public <T> Query setParameter(Parameter<T> param, T value) {
		
		return null;
	}

	@Override
	public Query setParameter(Parameter<Calendar> param, Calendar value,
			TemporalType temporalType) {
		
		return null;
	}

	@Override
	public Query setParameter(Parameter<Date> param, Date value,
			TemporalType temporalType) {
		
		return null;
	}

	@Override
	public Query setParameter(String name, Object value) {
		
		return null;
	}

	@Override
	public Query setParameter(String name, Calendar value,
			TemporalType temporalType) {
		
		return null;
	}

	@Override
	public Query setParameter(String name, Date value, TemporalType temporalType) {
		
		return null;
	}

	@Override
	public Query setParameter(int position, Object value) {
		
		return null;
	}

	@Override
	public Query setParameter(int position, Calendar value,
			TemporalType temporalType) {
		
		return null;
	}

	@Override
	public Query setParameter(int position, Date value,
			TemporalType temporalType) {
		
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> cls) {
		
		return null;
	}

}
