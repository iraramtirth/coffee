package coffee.database.test;

import coffee.jdbc.table.TableCreator;

public class SqlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sql = TableCreator.generateTableSql(UserBean.class);
		
		System.out.println(sql);
	}

}
