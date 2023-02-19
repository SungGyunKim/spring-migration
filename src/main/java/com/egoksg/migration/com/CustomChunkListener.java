package com.egoksg.migration.com;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomChunkListener implements ChunkListener {
	@Override
    public void beforeChunk(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();
        
        log.info("###### step = {}, read = {}", stepExecution.getStepName(), stepExecution.getReadCount());
    }
 
    @Override
    public void afterChunk(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();
        
        log.info("###### step = {}, commit = {}", stepExecution.getStepName(), stepExecution.getCommitCount());
    }
 
    @Override
    public void afterChunkError(ChunkContext context) {
        StepContext stepContext = context.getStepContext();
        StepExecution stepExecution = stepContext.getStepExecution();
        
        log.info("###### step = {}, rollback = {}", stepExecution.getStepName(), stepExecution.getRollbackCount());
    }
}
