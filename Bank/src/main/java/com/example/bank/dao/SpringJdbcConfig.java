package com.example.bank.dao;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@ComponentScan("com.example.bank")
@PropertySource("classpath:database.properties")
@RequiredArgsConstructor
public class SpringJdbcConfig {
	private final String URL = "url";
	private final String USER = "username";
	private final String DRIVER = "driverClassName";
	private final String PASSWORD = "password";

	private final Environment environment;
	
    @Bean
    public DataSource dataSource() {
    	DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setUrl(environment.getProperty(URL));
		driverManagerDataSource.setUsername(environment.getProperty(USER));
		driverManagerDataSource.setPassword(environment.getProperty(PASSWORD));
		driverManagerDataSource.setDriverClassName(environment.getProperty(DRIVER));
		return driverManagerDataSource;
    }
}
