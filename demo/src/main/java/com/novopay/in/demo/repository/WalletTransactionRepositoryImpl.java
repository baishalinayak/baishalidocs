package com.novopay.in.demo.repository;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.novopay.in.demo.bean.TransactionDetails;
import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.dto.TransactionEntity;
import com.novopay.in.demo.dto.WalletEntity;

@Repository
public class WalletTransactionRepositoryImpl implements WalletTransactionRepository {
	
	private static final String ADDMONEYSUCCESSMSG="Money added successfully";
	private static final String TRANSACTIONSUCCESSMSG="Transaction added successfully";
	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Integer getWalletId(User user) {
		Session session = sessionFactory.openSession();
		Integer walletId=0;
		try {
			String sql = "select WALLETID from USER where USERNAME = ?";
			Query query = session.createNativeQuery(sql);
			query.setParameter(1, user.getUserName());
			 walletId = (Integer)query.getSingleResult();

		} catch (PersistenceException exception) {

			throw new PersistenceException();
		} catch (Exception ex) {

		} finally {
			session.close();
		}

		return walletId;
	}
	
	public Integer getBalance(Integer walletId) {
		Session session = sessionFactory.openSession();
		Integer balance=0;
		try {
			String sql = "select we.balance from WalletEntity we where we.walletId = :walletId";
			Query query = session.createQuery(sql);
			query.setParameter("walletId", walletId);
			balance = (Integer)query.getSingleResult();
			session.getTransaction().commit();
		} catch (PersistenceException exception) {
			exception.printStackTrace();
			throw new PersistenceException();
		} catch (Exception ex) {

		} finally {
			session.close();
		}

		return balance;
	}

	@Transactional
	public String addMoney( User user,Integer money,Integer walletId) {

		Session session = sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		String message="";
		try {
			
			WalletEntity wallet=new WalletEntity();
			wallet.setWalletId(walletId);
			wallet.setBalance(money);
			session.update(wallet);
			message=ADDMONEYSUCCESSMSG;
			tx.commit();
			
		} catch (PersistenceException exception) {

			throw new PersistenceException();
		} catch (Exception ex) {

		} finally {
			session.close();
		}

		return message;

	}
	
	@Transactional
	public String saveTransactionDetails(TransactionDetails transactionDetails) {
		
		Session session = sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		String message="";
		try {
			
			TransactionEntity trans=new TransactionEntity();
			trans.setAmount(transactionDetails.getAmount());
			trans.setToUsername(transactionDetails.getToUsername());
			trans.setFromUsername(transactionDetails.getFromUsername());
			trans.setTransactionDate(transactionDetails.getTransactionDate());
			trans.setTransactionstatus(transactionDetails.getTransactionstatus());
			session.save(trans);
			message=TRANSACTIONSUCCESSMSG;
			tx.commit();
			
		} catch (PersistenceException exception) {

			throw new PersistenceException();
		} catch (Exception ex) {

		} finally {
			session.close();
		}
		return message;
	}

}
