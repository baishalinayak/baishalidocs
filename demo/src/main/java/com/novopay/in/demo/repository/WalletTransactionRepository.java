package com.novopay.in.demo.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.novopay.in.demo.bean.TransactionDetails;
import com.novopay.in.demo.bean.User;

@Repository
public interface WalletTransactionRepository {
	
	public Double getBalance(Integer walletId);
	public String addMoney(Double money,Integer walletId);
	public Integer getWalletId(String user);
	public String saveTransactionDetails(TransactionDetails transactionDetails);
	public List<TransactionDetails> getTransactionDetails(String userName);
	public String statusInquiry(Integer TransactionId);
	public TransactionDetails transactionReversal(Integer TransactionId);

}
