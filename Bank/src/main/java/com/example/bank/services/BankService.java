package com.example.bank.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.bank.model.AmountOfOperation;
import com.example.bank.model.ClientTransactionInfo;
import com.example.bank.model.TransactionBaseInfo;

public interface BankService {
	public ResponseEntity<Object> pay(AmountOfOperation amountOfOperation) throws Exception;

	public ResponseEntity<Object> withdraw(AmountOfOperation amountOfOperation) throws Exception;
	
	public ResponseEntity<Object> deleteTransaction(TransactionBaseInfo transactionBaseInfo) throws Exception;

	public List<ClientTransactionInfo> getClientTransactions();
}
