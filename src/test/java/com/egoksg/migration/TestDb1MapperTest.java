package com.egoksg.migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.egoksg.migration.db1.mapper.TestDb1Mapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
public class TestDb1MapperTest {
	@Autowired
	private TestDb1Mapper testDb1Mapper;
	
	@Test
	public void testDb1SelectTest() {
		log.info(testDb1Mapper.select());
	}
}
