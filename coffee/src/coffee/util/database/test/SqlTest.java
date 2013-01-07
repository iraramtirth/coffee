package coffee.util.database.test;

import java.sql.SQLException;

import coffee.util.database.Session;
import coffee.util.database.core.DBUtils;

public class SqlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// createTableSql();

		UserBean user = new UserBean();
		user.setUsername("coffee");
		user.setPassword("iop");
		insert(user);
	}

	public static void createTableSql() {
		String sql = DBUtils.generateTableSql(UserBean.class);

		System.out.println(sql);
	}

	public static <T> void insert(T obj) {
		try {
			Session session = new Session();
			session.insert(obj);
			session.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
