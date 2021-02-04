package com.example.bank.model.exceptions;

public class ClientNotFoundException extends Exception {
	private static final long serialVersionUID = 889843636163143869L;

	public ClientNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
