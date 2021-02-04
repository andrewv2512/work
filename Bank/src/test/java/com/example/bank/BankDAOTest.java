package com.example.bank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.example.bank.dao.BankDAOBean;
import com.example.bank.model.ClientTransactionInfo;
import com.example.bank.model.TransactionBaseInfo;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doReturn;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
public class BankDAOTest {
	private BankDAOBean bankDao;
	private List<ClientTransactionInfo> expectedTransactions;
	
	@Mock
	private JdbcTemplate template;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		bankDao = new BankDAOBean(template);
		
		doReturn(BigDecimal.TEN).when(template).queryForObject(any(String.class), any(Object[].class), (Class<?>) any(Class.class));
		
		expectedTransactions = new ArrayList<>();	
		expectedTransactions.add(new ClientTransactionInfo(11L, 1L, BigDecimal.TEN, BigDecimal.ONE, LocalDateTime.now()));
		expectedTransactions.add(new ClientTransactionInfo(12L, 1L, BigDecimal.TEN, BigDecimal.TEN, LocalDateTime.now()));
		
		doReturn(BigDecimal.TEN).when(template).queryForObject(any(String.class), any(Object[].class), (Class<?>) any(Class.class));
		doReturn(expectedTransactions).when(template).query(any(String.class), (RowMapper<?>) any(RowMapper.class));
	}
	
	@Test
	public void shouldGetCurrentBalance() {
		bankDao.getCurrentBalance();
		String sql = "SELECT \"BALANCE\" FROM \"CLIENT_BALANCE\" WHERE \"CLNT_CLNT_ID\" = 1" ;
		verify(template, times(1)).queryForObject(sql, BigDecimal.class);
	}
	
	@Test
	public void shouldPay()  {
		BigDecimal amount = BigDecimal.valueOf(100.45);
		BigDecimal currentBalance = BigDecimal.valueOf(100);
	
		bankDao.pay(amount, currentBalance);
		String sql = "INSERT INTO \"CLIENT_TRANSACTIONS\"(\"CLNT_CLNT_ID\", \"AMOUNT\", \"BALANCE\", "
				+ "\"ACTION_DATE\") VALUES(1, ?, ?, ?)";

		verify(template, times(1)).update(eq(sql), any(Object.class));
	}
	
	@Test
	public void shouldUpdateBalance()  {
		BigDecimal amount = BigDecimal.valueOf(100.45);
	
		bankDao.updateBalance(amount);
		String sql = "UPDATE \"CLIENT_BALANCE\" SET \"BALANCE\" = \"BALANCE\" + ? WHERE \"CLNT_CLNT_ID\" = 1";
		verify(template, times(1)).update(eq(sql), any(Object.class));
	}
	
	@Test
	public void shouldDeleteTransaction()  {
		bankDao.deleteTransaction(new TransactionBaseInfo(11L));
		String del = "DELETE FROM \"CLIENT_TRANSACTIONS\" WHERE \"CTRS_ID\" = ?";
		verify(template, times(1)).update(eq(del), any(Object.class));
	}
	
	@Test
	public void shouldGetTransactionAmount()  {
		String select = "SELECT \"AMOUNT\" FROM \"CLIENT_TRANSACTIONS\" WHERE \"CTRS_ID\" = ?";
		BigDecimal amount = bankDao.getTransactionAmount(new TransactionBaseInfo(11L));
		
		verify(template, times(1)).queryForObject(eq(select), any(Object[].class), (Class<?>) any(Class.class));
		assertEquals("should be BigDecimal TEN", BigDecimal.TEN, amount);
	}

	@Test
	public void shouldGetClientTransactions()  {
		String sql = "SELECT * FROM \"CLIENT_TRANSACTIONS\" WHERE \"CLNT_CLNT_ID\" = 1 ORDER BY \"ACTION_DATE\" DESC";
		List<ClientTransactionInfo> actualTransactions = bankDao.getClientTransactions();

		verify(template, times(1)).query(eq(sql), (RowMapper<?>) any(RowMapper.class));
		assertEquals("should be equal transaction lists", expectedTransactions, actualTransactions);
	}	
}
