package com.egoksg.migration;

import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.egoksg.migration.db1.mapper.TestDb1Mapper;
import com.egoksg.migration.db1.model.TestDb1Dto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class TestDb1MapperTest {
	@Autowired
	private TestDb1Mapper testDb1Mapper;
	
	@Autowired
	@Qualifier("db1BatchSqlSessionTemplate")
	public SqlSession db1BatchSqlSession;
	
	@DisplayName("Single Insert")
	@Test
	public void testDb1InsertTest() {
		testDb1Mapper.insert(TestDb1Dto.builder().name("INSERT").build());
	}
	
	@DisplayName("Bulk Insert")
	@Test
	@Transactional
	@Commit// @Test에서 @Transactional에서 commit을 하지 않아 수동으로 해줘야 한다. ref) https://www.inflearn.com/questions/257700
	public void testDb1InsertBatchTest() {
		for (int i = 0; i < 1_000_000; i++) {
			db1BatchSqlSession.insert("com.egoksg.migration.db1.mapper.TestDb1Mapper.insert", TestDb1Dto.builder().name("BATCH").build());
		}
	}
	
}
