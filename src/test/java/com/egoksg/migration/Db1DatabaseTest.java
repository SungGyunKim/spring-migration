package com.egoksg.migration;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import com.egoksg.migration.db1.mapper.Db1Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class Db1DatabaseTest {
	@Autowired
	private Db1Mapper db1Mapper;
	
	@Autowired
	@Qualifier("db1SqlSessionTemplate")
	public SqlSession db1SqlSession;
	
	@Autowired
	@Qualifier("db1BatchSqlSessionTemplate")
	public SqlSession db1BatchSqlSession;
}
