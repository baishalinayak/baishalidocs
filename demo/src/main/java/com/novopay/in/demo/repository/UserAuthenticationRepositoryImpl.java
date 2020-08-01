package com.novopay.in.demo.repository;

import java.util.List;

import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.novopay.in.demo.EncryptionDecryption;
import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.dto.UserEntity;
import com.novopay.in.demo.dto.WalletEntity;

@Repository
public class UserAuthenticationRepositoryImpl implements UserAuthenticationRepository {

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	EncryptionDecryption encryptionDecryption;

	@Transactional
	public String signUp(User user) {

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

		} catch (PersistenceException exception) {

			throw new PersistenceException();
		} catch (Exception ex) {

		} finally {
			tx.commit();
			session.close();
		}

		return "user has been added successfully";
	}

	public String signIn(User user) {
		String password = "";
		String message = "Incorrect username or password";
		Session session = sessionFactory.openSession();
		try {
			String sql = "from UserEntity ue where ue.userName = :username";
			Query query = session.createQuery(sql);
			query.setParameter("username", user.getUserName());
			List<UserEntity> userEntityList = query.getResultList();
			if (userEntityList.isEmpty() || userEntityList == null || userEntityList.size() == 0)
				message = "User not found";

			for (UserEntity userEntity : userEntityList) {
				password = encryptionDecryption.decryptedText(userEntity.getPassword());

			}

			if (password.equals(user.getPassword())) {
				message = "Login Successful";
			}

		} catch (PersistenceException exception) {

			throw new PersistenceException();
		} catch (Exception ex) {

		} finally {
			session.close();
		}

		return message;
	}

}
