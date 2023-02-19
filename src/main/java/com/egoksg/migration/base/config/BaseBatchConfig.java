package com.egoksg.migration.base.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.egoksg.migration.base.model.BaseDto;
import com.egoksg.migration.com.CustomChunkListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Configuration
public class BaseBatchConfig {
	public final CustomChunkListener customChunkListener;
	
	@Bean
	public Job baseJob(JobRepository jobRepository, Step dtoStep) {
		return new JobBuilder("BaseJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.flow(dtoStep)
			.end()
			.build();
	}
	
	@Bean
	public Step baseDtoStep(
			JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory,
			@Qualifier("batchSqlSessionTemplate") SqlSessionTemplate batchSqlSessionTemplate
	) {
		int dataSize = 10_000;
		MyBatisPagingItemReader<BaseDto> reader = new MyBatisPagingItemReaderBuilder<BaseDto>()
				.sqlSessionFactory(sqlSessionFactory)
				.pageSize(dataSize)
				.queryId("com.egoksg.migration.base.mapper.BaseMapper.selectPaging")
				.build();
		MyBatisBatchItemWriter<BaseDto> writer = new MyBatisBatchItemWriterBuilder<BaseDto>()
				.sqlSessionTemplate(batchSqlSessionTemplate)
				.statementId("com.egoksg.migration.base.mapper.BaseMapper.insertManualPk")
				.build();
		
		return new StepBuilder("step1", jobRepository)
				.<BaseDto, BaseDto> chunk(dataSize, transactionManager)
				.listener(customChunkListener)
				.reader(reader)
				.writer(writer)
				.build();
	}
}
