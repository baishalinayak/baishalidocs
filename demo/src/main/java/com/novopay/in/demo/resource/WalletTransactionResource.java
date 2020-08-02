package com.novopay.in.demo.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.novopay.in.demo.bean.TransactionDetails;
import com.novopay.in.demo.bean.User;
import com.novopay.in.demo.service.UserAuthenticationService;
import com.novopay.in.demo.service.WalletTransactionService;

@RestController
@RequestMapping("/novopay/transaction")
public class WalletTransactionResource {

	@Autowired
	WalletTransactionService walletTransactionService;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationService.class);

	@PostMapping(value = "/addMoney/{amount}")
	public ResponseEntity<String> addMoney(@PathVariable("amount") Double amount, HttpServletRequest req) {
		LOGGER.info("fetching session");
		User user = (User) req.getSession().getAttribute("USER");

		return new ResponseEntity<>(walletTransactionService.addMoney(user, amount), HttpStatus.OK);

	}

	@PostMapping(value = "/transferMoney/{recievingUser}/{amount}/{chargingEnd}")
	public ResponseEntity<String> transferMoney(@PathVariable("recievingUser") String recievingUser,
			@PathVariable("amount") Double amount, @PathVariable("chargingEnd") String chargingEnd,
			HttpServletRequest req) {
		LOGGER.info("fetching session");
		User user = (User) req.getSession().getAttribute("USER");

		return new ResponseEntity<>(walletTransactionService.transferMoney(user, recievingUser, amount, chargingEnd),
				HttpStatus.OK);

	}

	@GetMapping(value = "/statusInquiry/{transactionId}")
	public ResponseEntity<String> statusInquiry(@PathVariable("transactionId") Integer transactionId,
			HttpServletRequest req) {
		LOGGER.info("transaction status service started");
		
		return new ResponseEntity<>(walletTransactionService.statusInquiry(transactionId),HttpStatus.OK);

	}

	@GetMapping(value = "/getTransactionDetails")
	public ResponseEntity<List<TransactionDetails>> getTransactionDetails(HttpServletRequest req) {
		LOGGER.info("Fetching session");
		User user = (User) req.getSession().getAttribute("USER");
		
		return new ResponseEntity<>(walletTransactionService.getTransactionDetails(user),HttpStatus.OK);

	}

	@PostMapping(value = "/transactionReversal/{transactionId}")
	public ResponseEntity<String> transactionReversal(@PathVariable("transactionId") Integer transactionId,
			HttpServletRequest req) {
		LOGGER.info("transaction reversal started");
		walletTransactionService.transactionReversal(transactionId);
		return new ResponseEntity<>(HttpStatus.OK);

	}

}
