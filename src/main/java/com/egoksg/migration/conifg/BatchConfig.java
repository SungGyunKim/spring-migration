package com.egoksg.migration.conifg;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j

@Configuration
public class BatchConfig {
	@Bean
	public Job importUserJob(JobRepository jobRepository, Step step1) {
	  return new JobBuilder("importUserJob", jobRepository)
	    .incrementer(new RunIdIncrementer())
	    .flow(step1)
	    .end()
	    .build();
	}
	
	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
	  return new StepBuilder("step1", jobRepository)
		.tasklet(new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				log.info("테스트!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
				return RepeatStatus.FINISHED;
			}
		}, transactionManager)
	    .build();
	}
}
