package org.coffee.jdbc.service.impl;

import java.sql.SQLException;

import org.coffee.jdbc.SqlConnection;
import org.coffee.jdbc.dao.impl.TDaoImpl;
import org.coffee.jdbc.service.TService;
import org.coffee.spring.ioc.annotation.Resource;
import org.coffee.spring.ioc.annotation.Service;
import org.coffee.struts.PagerModel;
/**
 * service层通用父类接口实现类
 * @author wangtao
 */
@Service(name="service")
public class Session extends TDaoImpl implements TService {

	private Session session;

	 
	@Resource(name="session")
	public void setSession(Session session) {
		this.session = session;
	}
	public void open(){
		super.conn = new SqlConnection().getConnection();
	}
	// 关闭Connection
	public void close(){
		try {
			super.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 *  分页查询
	 */
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
		pager.setTotal(this.queryForColumn(Integer.class, countSql));
		pager.setCurpage(offset/size + 1);
		pager.setOffset(offset);
		this.session.close();
		return pager;
	}

}