package com.example.bank.dao;

import java.math.BigDecimal;
import java.util.List;

import com.example.bank.model.ClientTransactionInfo;
import com.example.bank.model.TransactionBaseInfo;


public interface BankDAO {
	public BigDecimal getCurrentBalance();
	
	public void pay(BigDecimal amount, BigDecimal currentBalance);

	public void updateBalance(BigDecimal amount);

	public void deleteTransaction(TransactionBaseInfo transactionBaseInfo);

	public BigDecimal getTransactionAmount(TransactionBaseInfo transactionBaseInfo);
	
	public List<ClientTransactionInfo> getClientTransactions();
}
