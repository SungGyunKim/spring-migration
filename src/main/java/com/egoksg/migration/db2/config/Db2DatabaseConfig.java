package com.egoksg.migration.db2.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement
public class Db2DatabaseConfig {
	private final ApplicationContext applicationContext;
	
	@ConfigurationProperties(prefix = "spring.datasource.db2")
	@Bean(name = "db2DataSource")
	public DataSource dataSource() throws Exception {
		return DataSourceBuilder.create()
				.type(HikariDataSource.class)
				.build();
	}
	
	@Bean(name = "db2SqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("db2DataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setTypeAliasesPackage("com.egoksg.migration.db2.model");
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:mybatis/db2/*.xml"));

		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "db2SqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(@Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
	
	@Bean(name = "db2BatchSqlSessionTemplate")
	public SqlSessionTemplate batchSqlSessionTemplate(@Qualifier("db2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH);
	}
	
	@Bean(name = "db2TransactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("db2DataSource") DataSource db2DataSource) {
		return new DataSourceTransactionManager(db2DataSource);
	}
}
