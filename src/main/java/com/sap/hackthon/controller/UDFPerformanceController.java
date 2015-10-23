package com.sap.hackthon.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.hackthon.entity.DynamicEntity;
import com.sap.hackthon.entity.PropertyMeta;
import com.sap.hackthon.enumeration.UDFTypeEnum;
import com.sap.hackthon.services.EntityService;
import com.sap.hackthon.services.PropertyMetaService;
import com.sap.hackthon.services.UDFPerformanceTestService;
import com.sap.hackthon.services.UDFPerformanceTestService.DDLThread;
import com.sap.hackthon.utils.GlobalConstants;

@Controller
public class UDFPerformanceController {

	@Autowired
    private EntityService entityService;
	
    @Autowired
    private PropertyMetaService metaService; 

    @RequestMapping(method = RequestMethod.GET, value = "start")
    public @ResponseBody void start(HttpServletRequest request)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ScheduledFuture> fs=startThreads();
        session.setAttribute("threads", fs);
    }

    @RequestMapping(method = RequestMethod.GET, value = "stop")
    public @ResponseBody void stop(HttpServletRequest request)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<ScheduledFuture> fs = (List<ScheduledFuture>) session.getAttribute("threads");
        for (ScheduledFuture scheduledFuture : fs) {
			scheduledFuture.cancel(false);
		}
    }

    public List<ScheduledFuture> startThreads() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(100);
        List<ScheduledFuture> fs = new ArrayList<ScheduledFuture>();
        Thread ddlT = new DDLThread();
        ScheduledFuture ddlF = executor.scheduleAtFixedRate(ddlT, 1, 2, TimeUnit.SECONDS);
        fs.add(ddlF);
        for (int i = 0; i < 100; i++) {
            String worker = "worker" + i;
            Thread t = new WorkThread(worker);
            ScheduledFuture f = executor.scheduleAtFixedRate(t, 1, 1, TimeUnit.SECONDS);
            fs.add(f);
        }
        return fs;
    }
    
    
class WorkThread extends Thread {
    final String workerName;

      public WorkThread(String workerName) {
          this.workerName = workerName;
      }

      @Override
      public void run() {
          String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
          SimpleDateFormat format = new SimpleDateFormat(pattern);
          
          Date from = new Date();
//          work();
//          String id = "1";
          String id = addOneRandomOrder();
          Date to = new Date();
          Long duration = to.getTime() - from.getTime();
          if (id != null) {
              System.out.println(workerName + ", order(" + id + "), "+format.format(from)+", "+format.format(to)+", "+ duration);
              //System.out.println(workerName + ", order(" + id + ") is added successfully in " + duration);
          } else {
              System.out.println(workerName + ", fail, "+format.format(from)+", "+format.format(to)+", "+ duration);
//              System.out.println(workerName + ", failed to added order in " + duration);
          }
      }
  }

  private void work(){
	  System.out.println("dddd");
  }

  private String addOneRandomOrder() {
  	DynamicEntity entity=new DynamicEntity();
  	String tenantId="Tenant004";
  	entity.setObjectType("T_ORDER");
//  	String randomNum1=getRandom(9999)+"";
//  	String randomNum2=getRandom(9999)+"";
//  	String randomNum3=getRandom(9999)+"";
//  	String orderID="Order"+randomNum1+randomNum2+randomNum3;
  	entity.setProperty("ORDER_ID", "Order012000142199");
  	entity.setProperty("TENANT_ID", tenantId);
  	entity.setProperty("ORDER_DATE", "2015-09-25");
  	
  	try {
		DynamicEntity rtn=entityService.create(entity, tenantId);
		return rtn.getProperty("ID").toString();
	} catch (Exception e) {
		return null;
	}
  }

  public class DDLThread extends Thread {

      final String workerName="DDL";

      public DDLThread() {
      }

      @Override
      public void run() {
          String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
          SimpleDateFormat format = new SimpleDateFormat(pattern);
          
          Date from = new Date();
          boolean isSuccess = alterTable();
          Date to = new Date();
          Long duration = to.getTime() - from.getTime();
          if (isSuccess) {
              System.out.println(workerName + ", " + isSuccess + ", "+format.format(from)+", "+format.format(to)+", "+ duration);
          } else {
              System.out.println(workerName + ", " + isSuccess + ", "+format.format(from)+", "+format.format(to)+", "+ duration);
//              System.out.println(workerName + ", failed to added order in " + duration);
          }
      }
  }

  @Transactional
  private boolean alterTable() {
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
		try {
			boolean rslt=metaService.create(propertyMeta);
			return rslt;
		} catch (Exception e) {
			return false;
		}
  }

  
}
