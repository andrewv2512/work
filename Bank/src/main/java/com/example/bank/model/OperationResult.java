package com.example.bank.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OperationResult {
	private BigDecimal amount;
	private BigDecimal balance;
}
