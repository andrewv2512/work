package com.example.bank.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionBaseInfo {
	@NotNull(message = "Не заполнен обязательный атрибут: transactionId")
	private Long transactionId;
}
