package com.novopay.in.demo.resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.service.UserAuthenticationService;
import com.novopay.in.demo.service.WalletTransactionServiceImpl;

@RestController
@RequestMapping("/novopay")
public class UserAuthenticationResource {
	
	@Autowired
	private UserAuthenticationService userAuthenticationService;
	
	private static final String LOGINSUCCESSMSG="Login Successful";
	private static final String SIGNUPSUCCESSMSG="user has been added successfully";
	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationService.class);
	
	@GetMapping("/signin")
	public ResponseEntity<String> signIn(@RequestParam String userName,@RequestParam String password,HttpServletRequest req){
		User user=new User();
		user.setUserName(userName);
		user.setPassword(password);
		LOGGER.info("getting session");
		HttpSession  session=req.getSession();
		if(userAuthenticationService.signIn(user).equals(LOGINSUCCESSMSG)) {
			LOGGER.info("setting session");
			session.setAttribute("USER", user);
			return new ResponseEntity<>(LOGINSUCCESSMSG,HttpStatus.OK);
		}else {
			session.invalidate();
		}
		
		
		return new ResponseEntity<>(userAuthenticationService.signIn(user),HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody User user){
		
		if(userAuthenticationService.signUp(user).equals(SIGNUPSUCCESSMSG)) {
			LOGGER.debug(SIGNUPSUCCESSMSG);
			return new ResponseEntity<>(SIGNUPSUCCESSMSG,HttpStatus.OK);
		}
			
		
		return new ResponseEntity<>("Some error occurred!!!!",HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
