package com.novopay.in.demo.exceptions;

public class NovopayException extends RuntimeException{
	
	private static final long serialVersionUID=1L;
	
	private final int exceptionType;
	
	public NovopayException() {
		this("Novopay generic Exception");
	}

	

	public NovopayException(String message) {
		
		this(ExceptionConstants.EXCP_GENERIC,message);
	}
	
	public NovopayException(int type,String message) {
		super(message);
		this.exceptionType=type;
	}
	
	
	
	public int getExceptionType() {
		return exceptionType;
	}
	
	public NovopayException(int type,Throwable t) {
		this(type,t.getMessage());
	}

}
