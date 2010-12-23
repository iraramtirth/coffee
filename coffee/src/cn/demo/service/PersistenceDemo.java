package cn.demo.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import cn.demo.bean.User;

public class PersistenceDemo {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
		System.out.println(emf);
		EntityManager em = emf.createEntityManager();
		System.out.println(em);
		em.getTransaction().begin();
		List<User> ls = em.createNativeQuery("select * from user",User.class).getResultList();
		System.out.println(ls.size());
		em.getTransaction().commit();
	}
}
