package com.egoksg.migration.base.config;

import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.egoksg.migration.base.mapper.BaseMapper;
import com.egoksg.migration.base.model.BaseDto;
import com.egoksg.migration.com.CustomChunkListener;
import com.egoksg.migration.com.MigrationStepBuilderUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class BaseBatchConfig {
	public final CustomChunkListener customChunkListener;
	
	@JobScope
	@Bean
	public Step deleteStep(
			JobRepository jobRepository, PlatformTransactionManager transactionManager, BaseMapper baseMapper
	) {
		return new StepBuilder("step0", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					baseMapper.delete();
					return RepeatStatus.FINISHED;
				}, transactionManager)
				.build();
	}
	
	@Bean("baseDtoJob")
	public Job baseDtoJob(JobRepository jobRepository, Step deleteStep, Step baseDtoStep) {
		return new JobBuilder("baseDtoJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(deleteStep)
				.on("COMPLETED")
				.to(baseDtoStep)
			.end()
			.build();
	}
	
	// DTO 방식을 사용하면 모든 테이블에 대해 DTO를 만들어야 하므로 번거롭다.
	@JobScope
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
				.queryId("com.egoksg.migration.base.mapper.BaseMapper.selectPagingDto")
				.build();
		MyBatisBatchItemWriter<BaseDto> writer = new MyBatisBatchItemWriterBuilder<BaseDto>()
				.sqlSessionTemplate(batchSqlSessionTemplate)
				.statementId("com.egoksg.migration.base.mapper.BaseMapper.insertManualPkDto")
				.build();
		
		return new StepBuilder("step1", jobRepository)
				.<BaseDto, BaseDto> chunk(dataSize, transactionManager)
				.listener(customChunkListener)
				.reader(reader)
				.writer(writer)
				.build();
	}
	
	@Bean("baseMapJob")
	public Job baseMapJob(JobRepository jobRepository, Step deleteStep, Step baseMapStep) {
		return new JobBuilder("baseMapJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(deleteStep)
				.on("COMPLETED")
				.to(baseMapStep)
			.end()
			.build();
	}
	
	@JobScope
	@Bean
	public Step baseMapStep(
			JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory,
			@Qualifier("batchSqlSessionTemplate") SqlSessionTemplate batchSqlSessionTemplate
	) {
		int dataSize = 10_000;
		MyBatisPagingItemReader<Map<String, ?>> reader = new MyBatisPagingItemReaderBuilder<Map<String, ?>>()
				.sqlSessionFactory(sqlSessionFactory)
				.pageSize(dataSize)
				.queryId("com.egoksg.migration.base.mapper.BaseMapper.selectPagingMap")
				.build();
		MyBatisBatchItemWriter<Map<String, ?>> writer = new MyBatisBatchItemWriterBuilder<Map<String, ?>>()
				.sqlSessionTemplate(batchSqlSessionTemplate)
				.statementId("com.egoksg.migration.base.mapper.BaseMapper.insertManualPkMap")
				.build();
		
		return new StepBuilder("step1", jobRepository)
				.<Map<String, ?>, Map<String, ?>> chunk(dataSize, transactionManager)
				.listener(customChunkListener)
				.reader(reader)
				.writer(writer)
				.build();
	}
	
	@Bean("baseMapJobByUtil1")
	public Job baseMapJobByUtil(JobRepository jobRepository, Step deleteStep, Step baseMapStepByUtil) {
		return new JobBuilder("baseMapJobByUtil", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(deleteStep)
				.on("COMPLETED")
				.to(baseMapStepByUtil)
			.end()
			.build();
	}
	
	@JobScope
	@Bean
	public Step baseMapStepByUtil(
			JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory,
			@Qualifier("batchSqlSessionTemplate") SqlSessionTemplate batchSqlSessionTemplate
	) {
		return MigrationStepBuilderUtil.<Map<String, ?>, Map<String, ?>>getMyBatisChunkStepBuilder(
				jobRepository,
				transactionManager, sqlSessionFactory, batchSqlSessionTemplate,
				customChunkListener,
				"step1", 10_000,
				"com.egoksg.migration.base.mapper.BaseMapper.selectPagingMap",
				"com.egoksg.migration.base.mapper.BaseMapper.insertManualPkMap"
		).build();
	}
	
	@Bean("baseMapJobByUtil2")
	public Job baseMapJobByUtil2(JobRepository jobRepository, PlatformTransactionManager transactionManager,
			@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory,
			@Qualifier("batchSqlSessionTemplate") SqlSessionTemplate batchSqlSessionTemplate
	) {
		Step deleteStep = MigrationStepBuilderUtil.getDeleteStepBuilder(
				jobRepository, transactionManager,
				batchSqlSessionTemplate,
				"com.egoksg.migration.base.mapper.BaseMapper.delete"
		).build();
		Step baseMapStepByUtil = MigrationStepBuilderUtil.<Map<String, ?>, Map<String, ?>>getMyBatisChunkStepBuilder(
				jobRepository, transactionManager,
				sqlSessionFactory, batchSqlSessionTemplate,
				customChunkListener,
				"step1", 10_000,
				"com.egoksg.migration.base.mapper.BaseMapper.selectPagingMap",
				"com.egoksg.migration.base.mapper.BaseMapper.insertManualPkMap"
		).build();
		
		return new JobBuilder("baseMapJobByUtil2", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(deleteStep)
				.on("COMPLETED")
				.to(baseMapStepByUtil)
			.end()
			.build();
	}
	
}
