package com.novopay.in.demo.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novopay.in.demo.bean.TransactionDetails;
import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.exceptions.ExceptionConstants;
import com.novopay.in.demo.exceptions.NovopayException;
import com.novopay.in.demo.repository.WalletTransactionRepository;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

	private static final String ADDMONEYSUCCESSMSG = "Money added successfully";
	private static final String ADDMONEYERRORMSG = "Some error occurred !!!";
	private static final String SENDMONEYSUCCESSMSG = "Money sent successfully";
	private static final String SENDMONEYFAILEDMSG = "Transaction failed !!!";

	private static final Logger LOGGER = LoggerFactory.getLogger(WalletTransactionServiceImpl.class);
	@Autowired
	WalletTransactionRepository walletTransactionRepository;

	public String addMoney(User user, Double amount) {
		LOGGER.info("money being added");
		String message = "";
		try {
			if (amount <= 0) {
				LOGGER.info("money less than 1");
				return "add more amount";
			}
			Integer walletId = walletTransactionRepository.getWalletId(user.getUserName());
			System.out.println(walletId);
			Double balance = walletTransactionRepository.getBalance(walletId);
			balance = balance + amount;
			message = walletTransactionRepository.addMoney(balance, walletId);
			if (message.equals(ADDMONEYSUCCESSMSG)) {
				LOGGER.info("addition in passbook started");
				TransactionDetails transactionDetails = new TransactionDetails();
				transactionDetails.setAmount(amount);
				transactionDetails.setFromUsername("self");
				transactionDetails.setToUsername("self");
				transactionDetails.setTransactionstatus("Success");
				Date transactionDate = new Date();
				transactionDetails.setTransactionDate(transactionDate);
				walletTransactionRepository.saveTransactionDetails(transactionDetails);

			}
		} catch (Exception ex) {
			throw new NovopayException(ExceptionConstants.SQL_EXCEPTION, ex);

		}
		LOGGER.debug(message);
		return message;

	}

	public Double calculateCommission(Double amount) {
		LOGGER.info("calculating commission");
		Double commission = 0.05 * amount;
		return commission;
	}

	public Double calculateCharge(Double amount) {
		LOGGER.info("calculating charges");
		Double charge = 0.2 * amount;
		return charge;
	}

	public String upDateBalance(Double updatedBalance, int walletId) {
		String message = "";
		try {
			message = walletTransactionRepository.addMoney(updatedBalance, walletId);

		} catch (Exception ex) {
			throw new NovopayException(ExceptionConstants.SQL_EXCEPTION, ex);
		}
		LOGGER.debug(message);
		return message;

	}

	public TransactionDetails setTransactionDetails(User user, String recievingUser, Double amount) {

		TransactionDetails transactionDetails = new TransactionDetails();
		transactionDetails.setAmount(amount);
		transactionDetails.setFromUsername(user.getUserName());
		transactionDetails.setToUsername(recievingUser);
		Date transactionDate = new Date();
		transactionDetails.setTransactionDate(transactionDate);
		return transactionDetails;

	}

	public String transferMoney(User user, String recievingUser, Double amount, String chargingEnd) {
		LOGGER.info("Transaction started");
		Double senderInitialBalance = 0d;
		Double receiverInitialBalance = 0d;
		Integer senderWalletId = 0;
		Integer receiverWalletId = 0;
		Double senderBalance = 0d;
		Double recieverBalance = 0d;
		String message = "";
		try {
			senderWalletId = walletTransactionRepository.getWalletId(user.getUserName());
			receiverWalletId = walletTransactionRepository.getWalletId(recievingUser);
			senderInitialBalance = walletTransactionRepository.getBalance(senderWalletId);
			receiverInitialBalance = walletTransactionRepository.getBalance(receiverWalletId);

			if (chargingEnd.equals("DEBIT")) {
				senderBalance = senderInitialBalance - amount;
				senderBalance -= calculateCharge(amount);
				recieverBalance = receiverInitialBalance + amount;

				recieverBalance += calculateCommission(amount);
				LOGGER.debug("The balance being deducted is : {}", senderBalance);
				LOGGER.debug("the balance being added is : {}", recieverBalance);

			}

			if (chargingEnd.equals("CREDIT")) {
				senderBalance = senderInitialBalance - amount;
				senderBalance += calculateCommission(amount);
				recieverBalance = receiverInitialBalance + amount;

				recieverBalance -= calculateCharge(amount);
				LOGGER.debug("The balance being deducted is : {}", senderBalance);
				LOGGER.debug("the balance being added is : {}", recieverBalance);
			}
		} catch (Exception ex) {
			message = ADDMONEYERRORMSG;
			LOGGER.debug(ADDMONEYERRORMSG);
			throw new NovopayException(ExceptionConstants.SQL_EXCEPTION, ex);
		}

		try {
			String senderMsg = upDateBalance(senderBalance, senderWalletId);
			String receiverMsg = upDateBalance(recieverBalance, receiverWalletId);
			if (senderMsg.equals(ADDMONEYSUCCESSMSG) && receiverMsg.equals(ADDMONEYSUCCESSMSG)) {

				TransactionDetails transactionDetails = setTransactionDetails(user, recievingUser, amount);
				transactionDetails.setTransactionstatus("Success");
				walletTransactionRepository.saveTransactionDetails(transactionDetails);
				message = SENDMONEYSUCCESSMSG;
				LOGGER.debug(SENDMONEYSUCCESSMSG);
				return message;

			}
		} catch (Exception ex) {
			message = SENDMONEYFAILEDMSG;
			LOGGER.debug(SENDMONEYFAILEDMSG);
			upDateBalance(senderInitialBalance, senderWalletId);
			upDateBalance(receiverInitialBalance, receiverWalletId);
			TransactionDetails transactionDetails = setTransactionDetails(user, recievingUser, amount);
			transactionDetails.setTransactionstatus("Failure");
			walletTransactionRepository.saveTransactionDetails(transactionDetails);
			return message;

		}

		return message;
	}

	public List<TransactionDetails> getTransactionDetails(User user) {
		try {
			return walletTransactionRepository.getTransactionDetails(user.getUserName());
		} catch (Exception ex) {
			throw new NovopayException(ExceptionConstants.SQL_EXCEPTION, ex);
		}
	}

	public String statusInquiry(Integer TransactionId) {
		return walletTransactionRepository.statusInquiry(TransactionId);
	}

	public String transactionReversal(Integer TransactionId) {

		Double senderInitialBalance = 0d;
		Double receiverInitialBalance = 0d;
		Integer senderWalletId = 0;
		Integer receiverWalletId = 0;
		Double senderBalance = 0d;
		Double recieverBalance = 0d;
		String toUser = "";
		String fromUser = "";
		Double amount = 0d;
		String message = "";
		TransactionDetails transactionDetails = null;
		try {
			transactionDetails = walletTransactionRepository.transactionReversal(TransactionId);
			toUser = transactionDetails.getFromUsername();
			fromUser = transactionDetails.getToUsername();
			amount = transactionDetails.getAmount();
			senderWalletId = walletTransactionRepository.getWalletId(toUser);
			receiverWalletId = walletTransactionRepository.getWalletId(fromUser);
			senderInitialBalance = walletTransactionRepository.getBalance(senderWalletId);
			receiverInitialBalance = walletTransactionRepository.getBalance(receiverWalletId);

			senderBalance = senderInitialBalance + amount;
			recieverBalance = receiverInitialBalance - amount;
		} catch (Exception ex) {

			message = ADDMONEYERRORMSG;
			LOGGER.debug(ADDMONEYERRORMSG);
			throw new NovopayException(ExceptionConstants.EXCP_GENERIC, ex);

		}
		try {
			String senderMsg = upDateBalance(senderBalance, senderWalletId);
			String receiverMsg = upDateBalance(recieverBalance, receiverWalletId);
			if (senderMsg.equals(ADDMONEYSUCCESSMSG) && receiverMsg.equals(ADDMONEYSUCCESSMSG)) {

				TransactionDetails transactionDetails1 = new TransactionDetails();
				transactionDetails.setToUsername(toUser);
				transactionDetails.setFromUsername(fromUser);
				transactionDetails.setAmount(amount);
				Date transactionDate = new Date();
				transactionDetails.setTransactionDate(transactionDate);
				transactionDetails.setTransactionstatus("Reversal");
				walletTransactionRepository.saveTransactionDetails(transactionDetails);
				message = SENDMONEYSUCCESSMSG;
				LOGGER.debug(SENDMONEYSUCCESSMSG);
				return message;

			}
		} catch (Exception ex) {
			message = SENDMONEYFAILEDMSG;
			LOGGER.debug(SENDMONEYFAILEDMSG);
			upDateBalance(senderInitialBalance, senderWalletId);
			upDateBalance(receiverInitialBalance, receiverWalletId);
			TransactionDetails transactionDetails1 = new TransactionDetails();
			transactionDetails.setToUsername(toUser);
			transactionDetails.setFromUsername(fromUser);
			transactionDetails.setAmount(amount);
			Date transactionDate = new Date();
			transactionDetails.setTransactionDate(transactionDate);
			transactionDetails.setTransactionstatus("Reversal Failure");
			walletTransactionRepository.saveTransactionDetails(transactionDetails);
			return message;

		}

		return message;
	}

}
