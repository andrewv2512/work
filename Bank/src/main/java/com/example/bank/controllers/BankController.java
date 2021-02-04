package com.example.bank.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.model.AmountOfOperation;
import com.example.bank.model.ClientTransactionInfo;
import com.example.bank.model.TransactionBaseInfo;
import com.example.bank.services.BankService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bank")
public class BankController {
	private final BankService bankService;
	
	@GetMapping("/transactions")
	public List<ClientTransactionInfo> getTransactions() {
		return bankService.getClientTransactions();
	}
	
	@PostMapping("/pay")
	public ResponseEntity<Object> pay(@Valid @RequestBody AmountOfOperation amount) throws Exception {
		return bankService.pay(amount);
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<Object> withdraw(@Valid @RequestBody AmountOfOperation amount) throws Exception {
		return bankService.withdraw(amount);
	}
	
	@PostMapping("/transactions/delete")
	public ResponseEntity<Object> deleteTransaction(@Valid @RequestBody TransactionBaseInfo transactionId) throws Exception {
		return bankService.deleteTransaction(transactionId);
	}
}
