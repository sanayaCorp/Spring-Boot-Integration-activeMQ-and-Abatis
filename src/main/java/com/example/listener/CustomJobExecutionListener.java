/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.listener;

import java.util.Iterator;
import java.util.Map;
import javax.batch.runtime.JobExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;

/**
 *
 * @author afes
 */
public class CustomJobExecutionListener implements JobExecutionListener {
    
    protected static Logger log = LoggerFactory.getLogger(CustomJobExecutionListener.class);
    
    public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
		StringBuilder protocol = new StringBuilder();
		protocol.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
		protocol.append("Protocol for " + jobExecution.getJobInstance().getJobName() + " \n");
		protocol.append("  Started     : "+ jobExecution.getStartTime()+"\n");
		protocol.append("  Finished    : "+ jobExecution.getEndTime()+"\n");
		protocol.append("  Exit-Code   : "+ jobExecution.getExitStatus().getExitCode()+"\n");
		protocol.append("  Exit-Descr. : "+ jobExecution.getExitStatus().getExitDescription()+"\n");
		protocol.append("  Status      : "+ jobExecution.getStatus()+"\n");
		protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");

		protocol.append("Job-Parameter: \n");		
		JobParameters jp = jobExecution.getJobParameters();
		for (Iterator<Map.Entry<String,JobParameter>> iter = jp.getParameters().entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String,JobParameter> entry = iter.next();
			protocol.append("  "+entry.getKey()+"="+entry.getValue()+"\n");
		}
		protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");		
		
		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			protocol.append("\n+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
			protocol.append("Step " + stepExecution.getStepName() + " \n");
			protocol.append("ReadCount " + stepExecution.getReadCount() + " \n");
			protocol.append("WriteCount: " + stepExecution.getWriteCount() + "\n");
			protocol.append("Commits: " + stepExecution.getCommitCount() + "\n");
			protocol.append("SkipCount: " + stepExecution.getSkipCount() + "\n");
			protocol.append("Rollbacks: " + stepExecution.getRollbackCount() + "\n");
			protocol.append("Filter: " + stepExecution.getFilterCount() + "\n");					
			protocol.append("+++++++++++++++++++++++++++++++++++++++++++++++++++++++ \n");
		}
		log.info("{}", protocol.toString());
	}

	public void beforeJob(org.springframework.batch.core.JobExecution arg0) {
		// nothing to do
	}
}
