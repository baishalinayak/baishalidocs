package com.novopay.in.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.repository.UserAuthenticationRepository;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);
	@Autowired
	UserAuthenticationRepository userAuthenticationRepository;

	public String signUp(User user) {
		LOGGER.info("sign up service");
		return userAuthenticationRepository.signUp(user);
	}

	public String signIn(User user) {
		LOGGER.info("sign in service");
		return userAuthenticationRepository.signIn(user);
	}

}
