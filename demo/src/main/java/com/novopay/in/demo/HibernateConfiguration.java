package com.novopay.in.demo;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class HibernateConfiguration {

	@Value("${db_driver}")
	private String driver;

	@Value("${db_url}")
	private String url;

	@Value("${db_username}")
	private String username;

	@Value("${db_password}")
	private String password;

	@Value("${hibernate.dialect}")
	private String dialect;

	@Value("${hibernate.show_sql}")
	private String showsql;

	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2autoddl;

	@Value("${entitymanager.packagesToScan}")
	private String packagesToScan;

	@Value("${hibernate.jdbc.batch_size}")
	private String batchSize;
	
	

	@Bean
	public DataSource dataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;

	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {

		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(packagesToScan);

		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", dialect);
		hibernateProperties.put("hibernate.show_sql", showsql);
		hibernateProperties.put("hibernate.hbm2ddl.auto", hbm2autoddl);
		hibernateProperties.put("hibernate.jdbc.batch_size", batchSize);
		
		sessionFactory.setHibernateProperties(hibernateProperties);
		return sessionFactory;

	}

}
