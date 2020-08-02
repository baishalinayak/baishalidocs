package com.novopay.in.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.novopay.in.demo.exceptions.NovopayException;
import com.novopay.in.demo.exceptions.UserAuthorizationException;

@SuppressWarnings({"unchecked","rawtypes"})
@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(NullPointerException.class)
	public final ResponseEntity<Object> handleAllExceptions(NovopayException ex){
		String message="Invalid Session "+ex.getMessage();
		return new ResponseEntity(message,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NovopayException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(NovopayException ex){
		String message="No user Found "+ex.getMessage();
		return new ResponseEntity(message,HttpStatus.NO_CONTENT);
	}
	
	@ExceptionHandler(UserAuthorizationException.class)
	public final ResponseEntity<Object> handleUserNotAuthorise(NovopayException ex){
		String message="You are not authorised to handle the exception  "+ex.getMessage();
		return new ResponseEntity(message,HttpStatus.UNAUTHORIZED);
	}
	
	

}
