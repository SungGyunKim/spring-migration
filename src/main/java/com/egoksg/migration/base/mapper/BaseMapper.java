package com.egoksg.migration.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.egoksg.migration.base.model.BaseDto;

@Mapper
public interface BaseMapper {
	List<BaseDto> selectPagingDto();
	int insertAutoPkDto(BaseDto dto);
	int insertManualPkDto(BaseDto dto);
	int delete();
}
