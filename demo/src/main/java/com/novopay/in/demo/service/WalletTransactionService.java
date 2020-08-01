package com.novopay.in.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.novopay.in.demo.bean.TransactionDetails;
import com.novopay.in.demo.bean.User;

@Service
public interface WalletTransactionService {
	
	
	public String addMoney( User user,Double amount);
	public String transferMoney(User user,String recievingUser,Double amount,String chargingEnd);
	public String statusInquiry(Integer TransactionId);
	public List<TransactionDetails> getTransactionDetails(User user);
	public String transactionReversal(Integer TransactionId);
	

}
