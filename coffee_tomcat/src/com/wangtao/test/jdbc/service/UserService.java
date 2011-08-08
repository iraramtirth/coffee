package com.wangtao.test.jdbc.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.coffee.jdbc.Session;

import com.wangtao.test.jdbc.bean.User;


public class UserService {
	public static void main(String[] args) {
		Session session = new Session();
		session.open();
		String sql = "select username from users";
		try {
			List<User> usersList = new ArrayList<User>();
			usersList = session.queryForList(sql, User.class);
			for(User user : usersList){
				System.out.println(user.getUsername());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			session.close();
		}
	}
}
