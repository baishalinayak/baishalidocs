package com.novopay.in.demo.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "WALLET")
public class WalletEntity {
	
	@OneToOne(mappedBy="wallet",cascade = CascadeType.ALL)
	private UserEntity user;
	
	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Integer walletId;
	
	
	
	
	@Column
	private Integer balance;


	public UserEntity getUser() {
		return user;
	}


	public void setUser(UserEntity user) {
		this.user = user;
	}


	public Integer getWalletId() {
		return walletId;
	}


	public void setWalletId(Integer walletId) {
		this.walletId = walletId;
	}


	public Integer getBalance() {
		return balance;
	}


	public void setBalance(Integer balance) {
		this.balance = balance;
	}
	
	
	
	

}
