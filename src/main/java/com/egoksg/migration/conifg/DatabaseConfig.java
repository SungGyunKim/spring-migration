package com.egoksg.migration.conifg;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class DatabaseConfig {
	private final ApplicationContext applicationContext;
	
	@Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    @Bean(name = "dataSource")
    public DataSource dataSource() throws Exception {
    	return DataSourceBuilder.create()
    			.type(HikariDataSource.class)
    			.build();
    }
    
	@Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("db1DataSource") DataSource db1DataSource) {
        return new DataSourceTransactionManager(db1DataSource);
    }
}

