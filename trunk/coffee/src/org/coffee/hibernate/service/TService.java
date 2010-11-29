package org.coffee.hibernate.service;

import java.util.List;

import org.coffee.util.PagerModel;
/**
 * service层通用父类接口
 * @author wangtao
 *
 */
public interface TService {
	public <T> void insert(T t) throws Exception;
	public <T> void delete(Class<T> clazz, long id) throws Exception;
	public <T> void update(T t) throws Exception;
	public <T> T queryForObject(Class<T> clazz, long id)throws Exception;
	public <T> List<T> query(String sql, Class<T> t) throws Exception;
	// 指行指定sql
	public void executeUpdate(String sql) throws Exception;
	
	/**
	 * @param sql ： 该语句不带有分页条件 ：需要自行拼接
	 * @param start　: 偏移量
	 * @param size : 分页条数 
	 */
	public <T> PagerModel<T> getPagerModel(String sql,int start,int size,Class<T> clazz) throws Exception;
	public int getCount(String sql) throws Exception;
	public <T> int getCount(Class<T> clazz) throws Exception;
	
}
