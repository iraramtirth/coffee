package coffee.database.test;

import coffee.database.core.DBUtils;

public class SqlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sql = DBUtils.generateTableSql(UserBean.class);
		
		System.out.println(sql);
	}

}
