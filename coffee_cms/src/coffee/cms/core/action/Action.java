package coffee.cms.core.action;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import coffee.database.Session;
import coffee.servlet.ParameterReflect;

public class Action {

	protected void query(HttpServletRequest request) {

	}

	/**
	 * 新增记录
	 * @param request
	 * @param modelClass
	 */
	protected <T> void insert(HttpServletRequest request, Class<T> modelClass) {
		T model = new ParameterReflect().invoke(request, modelClass);
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

	protected void update(HttpServletRequest request) {

	}

	protected void delete(HttpServletRequest request) {

	}
}
