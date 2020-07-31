package com.novopay.in.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.service.UserAuthenticationService;
import com.novopay.in.demo.service.WalletTransactionService;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner{
	
	@Autowired
	UserAuthenticationService signInSignUpService;
	
	@Autowired
	WalletTransactionService walletTransactionService;

	public static void main(String[] args) {
		
		SpringApplication.run(DemoApplication.class, args);
	}

	public void run(String... args) throws Exception {
		
		User user=new User();
		user.setPassword("Baishali");
		user.setUserName("Baishali6");
		//user.setFirstName("Baishali123");
		//user.setLastName("nayak123");
		//user.setPhoneNumber(1234567891);
		
		//signInSignUpService.signIn(user);
		walletTransactionService.addMoney(user, 100);
		
		// TODO Auto-generated method stub
		
	}

}
