package com.sap.hackthon.services;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sap.hackthon.entity.OrderLines;
import com.sap.hackthon.entity.Orders;

@Service
@Transactional
public class PerformanceTestService {

	@PersistenceContext
	EntityManager entityManager;

	@Transactional
	public void addOneRandomOrder() {
		
		Orders o = new Orders();
		OrderLines l= new OrderLines();
		l.setProductName("iphone 6s");
		int price = getRandom(6000);
		int count = getRandom(10);
		int total = price * count;
		l.setPrice(price);
		l.setCount(count);
		l.setLineTotal(total);
		l.setOrder(o);
		
		OrderLines l2= new OrderLines();
		l2.setProductName("iphone 6s");
		int price2 = getRandom(6000);
		int count2 = getRandom(10);
		int total2 = price * count;
		l2.setPrice(price2);
		l2.setCount(count2);
		l2.setLineTotal(total2);
		l2.setOrder(o);

		o.getLines().add(l);
		o.getLines().add(l2);
		o.setAddress("test address");
		o.setCustomerName("hou changjun");
		o.setTotal(total + total2);
		o.setTax((int)((total + total2) * 0.17));
		
		entityManager.persist(o);
		System.out.println("order(" + o.getId() + ") is added successfully");
	}
	
	private int getRandom(int max) {
		Random a = new Random(123);
		return a.nextInt(max);
	}
	
}
