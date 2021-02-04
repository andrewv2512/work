package com.example.bank;

import java.io.IOException;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.bank.model.AmountOfOperation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertEquals;


@SpringBootTest
public class MoneyDeserializerTest {
	private ObjectMapper mapper;
	
    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }
	
    @Test
    public void pqsql_deserialize_bigDecimal() throws JsonParseException, IOException {
        String json = mapper.writeValueAsString(new AmountOfOperation(BigDecimal.valueOf(12.16)));
        JsonParser parser = mapper.getFactory().createParser(json);
        
        AmountOfOperation deserialisedAmount = mapper.readValue(parser, AmountOfOperation.class);
        
        assertEquals("deserialized amount should be equal", BigDecimal.valueOf(12.16), deserialisedAmount.getAmount());
    }
}
