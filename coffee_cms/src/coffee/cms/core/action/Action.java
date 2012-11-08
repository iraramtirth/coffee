package coffee.cms.core.action;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import coffee.database.Session;
import coffee.servlet.ParameterReflect;

public class Action {

	protected String tableName;
	protected Class<?> modelClass;

	public void query(HttpServletRequest request) {

	}

	/**
	 * 新增记录
	 * 
	 * @param request
	 * @param modelClass
	 */
	public void insert(HttpServletRequest request) {
		Object model = new ParameterReflect().invoke(request, modelClass);
		Session session = new Session();
		try {
			session.open();
			session.insert(model);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public  void toUpdate(HttpServletRequest request) {
		String sid = request.getParameter("sid");
		Session session = new Session();
		try {
			session.open();
			Object model = session.queryForEntity(Long.valueOf(sid), modelClass);
			request.setAttribute("item", model);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	public void update(HttpServletRequest request) {
		Object model = new ParameterReflect().invoke(request, modelClass);
		Session session = new Session();
		try {
			session.open();
			session.update(model);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/**
	 * @param request
	 * @param modelClass
	 */
	public void delete(HttpServletRequest request) {
		String sid = request.getParameter("sid");
		Session session = new Session();
		try {
			session.open();
			String[] ids = sid.split(",");
			if (ids.length > 1) {
				session.deleteBatch(ids, modelClass);
			} else {
				session.delete(Long.valueOf(sid), modelClass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/**
	 * 以下是工具类
	 */
	public boolean isEmpty(String str) {
		if (str != null && str.trim().length() > 0) {
			return true;
		}
		return false;
	}
}
