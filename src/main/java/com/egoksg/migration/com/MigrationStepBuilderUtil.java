package com.egoksg.migration.com;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.mybatis.spring.batch.MyBatisPagingItemReader;
import org.mybatis.spring.batch.builder.MyBatisBatchItemWriterBuilder;
import org.mybatis.spring.batch.builder.MyBatisPagingItemReaderBuilder;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.TaskletStepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.transaction.PlatformTransactionManager;

import com.egoksg.migration.base.mapper.BaseMapper;

public class MigrationStepBuilderUtil {
	public static TaskletStepBuilder getDeleteStepBuilder(
			JobRepository jobRepository, PlatformTransactionManager transactionManager,
			SqlSessionTemplate sqlSessionTemplate,
			String deleteQueryId
	) {
		return new StepBuilder("step0", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					sqlSessionTemplate.delete(deleteQueryId);
					return RepeatStatus.FINISHED;
				}, transactionManager);
	}
	
	public static <R, W> SimpleStepBuilder<R, W> getMyBatisChunkStepBuilder(
			JobRepository jobRepository, PlatformTransactionManager transactionManager,
			SqlSessionFactory sqlSessionFactory, SqlSessionTemplate batchSqlSessionTemplate,
			ChunkListener chunkListener,
			String stepName, int dataSize, String readQueryId, String writeQueryId
	) {
		MyBatisPagingItemReader<R> reader = new MyBatisPagingItemReaderBuilder<R>()
				.sqlSessionFactory(sqlSessionFactory)
				.pageSize(dataSize)
				.queryId("com.egoksg.migration.base.mapper.BaseMapper.selectPagingMap")
				.build();
		MyBatisBatchItemWriter<W> writer = new MyBatisBatchItemWriterBuilder<W>()
				.sqlSessionTemplate(batchSqlSessionTemplate)
				.statementId("com.egoksg.migration.base.mapper.BaseMapper.insertManualPkMap")
				.build();
		
		return new StepBuilder(stepName, jobRepository)
				.<R, W> chunk(dataSize, transactionManager)
				.listener(chunkListener)
				.reader(reader)
				.writer(writer);
	}
}
