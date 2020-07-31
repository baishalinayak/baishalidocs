package com.novopay.in.demo.service;

import org.springframework.stereotype.Service;

import com.novopay.in.demo.bean.User;

@Service
public interface WalletTransactionService {
	
	
	public String addMoney( User user,Integer amount);

}
