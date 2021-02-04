package com.example.bank.model.exceptions;

public class InsufficientFundsException extends Exception {
	private static final long serialVersionUID = 8887165954293295202L;

	public InsufficientFundsException(String errorMessage) {
		super(errorMessage);
	}
}
