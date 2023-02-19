package com.egoksg.migration.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.egoksg.migration.base.model.BaseDto;

@Mapper
public interface BaseMapper {
	List<BaseDto> selectPaging();
	int insertAutoPk(BaseDto dto);
	int insertManualPk(BaseDto dto);
}
