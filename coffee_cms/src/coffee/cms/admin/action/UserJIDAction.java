package coffee.cms.admin.action;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import coffee.cms.admin.bean.UserJIDBean;
import coffee.cms.core.action.Action;
import coffee.database.Pager;
import coffee.database.Session;
import coffee.database.core.DBUtils;

/**
 * 
 * @author coffee
 * 
 */
public class UserJIDAction extends Action {

	public UserJIDAction() {
		super.tableName = DBUtils.getTableName(UserJIDBean.class);
	}

	@Override
	public void query(HttpServletRequest request) {
		String jid = request.getParameter("jid");
		String sql = "select * from " + super.tableName + " where true";
		if (jid != null) {
			sql += " and jid like '%" + jid + "%' ";
		}
		Session session = new Session();
		session.open();
		try {
			Pager<UserJIDBean> pager = session.queryForPager(sql, 0, 10,
					UserJIDBean.class);
			request.setAttribute("pager", pager);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("jid", jid);
	}
}
