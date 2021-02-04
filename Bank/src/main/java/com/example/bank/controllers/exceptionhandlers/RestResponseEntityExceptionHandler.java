package com.example.bank.controllers.exceptionhandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.bank.model.ResponseInfo;
import com.example.bank.model.exceptions.ClientNotFoundException;
import com.example.bank.model.exceptions.InsufficientFundsException;
import com.example.bank.model.exceptions.TransactionNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler({ClientNotFoundException.class, TransactionNotFoundException.class})
	public ResponseEntity<ResponseInfo> handleObjectNotFound(
			Exception exception, 
			WebRequest request
	){
	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseInfo(exception.getMessage()));
	}
	
	@ExceptionHandler(InsufficientFundsException.class)
	public ResponseEntity<ResponseInfo> handleInsufficientFunds(
			InsufficientFundsException exception, 
			WebRequest request
	){
	    return ResponseEntity.unprocessableEntity().body(new ResponseInfo(exception.getMessage()));
	}
}
