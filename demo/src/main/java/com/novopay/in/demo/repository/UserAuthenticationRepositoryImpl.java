package com.novopay.in.demo.repository;

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

import com.novopay.in.demo.EncryptionDecryption;
import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.dto.UserEntity;
import com.novopay.in.demo.dto.WalletEntity;

@Repository
public class UserAuthenticationRepositoryImpl implements UserAuthenticationRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationRepository.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	EncryptionDecryption encryptionDecryption;

	private static final String LOGINSUCCESSMSG = "Login Successful";
	private static final String USERNOTFOUNDMSG = "User not found";
	private static final String LOGINERRORMSG = "Incorrect username or password";
	private static final String SIGNUPSUCCESSMSG = "user has been added successfully";

	@Transactional
	public String signUp(User user) {
		LOGGER.info("Inside signup method");

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();

		try {

			WalletEntity wallet = new WalletEntity();
			wallet.setBalance(0d);

			UserEntity userEntity = new UserEntity();
			userEntity.setUserName(user.getUserName());
			String encryptedPas = encryptionDecryption.encryptedText(user.getPassword());
			userEntity.setPassword(encryptedPas);
			userEntity.setFirstName(user.getFirstName());
			userEntity.setLastName(user.getLastName());
			userEntity.setPhoneNumber(user.getPhoneNumber());
			session.saveOrUpdate(wallet);
			userEntity.setWallet(wallet);
			session.saveOrUpdate(userEntity);

			LOGGER.debug("the user is : {}", userEntity.getUserName());

		} catch (PersistenceException exception) {

			throw new PersistenceException();
		} catch (Exception ex) {

		} finally {
			tx.commit();
			session.close();
		}

		return SIGNUPSUCCESSMSG;
	}

	public String signIn(User user) {
		LOGGER.info("Inside signIn method");
		String password = "";
		String message = LOGINERRORMSG;
		Session session = sessionFactory.openSession();
		try {
			String sql = "from UserEntity ue where ue.userName = :username";
			Query query = session.createQuery(sql);
			query.setParameter("username", user.getUserName());
			List<UserEntity> userEntityList = query.getResultList();
			if (userEntityList.isEmpty() || userEntityList == null || userEntityList.size() == 0)
				message = USERNOTFOUNDMSG;

			for (UserEntity userEntity : userEntityList) {
				password = encryptionDecryption.decryptedText(userEntity.getPassword());

			}

			if (password.equals(user.getPassword())) {
				message = LOGINSUCCESSMSG;
			}

			LOGGER.debug("the message : {}", message);

		} catch (PersistenceException exception) {

			throw new PersistenceException();
		} catch (Exception ex) {

		} finally {
			session.close();
		}

		return message;
	}

}
