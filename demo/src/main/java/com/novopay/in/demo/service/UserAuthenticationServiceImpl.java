package com.novopay.in.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.repository.UserAuthenticationRepository;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService{
	
	@Autowired
	UserAuthenticationRepository userAuthenticationRepository;
	
	public String signUp(User user) {
		return userAuthenticationRepository.signUp(user);
	}
	
	public String signIn(User user) {
		return userAuthenticationRepository.signIn(user);
	}

}
