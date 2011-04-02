package com.wangtao.test.jdbc.service;

import java.sql.SQLException;

import org.coffee.jdbc.Session;

import com.wangtao.test.jdbc.bean.User;


public class UserService {
	public static void main(String[] args) {
		Session session = new Session();
		session.open();
		User user = new User();
		user.setUsername("test_name");
		user.setPassword("test_pwd");
		try {
			session.insert(user);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			session.close();
		}
	}
}
