package com.egoksg.migration.base.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BaseDto implements Serializable {
	private static final long serialVersionUID = 1L;
	Integer no;
	String name;
}
