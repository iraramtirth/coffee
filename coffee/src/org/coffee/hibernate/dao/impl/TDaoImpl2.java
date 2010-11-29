package org.coffee.hibernate.dao.impl;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.coffee.hibernate.SqlConnection;
import org.coffee.hibernate.dao.TDao;

import com.sun.rowset.CachedRowSetImpl;

/**
 * 系统DAO的公共/通用父类接口 实现类
 * 
 * @author wangtao
 * 
 * @version 2.0
 */
public class TDaoImpl2 implements TDao {

	private Connection conn;

	public TDaoImpl2() {
		conn = new SqlConnection().getConnection();
	}

	/**
	 * 删除记录；该实体中必须含有ID主键；且主键不为null
	 * @throws SQLException
	 */
	@Override
	public <T> void delete(T t) throws SQLException {
		try {
			BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			Object id = null;
			for (PropertyDescriptor p : props) {
				if (p.getName().equals("id")) {
					id = p.getReadMethod().invoke(t, (Object[]) null);
					break;
				}
			}
			String sql = "delete from "
					+ t.getClass().getSimpleName().toLowerCase()
					+ " where id = " + Integer.valueOf(id.toString());
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 删除实体
	 */
	@Override
	public <T> void delete(Class<T> clazz, long id) throws SQLException {
		try {
			String sql = "delete from " + clazz.getSimpleName().toLowerCase()
					+ " where id = " + id;
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *  插入实体
	 */
	@Override
	public <T> void insert(T t) throws SQLException {
		StringBuffer sql = new StringBuffer("insert into ").append(
				t.getClass().getSimpleName().toLowerCase()).append(" ");
		try {
			BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			sql.append(" (");
			for (int i = 0; i < props.length; i++) {
				sql.append(props[i].getName());
				if (i + 1 < props.length) {
					sql.append(",");
				}
			}
			sql.append(") values (");
			for (int i = 0; i < props.length; i++) {
				try {
					if ("int".equals(props[i].getPropertyType()
							.getCanonicalName())
							|| "long".equals(props[i].getPropertyType()
									.getCanonicalName())) {
						// 处理 ID 主键
						if ("id".equals(props[i].getName())) {
							sql.append("null");
						} else {
							sql.append(props[i].getReadMethod().invoke(t,
									(Object[]) null));
						}
					} else if (props[i].getPropertyType().getCanonicalName()
							.endsWith("Date")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String value = "";
						try {
							value = sdf.format(props[i].getReadMethod().invoke(
									t, (Object[]) null));
						} catch (Exception e) {
							value = "null";
						}// 如果时间为空
						if ("null".equals(value)) {
							sql.append(value);
						} else {
							sql.append(" '").append(value).append("' ");
						}
					} else {
						Object value = props[i].getReadMethod().invoke(t,
								(Object[]) null);
						if (value == null) {
							sql.append("null");
						} else {
							sql.append(" '").append(value.toString()).append(
									"' ");
						}
					}
					if (i + 1 < props.length) {
						sql.append(",");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			sql.append(" )");
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(sql.toString());
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> void update(T t) throws SQLException {
		StringBuffer sql = new StringBuffer("update ").append(
				t.getClass().getSimpleName().toLowerCase()).append(" set ");
		long id = 0;
		try {
			BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			for (int i = 0; i < props.length; i++) {
				try {
					Object value = null;
					if (props[i].getPropertyType().getCanonicalName().endsWith(
							"Integer")
							|| props[i].getPropertyType().getCanonicalName()
									.endsWith("Long")) {
						// 处理 ID 主键
						if ("id".equals(props[i].getName())) {
							id = Long.valueOf(props[i].getReadMethod().invoke(
									t, (Object[]) null).toString());
						} else {
							value = props[i].getReadMethod().invoke(t,
									(Object[]) null);
							if (value != null) {
								sql.append(props[i].getName()).append(" = ")
										.append(value);
							} else {
								continue;
							}
						}
					} else if (props[i].getPropertyType().getCanonicalName()
							.endsWith("Date")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						try {
							value = sdf.format(props[i].getReadMethod().invoke(
									t, (Object[]) null));
						} catch (Exception e) {
							value = "null";
						}
						if ("null".equals(value) || null == value) {
							continue;
						} else {
							sql.append(props[i].getName()).append(" = '")
									.append(value).append("' ");
						}
					} else {
						value = props[i].getReadMethod().invoke(t,
								(Object[]) null);
						if (value == null) {
							continue;
						} else {
							sql.append(props[i].getName()).append(" = '")
									.append(value.toString()).append("' ");
						}
					}
					if (value != null && i + 1 < props.length) {
						sql.append(",");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String str = sql.toString().trim();
			// 出去去 末尾的 ,
			while (str.trim().endsWith(",")) {
				str = str.substring(0, str.length() - 1);
			}
			str += " where id = " + id;
			System.out.println(str);
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(str);
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 *  返回离线数据集
	 */
	@Override
	public CachedRowSet queryForResultSet(String sql) throws SQLException {
		CachedRowSet crs = new CachedRowSetImpl();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			crs.populate(rs);
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return crs;
	}
	/**
	 *  查询总记录数
	 *  sql = "select count(*) from ..."
	 */
	@Override
	public int queryForCount(String sql) throws SQLException {
		int count = 0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	/**
	 * 查询单个实体
	 * id = t.getId()
	 */
	@Override
	public <T> T queryForobject(T t) throws SQLException {
		try {
			BeanInfo bi = Introspector.getBeanInfo(t.getClass(), Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			Object id = null;
			for (PropertyDescriptor p : props) {
				if (p.getName().equals("id")) {
					id = p.getReadMethod().invoke(t, (Object[]) null);
				}
			}
			String sql = "select * from "
					+ t.getClass().getSimpleName().toLowerCase()
					+ " where id = " + Integer.valueOf(id.toString());
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				for (PropertyDescriptor p : props) {
					p.getWriteMethod().invoke(t,
							new Object[] { rs.getObject(p.getName()) });
				}
			}
			rs.close();
			stmt.close();
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 查询单个实体
	 */
	@Override
	public <T> T queryForobject(Class<T> clazz, long id) throws SQLException {
		try {
			BeanInfo bi = Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			String sql = "select * from " + clazz.getSimpleName().toLowerCase()
					+ " where id = " + id;
			Statement stmt = conn.createStatement();
			System.out.println(sql);
			ResultSet rs = stmt.executeQuery(sql);
			T t = clazz.newInstance();
			if (rs.next()) {
				for (PropertyDescriptor p : props) {
					p.getWriteMethod().invoke(t,
							new Object[] { rs.getObject(p.getName()) });
				}
			}
			rs.close();
			stmt.close();
			return t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *  查询返回list
	 */
	@Override
	public <T> List<T> queryForList(String sql, Class<T> clazz)
			throws SQLException {
		List<T> ls = new ArrayList<T>();
		try {
			BeanInfo bi = Introspector.getBeanInfo(clazz, Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				T tt = clazz.newInstance();
				for (PropertyDescriptor p : props) {
					try {
						p.getWriteMethod().invoke(tt,
								new Object[] { rs.getObject(p.getName()) });
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				ls.add(tt);
			}
			rs.close();
			stmt.close();
			return ls;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 执行指定sql语句
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int executeUpdate(String sql) throws SQLException {
		try {
			Statement stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 关闭数据库连接
	 */
	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection(){
		return conn;
	}
	public static void main(String[] args) throws Exception {
		String sql = "select * from activities";
		TDaoImpl2 tdao = new TDaoImpl2();
		CachedRowSet rs = tdao.queryForResultSet(sql);
		tdao.conn.setAutoCommit(false);
		if(rs.next()){
			System.out.println(rs.getString(2));
			rs.updateString(2, "sddssd");
			rs.updateRow();
		}
		rs.acceptChanges(tdao.getConnection());
		rs.close();
	}

}
