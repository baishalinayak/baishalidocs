package com.novopay.in.demo.repository;

import com.novopay.in.demo.bean.User;

public interface UserAuthenticationRepository {
	
	public String signUp(User user) ;
	public String signIn(User user);

}
