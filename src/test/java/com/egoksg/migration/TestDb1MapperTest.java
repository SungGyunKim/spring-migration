package com.egoksg.migration;

import java.util.List;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;import org.springframework.boot.test.mock.mockito.ResetMocksTestExecutionListener;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.egoksg.migration.db1.mapper.TestDb1Mapper;
import com.egoksg.migration.db1.model.TestDb1Dto;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class TestDb1MapperTest {
	@Autowired
	private TestDb1Mapper testDb1Mapper;
	
	@Autowired
	@Qualifier("db1BatchSqlSessionTemplate")
	public SqlSession db1BatchSqlSession;
	
	@Test
	public void testDb1SelectTest() {
		log.info(testDb1Mapper.select());
	}
	
	@Test
	@Transactional
	@Commit// @Test에서 @Transactional에서 commit을 하지 않아 수동으로 해줘야 한다. ref) https://www.inflearn.com/questions/257700
	public void testDb1InsertBatchTest() {
		for (int i = 0; i < 1_000_000; i++) {
			db1BatchSqlSession.insert("com.egoksg.migration.db1.mapper.TestDb1Mapper.insert", TestDb1Dto.builder().name("BATCH").build());
		}
	}
	
}
