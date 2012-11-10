package coffee.cms.admin.action;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import coffee.cms.admin.bean.TigNodeBean;
import coffee.cms.admin.bean.TigUserBean;
import coffee.cms.core.action.Action;
import coffee.database.Pager;
import coffee.database.Session;
import coffee.database.core.DBUtils;
import coffee.servlet.ParameterReflect;

/**
 * 对应 tig_user表的操作
 * 
 * @author coffee 20122012-11-9下午3:42:10
 */
public class TigUserAction extends Action {

	public TigUserAction() {
		super.modelClass = TigUserBean.class;
		super.tableName = DBUtils.getTableName(TigUserBean.class);
	}

	@Override
	public void delete(HttpServletRequest request) {
		String sid = request.getParameter("sid");
		Session session = new Session();
		try {
			session.open();
			String[] ids = sid.split(",");

			String sql = "delete from tig_nodes where uid in (" + sid + ")";
			
			if (ids.length > 1) {
				session.deleteBatch(ids, TigNodeBean.class);
				session.deleteBatch(ids, modelClass);
			} else {
				//删除 TigNodeBean
				session.executeUpdate(sql);
				session.delete(Long.valueOf(sid), modelClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/**
	 * 新增记录
	 * 
	 * @param request
	 * @param modelClass
	 */
	public void insert(HttpServletRequest request) {
		TigUserBean model = (TigUserBean) new ParameterReflect().invoke(
				request, modelClass);
		Session session = new Session();
		try {
			session.open();
			model.setUserId(model.getUserId());
			String uid = session.insert(model,true);
			
			TigNodeBean tigNode = new TigNodeBean();
			tigNode.setUid(Integer.valueOf(uid));
			session.insert(tigNode);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
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
			Pager<TigUserBean> pager = session.queryForPager(sql, 0, 10,
					TigUserBean.class);
			request.setAttribute("pager", pager);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		request.setAttribute("jid", jid);
	}
}
