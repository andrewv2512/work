package com.example.bank.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.bank.dao.BankDAO;
import com.example.bank.model.AmountOfOperation;
import com.example.bank.model.ClientTransactionInfo;
import com.example.bank.model.OperationResult;
import com.example.bank.model.TransactionBaseInfo;
import com.example.bank.model.exceptions.ClientNotFoundException;
import com.example.bank.model.exceptions.InsufficientFundsException;
import com.example.bank.model.exceptions.TransactionNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankServiceBean implements BankService {
	private final BankDAO dao;

	@Override
	public ResponseEntity<Object> pay(AmountOfOperation amountOfOperation) throws Exception {
		BigDecimal amount = amountOfOperation.getAmount();
		BigDecimal currentBalance = getCurrentBalance();
			
		processOperation(amount, currentBalance);
		
		return ResponseEntity.ok(new OperationResult(amount, currentBalance.add(amount)));
	}

	@Override
	public ResponseEntity<Object> withdraw(AmountOfOperation amountOfOperation) throws Exception {
		BigDecimal amount = amountOfOperation.getAmount().negate();
		BigDecimal currentBalance = getCurrentBalance();
			
		processOperation(amount, currentBalance);
			
		return ResponseEntity.ok(new OperationResult(amount, currentBalance.add(amount)));
	}
	
	@Override
	public ResponseEntity<Object> deleteTransaction(TransactionBaseInfo transactionBaseInfo) throws Exception {
		try {
			BigDecimal delAmount = dao.getTransactionAmount(transactionBaseInfo).negate();
			BigDecimal currentBalance = getCurrentBalance();
			checkAmount(delAmount, currentBalance);
			dao.deleteTransaction(transactionBaseInfo);
			dao.updateBalance(delAmount);
			
			return ResponseEntity.ok(getClientTransactions());
		}
		catch (EmptyResultDataAccessException e) {
			throw new TransactionNotFoundException("Транзакция не найдена");
		}
	}

	@Override
	public List<ClientTransactionInfo> getClientTransactions() {
		return dao.getClientTransactions();
	}
	
	private void processOperation(BigDecimal amount, BigDecimal currentBalance) throws Exception {
		checkAmount(amount, currentBalance);
		dao.pay(amount, currentBalance);
		dao.updateBalance(amount);
	}
	
	private void checkAmount(BigDecimal addingAmount, BigDecimal currentBalance) throws Exception {
		if (currentBalance.add(addingAmount).compareTo(BigDecimal.ZERO) < 0) {
			throw new InsufficientFundsException("Недостаточно средств на счёте");
		}
	}
	
	private BigDecimal getCurrentBalance() throws ClientNotFoundException {
		try {
			BigDecimal getCurrentBalance = dao.getCurrentBalance();
			
			return getCurrentBalance;
		}
		catch (EmptyResultDataAccessException e) {
			throw new ClientNotFoundException("Клиент с идентификатором 1 не найден");
		}
	}
}
