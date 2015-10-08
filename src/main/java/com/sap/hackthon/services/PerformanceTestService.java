package com.sap.hackthon.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sap.hackthon.entity.OrderLines;
import com.sap.hackthon.entity.Orders;

@Service
@Transactional
public class PerformanceTestService {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    private long addOneRandomOrder() {

        Orders o = new Orders();
        OrderLines l = new OrderLines();
        l.setProductName("iphone 6s");
        int price = getRandom(6000);
        int count = getRandom(10);
        int total = price * count;
        l.setPrice(price);
        l.setCount(count);
        l.setLineTotal(total);
        l.setOrder(o);

        OrderLines l2 = new OrderLines();
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
        o.setTax((int) ((total + total2) * 0.17));
        entityManager.persist(o);
        return o.getId();
    }

    private int getRandom(int max) {
        Random a = new Random(new Date().getTime());
        return a.nextInt(max);
    }

    public Long addRandomOrder() {
        try {
            return this.addOneRandomOrder();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addOrdersInThreads(final ApplicationContext context) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(100);
        List<ScheduledFuture> fs = new ArrayList<ScheduledFuture>();
        for (int i = 0; i < 100; i++) {
            String worker = "worker" + i;
            Thread t = new MyThread(context, worker);
            long period = this.getRandom(5);
            ScheduledFuture f = executor.scheduleAtFixedRate(t, 1, 2, TimeUnit.SECONDS);
            fs.add(f);
        }

    }

    public class MyThread extends Thread {

        final ApplicationContext context;
        final String workerName;

        public MyThread(ApplicationContext context, String workerName) {
            this.context = context;
            this.workerName = workerName;
        }

        @Override
        public void run() {
            PerformanceTestService service = context.getBean(PerformanceTestService.class);
            Date from = new Date();
            Long id = service.addRandomOrder();
            Date to = new Date();
            Long duration = to.getTime() - from.getTime();
            if (id != null) {
                System.out.println(workerName + ", order(" + id + ") is added successfully in " + duration);
            } else {
                System.out.println(workerName + ", failed to added order in " + duration);
            }
        }
    }
}
