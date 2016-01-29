/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.configuration;

import com.example.DBConfig;
import com.example.listener.CustomJobExecutionListener;
import javax.inject.Inject;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author afes
 */
@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    
    @Inject
    DBConfig config;
    
    @Inject
    PlatformTransactionManager transactionManager;
    
    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }
    
    @Bean
    public LobHandler lobHandler() {
        return new DefaultLobHandler();
    }
    
    @Bean
    public JobRegistryBeanPostProcessor registryBeanPostProcessor() {
        JobRegistryBeanPostProcessor beanPostProcessor = new JobRegistryBeanPostProcessor();
        beanPostProcessor.setJobRegistry(jobRegistry());
        return beanPostProcessor;
    }
    
    public JobRepository getJobRepository() throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDataSource(config.dataSource());
		factory.setTransactionManager(transactionManager);
		factory.setLobHandler(lobHandler());
		factory.setDatabaseType(DatabaseType.fromMetaData(config.dataSource()).name());
		factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
		factory.afterPropertiesSet();
		return  (JobRepository) factory.getObject();
	}
		
	@Bean
	public JobLauncher jobLauncher() throws Exception{
		SimpleJobLauncher launcher = new SimpleJobLauncher();
		launcher.setJobRepository(getJobRepository());
		launcher.setTaskExecutor(new SyncTaskExecutor());
		return launcher;
	}
	
	@Bean
	public CustomJobExecutionListener customJobExecutionListener(){
		return new CustomJobExecutionListener();
	}
}
