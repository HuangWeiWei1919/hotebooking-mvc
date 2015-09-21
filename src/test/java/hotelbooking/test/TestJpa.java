package hotelbooking.test;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.webflow.samples.booking.Hotel;
import org.springframework.webflow.samples.booking.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:data-access-config.xml"})
public class TestJpa {
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	@Test
	public void save() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		entityManager.getTransaction().begin();
		User user = new User();
		user.setName("new Name");
		user.setPassword("old password");
		user.setUsername("new username");
		
		entityManager.persist(user);
		
		Query query = entityManager.createQuery("select u from User u where u.name = :name");
		query.setParameter("name", "new Name");
		@SuppressWarnings("unchecked")
		List<User> list = query.getResultList();
		for(User u : list) {
			System.out.println(u);
		}
		
		
	}
	
	@Test
	public void update() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		//只取一个
		User u = entityManager.find(User.class, "scott");
		
		entityManager.getTransaction().begin();
		
		u.setName("uuuuu");
		entityManager.getTransaction().commit();
		Query query = entityManager.createQuery("select u  from User u");

		@SuppressWarnings("unchecked")
		List<User> list = query.getResultList();
		for(User user : list) {
			System.out.println(user);
		}
		
	}
	
	@Test
	public void update2() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		User user = entityManager.find(User.class, "scott");
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		user.setName("new Name");
		User u = entityManager.merge(user);
		System.out.println(u);
		
		ts.commit();
		
		Query query = entityManager.createQuery("select u  from User u");

		@SuppressWarnings("unchecked")
		List<User> list = query.getResultList();
		for(User uu : list) {
			System.out.println(uu);
		}
	}
	
	@Test
	public void remove() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		User user = entityManager.find(User.class, "scott");
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		entityManager.remove(user);
		ts.commit();
		
		Query query = entityManager.createQuery("select u  from User u");
		@SuppressWarnings("unchecked")
		List<User> list = query.getResultList();
		for(User uu : list) {
			System.out.println(uu);
		}
		
	}
	
	@Test
	public void remove2() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		User user = entityManager.find(User.class, "scott");
		EntityTransaction ts = entityManager.getTransaction();
		ts.begin();
		entityManager.detach(user);
		ts.commit();
		
		Query query = entityManager.createQuery("select u  from User u");
		@SuppressWarnings("unchecked")
		List<User> list = query.getResultList();
		for(User uu : list) {
			System.out.println(uu);
		}
		
	}
	
	
	
	@Test
	public void queryOneByPrimaryKey() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Hotel hotel = entityManager.find(Hotel.class, 1l);
		System.out.println(hotel);
			
	}
	
	@Test
	public void queryList() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		System.out.println("-----------不带参数查询---------");
		Query query = entityManager.createQuery("select u  from User u");
		@SuppressWarnings("unchecked")
		List<User> list = query.getResultList();
		for(User u : list) {
			System.out.println(u);
		}
		
		System.out.println("--------带参数查询----------");
		query = entityManager.createQuery("select h from Hotel h where h.price >= :price");
		query.setParameter("price", new BigDecimal(300));
		@SuppressWarnings("unchecked")
		List<Hotel> hotelList = query.getResultList();
		for(Hotel h : hotelList) {
			System.out.println(h);
		}
		
		System.out.println("--------命名查询，即在实体类上就先定义好查询语句--------");
		query = entityManager.createNamedQuery("getUser");
		query.setParameter("name", "Scott");
		@SuppressWarnings("unchecked")
		List<User> list2 = query.getResultList();
		for(User u : list2) {
			System.out.println(u);
		}
		
		query = entityManager.createNamedQuery("getHotel");
		query.setParameter("price", new BigDecimal(400));
		@SuppressWarnings("unchecked")
		List<Hotel> hotelList2 = query.getResultList();
		for(Hotel h : hotelList2) {
			System.out.println(h);
		}
		
		query = entityManager.createNamedQuery("getHotel2");
		query.setParameter("name", "%" + "Hilton" + "%");
		@SuppressWarnings("unchecked")
		List<Hotel> hotelList3 = query.getResultList();
		for(Hotel h : hotelList3) {
			System.out.println(h);
		}
	}
	
	@Test
	public void queryPageAndOrder() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		
		Query query = entityManager.createQuery("select h from Hotel h order by h.id desc");
		int maxResult = 10;
		int size = query.getResultList().size();
		int n = size % 10;
		int maxPage = n == 0 ?  size / maxResult : size / maxResult + 1;
		
		System.out.println("-----------分页排序查询---------");
		for(int i = 1; i <= maxPage; i++) {
			System.out.println("第" + i + "页");
			query.setFirstResult(( i - 1) * maxResult);
			query.setMaxResults(i * maxResult);
			@SuppressWarnings("unchecked")
			List<Hotel> hotelList = query.getResultList();
			for(Hotel h : hotelList) {
				System.out.println(h);
			}
			
		}
		
	}

}
