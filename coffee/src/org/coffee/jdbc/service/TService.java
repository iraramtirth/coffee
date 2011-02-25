package org.coffee.jdbc.service;

import org.coffee.jdbc.Pager;

/**
 * service层通用父类接口
 * @author wangtao
 */
public interface TService {
	
	/**
	 * @param sql ： 该语句不带有分页条件 ：需要自行拼接
	 * @param start　: 偏移量
	 * @param size : 分页条数 
	 */
	public <T> Pager<T> queryForPager(String sql,int start,int size,Class<T> clazz) throws Exception;
}
