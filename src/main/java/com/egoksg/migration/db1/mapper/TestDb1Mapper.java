package com.egoksg.migration.db1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.egoksg.migration.db1.model.TestDb1Dto;

@Mapper
public interface TestDb1Mapper {
	List<TestDb1Dto> select();
	int insert(TestDb1Dto dto);
}
