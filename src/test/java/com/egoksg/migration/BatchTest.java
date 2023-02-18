package com.egoksg.migration;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest
public class BatchTest {
	@Autowired
	public JobLauncherTestUtils jobLauncherTestUtils;
	
	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;
	
	@Test
	public void jobExecute() throws Exception {
		// given
		JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParameters();
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
		// then
		jobExecution.getExitStatus();
	}
}
