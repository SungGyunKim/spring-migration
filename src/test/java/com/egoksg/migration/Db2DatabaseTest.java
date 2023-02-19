package com.egoksg.migration;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import com.egoksg.migration.db2.mapper.Db2Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class Db2DatabaseTest {
	@Autowired
	private Db2Mapper db2Mapper;
	
	@Autowired
	@Qualifier("db2SqlSessionTemplate")
	public SqlSession db2SqlSession;
	
	@Autowired
	@Qualifier("db2BatchSqlSessionTemplate")
	public SqlSession db2BatchSqlSession;
}
