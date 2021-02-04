package com.example.bank.model.exceptions;

public class TransactionNotFoundException extends Exception {
	private static final long serialVersionUID = 5253588401106910886L;
	
	public TransactionNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
