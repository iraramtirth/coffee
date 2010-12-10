package com.ccibs.service.impl;

import java.util.List;

import com.ccibs.dao.PagerModel;
import com.ccibs.dao.TDao;
import com.ccibs.dao.impl.TDaoImpl;
import com.ccibs.service.TService;
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
		this.dao.close();
	}

	@Override
	public <T> List<T> query(String sql, Class<T> t) throws Exception{
		List<T> ls = this.dao.queryForList(sql, t);
		this.dao.close();
		return ls;
	}

	@Override
	public <T> void update(T t) throws Exception {
		this.dao.update(t);
		this.dao.close();
	}
	@Override
	public <T> void delete(Class<T> clazz, long id) throws Exception {
		this.dao.delete(clazz,id);
		this.dao.close();
	}
	@Override
	public <T> void deleteBatch(Class<T> clazz,String ids) throws Exception{
		this.dao.deleteBatch(clazz, ids.split(",+"));
		this.dao.close();
	}
	@Override
	public <T> T queryForObject(Class<T> clazz, long id) throws Exception {
		T t = this.dao.queryForObject(clazz,id);
		this.dao.close();
		return t;
	}
	
	@Override
	public <T> T queryForObject(Class<T> clazz, String sql) throws Exception {
		T t = this.dao.queryForObject(clazz,sql);
		this.dao.close();
		return t;
	}
	@Override
	public void executeUpdate(String sql) throws Exception {
		this.dao.executeUpdate(sql);
		this.dao.close();
	}
	
	@Override
	public <T> PagerModel<T> queryForPagerModel(String sql, int offset, int size,
			Class<T> clazz) throws Exception {
		String countSql = sql.replaceAll("select\\s*.*? from", "select count(*) from");
		if(offset < 0){
			offset = 0;
		}
		PagerModel<T> pager = new PagerModel<T>();
		pager.setItems(this.dao.queryForList(sql,offset,size,clazz));
		pager.setTotal(this.getCount(countSql));
		pager.setCurpage(offset/size + 1);
		pager.setOffset(offset);
		this.dao.close();
		return pager;
	}
	
	@Override
	public int getCount(String sql) throws Exception {
		int value = (Integer)this.dao.queryForObject(Integer.class,sql);
		this.dao.close();
		return value;
	}

	public static void main(String[] args) {
		String sql = "ss,,ssd,,sdsd,sdsd,,";
		String[] strs = sql.split(",+");
		for(String str : strs){
			System.out.println(str);
		}
		System.out.println();
	}
}