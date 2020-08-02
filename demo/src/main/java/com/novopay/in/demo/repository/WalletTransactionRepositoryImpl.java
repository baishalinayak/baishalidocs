package com.novopay.in.demo.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.novopay.in.demo.bean.TransactionDetails;
import com.novopay.in.demo.dto.TransactionEntity;
import com.novopay.in.demo.dto.WalletEntity;

@Repository
public class WalletTransactionRepositoryImpl implements WalletTransactionRepository {

	private static final String ADDMONEYSUCCESSMSG = "Money added successfully";
	private static final String TRANSACTIONSUCCESSMSG = "Transaction added successfully";

	private static final Logger LOGGER = LoggerFactory.getLogger(WalletTransactionRepository.class);

	@Autowired
	private SessionFactory sessionFactory;

	public Integer getWalletId(String user) {
		LOGGER.info("Fetching getWalletId");
		Session session = sessionFactory.openSession();
		Integer walletId = 0;
		try {
			String sql = "select WALLETID from USER where USERNAME = ?";
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, user);
			walletId = (Integer) query.getSingleResult();

		} catch (PersistenceException exception) {

			throw exception;
		} finally {
			session.close();
		}

		return walletId;
	}

	public Double getBalance(Integer walletId) {
		LOGGER.info("Fetching balance");
		Session session = sessionFactory.openSession();

		Double balance = 0d;
		try {
			String sql = "select we.balance from WalletEntity we where we.walletId = :walletId";
			Query query = session.createQuery(sql);
			query.setParameter("walletId", walletId);
			balance = (Double) query.getSingleResult();

		} catch (PersistenceException exception) {
			throw exception;
		} finally {
			session.close();
		}

		return balance;
	}

	@Transactional
	public String addMoney(Double money, Integer walletId) {
		LOGGER.info("Adding money");
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String message = "";
		try {

			WalletEntity wallet = new WalletEntity();
			wallet.setWalletId(walletId);
			wallet.setBalance(money);
			session.update(wallet);
			message = ADDMONEYSUCCESSMSG;
			tx.commit();
			LOGGER.debug("success message : {}", ADDMONEYSUCCESSMSG);
		} catch (PersistenceException exception) {
			throw exception;

		} finally {

			session.close();
		}

		return message;

	}

	public String addErrormessage() {
		return "some error occured";
	}

	@Transactional
	public String saveTransactionDetails(TransactionDetails transactionDetails) {
		LOGGER.info("Transaction started");
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		String message = "";
		try {

			TransactionEntity trans = new TransactionEntity();
			trans.setAmount(transactionDetails.getAmount());
			trans.setToUsername(transactionDetails.getToUsername());
			trans.setFromUsername(transactionDetails.getFromUsername());
			trans.setTransactionDate(transactionDetails.getTransactionDate());
			trans.setTransactionstatus(transactionDetails.getTransactionstatus());
			session.save(trans);
			message = TRANSACTIONSUCCESSMSG;
			tx.commit();
			LOGGER.debug("the transaction success message {}", message);
		} catch (PersistenceException exception) {

			throw exception;
		} catch (Exception ex) {

		} finally {
			session.close();
		}
		return message;
	}

	public List<TransactionDetails> getTransactionDetails(String userName) {
		LOGGER.info("Fetching Passbook");
		Session session = sessionFactory.openSession();
		List<TransactionDetails> transactionDetailslist = new ArrayList();

		try {
			String sql = " from TransactionEntity we where we.fromUsername = :userName or we.toUsername = :userName";
			Query query = session.createQuery(sql);
			query.setParameter("userName", userName);
			List<TransactionEntity> list = query.getResultList();
			for (TransactionEntity transactionEntity : list) {
				TransactionDetails transactionDetails = new TransactionDetails();
				transactionDetails.setTransactionId(transactionDetails.getTransactionId());
				transactionDetails.setAmount(transactionEntity.getAmount());
				transactionDetails.setToUsername(transactionEntity.getToUsername());
				transactionDetails.setFromUsername(transactionDetails.getFromUsername());
				transactionDetails.setTransactionDate(transactionDetails.getTransactionDate());
				transactionDetails.setTransactionstatus(transactionDetails.getTransactionstatus());
				transactionDetailslist.add(transactionDetails);
			}

		} catch (PersistenceException exception) {
			throw exception;
		} finally {
			session.close();
		}

		return transactionDetailslist;
	}

	public String statusInquiry(Integer TransactionId) {
		LOGGER.info("fetching status inquiry");
		Session session = sessionFactory.openSession();
		String status = "";
		try {
			String sql = "select te.transactionstatus from  TransactionEntity te where te.transactionId =: TransactionId";
			Query query = session.createQuery(sql);
			query.setParameter("TransactionId", TransactionId);
			status = (String) query.getSingleResult();

		} catch (PersistenceException exception) {

			throw exception;
		} finally {
			session.close();
		}

		return status;
	}

	public TransactionDetails transactionReversal(Integer TransactionId) {

		LOGGER.info("Reversing transaction");

		Session session = sessionFactory.openSession();
		TransactionDetails transactionDetails = new TransactionDetails();
		try {
			String sql = "select te.toUsername , te.fromUsername , te.amount from  TransactionEntity te where te.transactionId =: TransactionId";
			Query query = session.createQuery(sql);
			query.setParameter("TransactionId", TransactionId);
			List<Object[]> list = query.getResultList();

			for (Object[] transactionEntity : list) {

				transactionDetails.setToUsername((String) transactionEntity[0]);
				transactionDetails.setFromUsername((String) transactionEntity[1]);
				transactionDetails.setAmount((Double) transactionEntity[2]);

			}

		} catch (PersistenceException exception) {

			throw exception;
		} finally {
			session.close();
		}

		return transactionDetails;

	}

}
