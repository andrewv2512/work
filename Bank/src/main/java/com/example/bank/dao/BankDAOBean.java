package com.example.bank.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.bank.model.ClientTransactionInfo;
import com.example.bank.model.TransactionBaseInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BankDAOBean implements BankDAO {
	private final JdbcTemplate template;
	
	@Override
	public BigDecimal getCurrentBalance() {
		String sql = "SELECT \"BALANCE\" FROM \"CLIENT_BALANCE\" WHERE \"CLNT_CLNT_ID\" = 1" ;
		return template.queryForObject(sql, BigDecimal.class);
	}
	
	@Override
	public void pay(BigDecimal amount, BigDecimal currentBalance)  {
		String sql = "INSERT INTO \"CLIENT_TRANSACTIONS\"(\"CLNT_CLNT_ID\", \"AMOUNT\", \"BALANCE\", \"ACTION_DATE\") VALUES(1, ?, ?, ?)";
		template.update(sql, new Object[] {amount, amount.add(currentBalance), new Date()});
	}

	@Override
	public void updateBalance(BigDecimal amount) {
		String sql = "UPDATE \"CLIENT_BALANCE\" SET \"BALANCE\" = \"BALANCE\" + ? WHERE \"CLNT_CLNT_ID\" = 1";
		template.update(sql, new Object[] {amount});
	}

	@Override
	public void deleteTransaction(TransactionBaseInfo transactionBaseInfo) {
		String del = "DELETE FROM \"CLIENT_TRANSACTIONS\" WHERE \"CTRS_ID\" = ?";
		template.update(del, new Object[] {transactionBaseInfo.getTransactionId()});
	}
	
	@Override
	public BigDecimal getTransactionAmount(TransactionBaseInfo transactionBaseInfo) {
		String select = "SELECT \"AMOUNT\" FROM \"CLIENT_TRANSACTIONS\" WHERE \"CTRS_ID\" = ?";
		return template.queryForObject(select, new Object[] {transactionBaseInfo.getTransactionId()}, BigDecimal.class);
	}

	@Override
	public List<ClientTransactionInfo> getClientTransactions() {
		String sql = "SELECT * FROM \"CLIENT_TRANSACTIONS\" WHERE \"CLNT_CLNT_ID\" = 1 ORDER BY \"ACTION_DATE\" DESC";
		
		List<ClientTransactionInfo> result = template.query(sql,
				(rs, rowNum) -> new ClientTransactionInfo(
						rs.getLong("ctrs_id"),
						rs.getLong("clnt_clnt_id"),
						rs.getBigDecimal("amount"),
						rs.getBigDecimal("balance"),
						rs.getObject("action_date", LocalDateTime.class))
				);
		
		for (ClientTransactionInfo c: result) {
			log.info(c.getActionDate().toString());
		}
		
		
		return result;
	}
}
