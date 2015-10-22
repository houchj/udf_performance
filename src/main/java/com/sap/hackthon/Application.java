package com.sap.hackthon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.sap.hackthon.services.UDFPerformanceTestService;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySources({
	@PropertySource("classpath:application.properties"),
	@PropertySource("classpath:application_${app.env}.properties")
})
public class Application {

	public static void main(String[] args) {
		/**
		 * by default, we adopt local configuration
		 * in PROD env, we will setup app.env with -D option while running the Application.
		 */
		String env = System.getProperty("app.env");
		if(env == null || env.equals("")) {
			System.setProperty("app.env", "local");
		}
		
		SpringApplication.run(Application.class, args);			
//		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
//		
//		System.out.println("system initialized!");
//		
//		UDFPerformanceTestService test = context.getBean(UDFPerformanceTestService.class);
//		//PerformanceTestService test = context.getBean(PerformanceTestService.class);
//		//test.addRandomOrder();
//		test.addOrdersInThreads(context);
	}

}