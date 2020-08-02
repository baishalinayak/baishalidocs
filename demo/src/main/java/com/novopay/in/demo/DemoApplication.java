package com.novopay.in.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")

@SpringBootApplication(scanBasePackages = {"com.novopay"})
public class DemoApplication implements CommandLineRunner {
	
	private  static final  Logger LOGGER=LoggerFactory.getLogger(DemoApplication.class);
	
	

	public static void main(String[] args) {
		
		SpringApplication.run(DemoApplication.class, args);
	}
	
	

	public void run(String... args) throws Exception {
		
		LOGGER.info("application is running ");
		
		
	}

}
