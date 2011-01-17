package org.coffee.jdbc.service.impl;

import java.util.List;

import org.coffee.jdbc.Session;
import org.coffee.jdbc.service.TService;
import org.coffee.spring.ioc.annotation.Resource;
import org.coffee.spring.ioc.annotation.Service;
import org.coffee.struts.PagerModel;
/**
 * service层通用父类接口实现类
 * @author wangtao
 */
@Service(name="service")
public class TServiceImpl implements TService {

	private Session session;

	public TServiceImpl(){
//		this.dao = new TDaoImpl();
	}
	@Resource(name="session")
	public void setSession(Session session) {
		this.session = session;
	}

	@Override
	public <T> void insert(T t)throws Exception {
		this.session.open();
		this.session.insert(t);
		this.session.close();
	}
	
	@Override
	public <T> List<T> query(String sql, Class<T> t) throws Exception{
		this.session.open();
		List<T> ls = this.session.queryForList(sql, t);
		this.session.close();
		return ls;
	}

	@Override
	public <T> void update(T t) throws Exception {
		this.session.open();
		this.session.update(t);
		this.session.close();
	}
	@Override
	public <T> void delete(Class<T> clazz, long id) throws Exception {
		this.session.open();
		this.session.delete(clazz,id);
		this.session.close();
	}
	@Override
	public <T> void deleteBatch(Class<T> clazz,String ids) throws Exception{
		this.session.open();
		this.session.deleteBatch(clazz, ids.split(",+"));
		this.session.close();
	}
	@Override
	public <T> T queryForObject(Class<T> clazz, long id) throws Exception {
		this.session.open();
		T t = this.session.queryForObject(clazz,id);
		this.session.close();
		return t;
	}
	
	@Override
	public <T> T queryForObject(Class<T> clazz, String sql) throws Exception {
		this.session.open();
		T t = this.session.queryForObject(clazz,sql);
		this.session.close();
		return t;
	}
	@Override
	public void executeUpdate(String sql) throws Exception {
		this.session.open();
		this.session.executeUpdate(sql);
		this.session.close();
	}
	
	@Override
	public <T> PagerModel<T> queryForPagerModel(String sql, int offset, int size,
			Class<T> clazz) throws Exception {
		this.session.open();
		String countSql = sql.replaceAll("select\\s*.*? from", "select count(*) from")
		.replaceAll("\\s?order\\s+?by.+", "");
		if(offset < 0){
			offset = 0;
		}
		PagerModel<T> pager = new PagerModel<T>();
		pager.setItems(this.session.queryForList(sql,offset,size,clazz));
		pager.setTotal(this.getCount(countSql));
		pager.setCurpage(offset/size + 1);
		pager.setOffset(offset);
		this.session.close();
		return pager;
	}
	
	@Override
	public int getCount(String sql) throws Exception {
		this.session.open();
		int value = (Integer)this.session.queryForObject(Integer.class,sql);
		this.session.close();
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