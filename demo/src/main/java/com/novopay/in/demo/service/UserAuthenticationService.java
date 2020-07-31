package com.novopay.in.demo.service;

import com.novopay.in.demo.bean.User;

public interface UserAuthenticationService {

	public String signUp(User user);
	
	public String signIn(User user);
}
