package org.coffee.hibernate.service.impl;

import java.util.List;

import org.coffee.hibernate.dao.TDao;
import org.coffee.hibernate.dao.mysql.impl.TDaoImpl;
import org.coffee.hibernate.service.TService;
import org.coffee.util.PagerModel;
/**
 * service层通用父类接口实现类
 * 
 * @author wangtao
 */
public class TServiceImpl implements TService {

	private TDao dao;

	public TServiceImpl(){
		this.dao = new TDaoImpl();
	}
	public void setDao(TDao dao) {
		this.dao = dao;
	}

	@Override
	public <T> void insert(T t)throws Exception {
		this.dao.insert(t);
	}

	@Override
	public <T> List<T> query(String sql, Class<T> t) throws Exception{
		return this.dao.queryForList(sql, t);
	}

	@Override
	public <T> void update(T t) throws Exception {
		this.dao.update(t);
	}
	@Override
	public <T> void delete(Class<T> clazz, long id) throws Exception {
		this.dao.delete(clazz,id);
	}
	@Override
	public <T> T queryForObject(Class<T> clazz, long id) throws Exception {
		return this.dao.queryForObject(clazz,id);
	}
	@Override
	public void executeUpdate(String sql) throws Exception {
		this.dao.executeUpdate(sql);
	}
	
	
	@Override
	public <T> PagerModel<T> getPagerModel(String sql, int start, int size,
			Class<T> clazz) throws Exception {
		String countSql = sql.replaceAll("select\\s*.*? from", "select count(*) from");
		if(start <0){
			start = 0;
		}
		sql += " limit "+start + "," + size;
		PagerModel<T> pager = new PagerModel<T>();
		pager.setItems(this.dao.queryForList(sql, clazz));

		pager.setTotal(this.getCount(countSql));
		return pager;
	}
	
	@Override
	public int getCount(String sql) throws Exception {
		
		return this.dao.queryForCount(sql);
	}
	@Override
	public <T> int getCount(Class<T> clazz) throws Exception {
		String sql = "select count(*) from " + clazz.getSimpleName().toLowerCase();
		return this.dao.queryForCount(sql);
	}

}