package com.example.bank;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import com.example.bank.controllers.BankController;
import com.example.bank.model.AmountOfOperation;
import com.example.bank.model.ClientTransactionInfo;
import com.example.bank.model.OperationResult;
import com.example.bank.model.TransactionBaseInfo;
import com.example.bank.services.BankService;

import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class BankControllerTest {
	private BankController controller;
	
	private List<ClientTransactionInfo> expectedTransactions;
	
	@Mock
	private BankService service;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		controller = new BankController(service);
		
		expectedTransactions = new ArrayList<>();	
		expectedTransactions.add(new ClientTransactionInfo(11L, 1L, BigDecimal.TEN, BigDecimal.ONE, LocalDateTime.now()));
		expectedTransactions.add(new ClientTransactionInfo(12L, 1L, BigDecimal.ONE, BigDecimal.ONE, LocalDateTime.now()));
		expectedTransactions.add(new ClientTransactionInfo(13L, 1L, BigDecimal.ONE.negate(), BigDecimal.ONE, LocalDateTime.now()));
	}
	
	@Test
	public void shouldGetTransactions() throws Exception {
		when(service.getClientTransactions()).thenReturn(expectedTransactions);
		
		List<ClientTransactionInfo> actualTransactions = controller.getTransactions();
		
		assertEquals("ResponseEntity should be equal", expectedTransactions, actualTransactions);
	}
	
	@Test
	public void shouldPay() throws Exception {
		AmountOfOperation amountOfOperation = new AmountOfOperation(BigDecimal.ONE);
		
		ResponseEntity<Object> expected = ResponseEntity.ok(new OperationResult(BigDecimal.ONE, BigDecimal.ONE));
		
		when(service.pay(eq(amountOfOperation))).thenReturn(expected);

		ResponseEntity<?> result = controller.pay(amountOfOperation);
		
		assertEquals("ResponseEntity should be equal", expected, result);
	}
	
	@Test
	public void shouldWithdraw() throws Exception {
		AmountOfOperation amountOfOperation = new AmountOfOperation(BigDecimal.ONE);
		
		ResponseEntity<Object> expected = ResponseEntity.ok(new OperationResult(BigDecimal.ONE, BigDecimal.ONE));
		
		when(service.withdraw(eq(amountOfOperation))).thenReturn(expected);

		ResponseEntity<?> result = controller.withdraw(amountOfOperation);
		
		assertEquals("ResponseEntity should be equal", expected, result);
	}
	
	@Test
	public void shouldDeleteTransaction() throws Exception {
		ResponseEntity<Object> expected = ResponseEntity.ok(expectedTransactions);
		
		when(service.deleteTransaction(any(TransactionBaseInfo.class))).thenReturn(expected);

		ResponseEntity<?> result = controller.deleteTransaction(new TransactionBaseInfo(11L));
		
		assertEquals("ResponseEntity should be equal", expected, result);
	}
}
