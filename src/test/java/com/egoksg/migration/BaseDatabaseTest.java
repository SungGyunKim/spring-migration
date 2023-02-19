package com.egoksg.migration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.egoksg.migration.base.mapper.BaseMapper;
import com.egoksg.migration.base.model.BaseDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBatchTest
@SpringBootTest
public class BaseDatabaseTest {
	@Autowired
	private BaseMapper baseMapper;
	
	@Autowired
	@Qualifier("sqlSessionTemplate")
	public SqlSession sqlSessionTemplate;
	
	@Autowired
	@Qualifier("batchSqlSessionTemplate")
	public SqlSession batchSqlSession;
	
	@Autowired
	public JobLauncherTestUtils jobLauncherTestUtils;
	
	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;
	
	@BeforeEach
    public void clearMetadata() {
        jobRepositoryTestUtils.removeJobExecutions();
    }
	
	@DisplayName("Single Insert Test")
	@Test
	public void singleInsertTest() {
		baseMapper.insertAutoPk(BaseDto.builder().name("INSERT").build());
	}
	
	@DisplayName("Bulk Insert Test")
	@Test
	@Transactional
	@Commit // @Test에서 @Transactional은 자동으로 commit을 하지 않아 수동으로 해줘야 한다. ref) https://www.inflearn.com/questions/257700
	public void bulkInsertTest() {
		for (int i = 0; i < 1_000_000; i++) {
			batchSqlSession.insert("com.egoksg.migration.base.mapper.BaseMapper.insertAutoPk", BaseDto.builder().name("BATCH").build());
		}
	}
	
	@DisplayName("Batch Test")
	@Test
	public void jobExecuteTest() throws Exception {
		// given
		JobParameters jobParameters = jobLauncherTestUtils.getUniqueJobParameters();
		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
		// then
		assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}
}
