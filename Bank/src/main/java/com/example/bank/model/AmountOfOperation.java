package com.example.bank.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.example.bank.utils.MoneyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AmountOfOperation {
	@NotNull(message = "Не заполнен обязательный атрибут: amount")
	@JsonDeserialize(using = MoneyDeserializer.class)
	private BigDecimal amount;
}
