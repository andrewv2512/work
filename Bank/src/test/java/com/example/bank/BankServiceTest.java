package com.example.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;

import com.example.bank.dao.BankDAO;
import com.example.bank.model.AmountOfOperation;
import com.example.bank.model.ClientTransactionInfo;
import com.example.bank.model.OperationResult;
import com.example.bank.model.TransactionBaseInfo;
import com.example.bank.model.exceptions.ClientNotFoundException;
import com.example.bank.model.exceptions.InsufficientFundsException;
import com.example.bank.model.exceptions.TransactionNotFoundException;
import com.example.bank.services.BankService;
import com.example.bank.services.BankServiceBean;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.Assert.assertEquals;

@SpringBootTest
public class BankServiceTest {
	private BankService bankservice;
	private List<ClientTransactionInfo> expectedTransactions;
	private TransactionBaseInfo transactionBaseInfo = new TransactionBaseInfo(11L);
	
	@Mock
	private BankDAO dao;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		bankservice = new BankServiceBean(dao);
		
		expectedTransactions = new ArrayList<>();	
		expectedTransactions.add(new ClientTransactionInfo(11L, 1L, BigDecimal.TEN, BigDecimal.ONE, LocalDateTime.now()));
		
		doNothing().when(dao).pay(any(BigDecimal.class), any(BigDecimal.class));
		doNothing().when(dao).updateBalance(any(BigDecimal.class));
		doReturn(expectedTransactions).when(dao).getClientTransactions();
		doReturn(BigDecimal.TEN).when(dao).getCurrentBalance();
		doReturn(BigDecimal.ONE).when(dao).getTransactionAmount(any(TransactionBaseInfo.class));
	}
	
	@Test
	public void shouldPay() throws Exception {
		ResponseEntity<?> result = bankservice.pay(new AmountOfOperation(BigDecimal.ONE));

		verify(dao, times(1)).getCurrentBalance();
		verify(dao, times(1)).pay(any(BigDecimal.class), eq(BigDecimal.TEN));
		verify(dao, times(1)).updateBalance(any(BigDecimal.class));
		
		ResponseEntity<OperationResult> expected = ResponseEntity.ok().body(new OperationResult(BigDecimal.ONE, BigDecimal.TEN));
		
		OperationResult resultBody = (OperationResult)result.getBody();
		OperationResult expectedBody = (OperationResult)expected.getBody();
		
		assertEquals("should be equal message", expectedBody.getAmount(), resultBody.getAmount());
		assertEquals("should be equal balance", expectedBody.getBalance(), resultBody.getBalance().subtract(resultBody.getAmount()));
	}
	
	@Test
	public void shouldWithdraw() throws Exception {
		ResponseEntity<?> result = bankservice.withdraw(new AmountOfOperation(BigDecimal.ONE));

		verify(dao, times(1)).getCurrentBalance();
		verify(dao, times(1)).pay(any(BigDecimal.class), eq(BigDecimal.TEN));
		verify(dao, times(1)).updateBalance(any(BigDecimal.class));
		
		ResponseEntity<OperationResult> expected = ResponseEntity.ok().body(new OperationResult(BigDecimal.ONE.negate(), BigDecimal.TEN));
		
		OperationResult resultBody = (OperationResult)result.getBody();
		OperationResult expectedBody = (OperationResult)expected.getBody();
		
		assertEquals("should be equal message", expectedBody.getAmount(), resultBody.getAmount());
		assertEquals("should be equal balance", expectedBody.getBalance(), resultBody.getBalance().subtract(resultBody.getAmount()));
	}
	
	@Test
	public void shouldDeleteTransaction() throws Exception {
		ResponseEntity<?> result = bankservice.deleteTransaction(transactionBaseInfo);

		verify(dao, times(1)).getTransactionAmount(transactionBaseInfo);
		verify(dao, times(1)).getCurrentBalance();
		verify(dao, times(1)).deleteTransaction(transactionBaseInfo);
		verify(dao, times(1)).updateBalance(BigDecimal.ONE.negate());
		
		ResponseEntity<List<ClientTransactionInfo>> expected = ResponseEntity.ok().body(expectedTransactions);
		
		assertEquals("responseEntity shold be equal to result", expected, result);
	}
	
	@Test
	public void shouldGetClientTransactions() throws Exception {
		List<ClientTransactionInfo> result = bankservice.getClientTransactions();
		
		List<ClientTransactionInfo> expected = expectedTransactions;
		
		assertEquals("response should be equal to result", expected, result);
	}
	
	@Test
	public void shouldGetInsufficientFundsException() throws Exception {
		thrown.expect(InsufficientFundsException.class);
		
		bankservice.pay(new AmountOfOperation(BigDecimal.valueOf(20.2).negate()));
	}
	
	@Test(expected=ClientNotFoundException.class)
	public void shouldGetClientNotFoundException() throws Exception {
	    when(dao.getCurrentBalance()).thenThrow(EmptyResultDataAccessException.class);
		
		bankservice.pay(new AmountOfOperation(BigDecimal.valueOf(20)));
	}
	
	@Test(expected=TransactionNotFoundException.class)
	public void shouldGetTransactionNotFoundException() throws Exception {
	    when(dao.getTransactionAmount(transactionBaseInfo)).thenThrow(EmptyResultDataAccessException.class);
		
	    bankservice.deleteTransaction(transactionBaseInfo);
	}
}
