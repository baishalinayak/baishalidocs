package com.novopay.in.demo.repository;

import org.springframework.stereotype.Repository;

import com.novopay.in.demo.bean.TransactionDetails;
import com.novopay.in.demo.bean.User;

@Repository
public interface WalletTransactionRepository {
	
	public Integer getBalance(Integer walletId);
	public String addMoney( User user,Integer money,Integer walletId);
	public Integer getWalletId(User user);
	public String saveTransactionDetails(TransactionDetails transactionDetails);

}
