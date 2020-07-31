package com.novopay.in.demo.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novopay.in.demo.bean.TransactionDetails;
import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.repository.WalletTransactionRepository;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService{
	
	private static final String ADDMONEYSUCCESSMSG="Money added successfully";
	
	@Autowired
	WalletTransactionRepository walletTransactionRepository;
	
	public String addMoney( User user,Integer amount) {
		if(amount<=0)
			return "add more amount";
		
		Integer walletId=walletTransactionRepository.getWalletId(user);
		System.out.println(walletId);
		Integer balance=walletTransactionRepository.getBalance(walletId);
		balance=balance+amount;
		String message=walletTransactionRepository.addMoney(user, balance, walletId);
		if(message.equals(ADDMONEYSUCCESSMSG)) {
			TransactionDetails transactionDetails=new TransactionDetails();
			transactionDetails.setAmount(amount);
			transactionDetails.setFromUsername("self");
			transactionDetails.setToUsername("self");
			transactionDetails.setTransactionstatus("Success");
			Date transactionDate=new Date();
			transactionDetails.setTransactionDate(transactionDate);
			walletTransactionRepository.saveTransactionDetails(transactionDetails);
		}
		return message;
		
	}

}
