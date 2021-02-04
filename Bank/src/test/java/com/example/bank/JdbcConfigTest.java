package com.example.bank;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.example.bank.dao.SpringJdbcConfig;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringJdbcConfig.class})
public class JdbcConfigTest {
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private Environment environment;
	
	@Test
	public void checkDbContext() {
		DriverManagerDataSource dataSource = (DriverManagerDataSource) applicationContext.getBean("dataSource");
		
		assertEquals("url should be like in properties", environment.getProperty("url"), dataSource.getUrl());
		//assertEquals("user should be like in properties", environment.getProperty("user"), dataSource.getUsername());
		//assertEquals("password should be like in properties", environment.getProperty("password"), dataSource.getPassword());
	}
}
