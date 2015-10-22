package com.sap.hackthon.services;

import java.text.SimpleDateFormat;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.sap.hackthon.entity.DynamicEntity;
import com.sap.hackthon.entity.PropertyMeta;
import com.sap.hackthon.enumeration.UDFTypeEnum;
import com.sap.hackthon.utils.GlobalConstants;

@Service
@Transactional
public class UDFPerformanceTestService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private PropertyMetaService metaService; 
    
    @Autowired
    private EntityService service;
    
    @Transactional
    private String addOneRandomOrder() {
    	DynamicEntity entity=new DynamicEntity();
    	String tenantId="Tenant004";
    	entity.setObjectType("T_ORDER");
    	String randomNum1=getRandom(9999)+"";
    	String randomNum2=getRandom(9999)+"";
    	String randomNum3=getRandom(9999)+"";
    	String orderID="Order"+randomNum1+randomNum2+randomNum3;
    	entity.setProperty("ORDER_ID", "Order012000142199");
    	entity.setProperty("TENANT_ID", tenantId);
    	entity.setProperty("ORDER_DATE", "2015-09-25");
    	
    	DynamicEntity rtn=service.create(entity, tenantId);
        return rtn.getProperty("ID").toString();
    }

    @Transactional
    private boolean alterTable() {
    	//PropertyMetaService metaService=context.getBean(PropertyMetaService.class);
    	PropertyMeta propertyMeta=new PropertyMeta();
    	String tenantId="Tenant004";
		propertyMeta.setTenantId(tenantId);
		propertyMeta.setObjectName("T_ORDER");
		propertyMeta.setType(UDFTypeEnum.NVARCHAR);
		int nextParamIndex = metaService.getMaxParamIndexByTenantIdAndObjectNameAndType(tenantId, propertyMeta.getObjectName(), propertyMeta.getType()) + 1;
//		int nextParamIndex = propertyMetaRepository.findMaxParamIndexByTenantIdAndObjectNameAndType(tenantId, propertyMeta.getObjectName(), propertyMeta.getType()) + 1;
		String internalName = GlobalConstants.UDF + "_" + propertyMeta.getTenantId() + "_" + propertyMeta.getType() + "_" + nextParamIndex;
		propertyMeta.setParamIndex(nextParamIndex);
    	propertyMeta.setDisplayName(internalName);
		propertyMeta.setInternalName(internalName);
		boolean rslt=metaService.create(propertyMeta);
		return rslt;
    }

    private int getRandom(int max) {
        Random a = new Random(new Date().getTime());
        return a.nextInt(max);
    }

    public boolean alterTableM() {
        try {
            return this.alterTable();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String addRandomOrder() {
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
//        for (int i = 0; i < 1; i++) {
//            String worker = "worker" + i;
//            Thread t = new MyThread(context, worker);
//            long period = this.getRandom(5);
//            ScheduledFuture f = executor.scheduleAtFixedRate(t, 1, 2, TimeUnit.SECONDS);
//            fs.add(f);
//        }
        Thread ddlT = new DDLThread(context);
        ScheduledFuture ddlF = executor.scheduleAtFixedRate(ddlT, 1, 40, TimeUnit.SECONDS);
        fs.add(ddlF);
    }
    
//    public class MyThread extends Thread {
//
//        final ApplicationContext context;
//        final String workerName;
//
//        public MyThread(ApplicationContext context, String workerName) {
//            this.context = context;
//            this.workerName = workerName;
//        }
//
//        @Override
//        public void run() {
//            UDFPerformanceTestService service = context.getBean(UDFPerformanceTestService.class);
//            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
//            SimpleDateFormat format = new SimpleDateFormat(pattern);
//            
//            Date from = new Date();
//            String id = service.addRandomOrder();
//            Date to = new Date();
//            Long duration = to.getTime() - from.getTime();
//            if (id != null) {
//                System.out.println(workerName + ", order(" + id + "), "+format.format(from)+", "+format.format(to)+", "+ duration);
//                //System.out.println(workerName + ", order(" + id + ") is added successfully in " + duration);
//            } else {
//                System.out.println(workerName + ", failed to added order in " + duration);
//            }
//        }
//    }

    public class DDLThread extends Thread {

        final ApplicationContext context;
        final String workerName="DDL";

        public DDLThread(ApplicationContext context) {
            this.context = context;
        }

        @Override
        public void run() {
            UDFPerformanceTestService service = context.getBean(UDFPerformanceTestService.class);
            String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            
            Date from = new Date();
            boolean isSuccess = service.alterTable();
            Date to = new Date();
            Long duration = to.getTime() - from.getTime();
            if (isSuccess) {
                System.out.println(workerName + ", " + isSuccess + ", "+format.format(from)+", "+format.format(to)+", "+ duration);
            } else {
                System.out.println(workerName + ", failed to added order in " + duration);
            }
        }
    }
}
