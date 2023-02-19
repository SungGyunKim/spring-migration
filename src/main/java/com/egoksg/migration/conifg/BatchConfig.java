package com.egoksg.migration.conifg;

import org.springframework.context.annotation.Configuration;

import com.egoksg.migration.com.CustomChunkListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {
	public final CustomChunkListener customChunkListener;
}
