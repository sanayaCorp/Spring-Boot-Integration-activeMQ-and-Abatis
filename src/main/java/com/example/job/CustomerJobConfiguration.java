/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.job;

import com.example.JobUtil;
import com.example.configuration.BatchConfiguration;
import com.example.domain.Customer;
import com.example.processor.CustomerJobProcessor;
import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 *
 * @author afes
 */
@Configuration
public class CustomerJobConfiguration {
    
    @Autowired
    private StepBuilderFactory stepBuilders;

    @Autowired
    private JobBuilderFactory jobBuilders;

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private BatchConfiguration batchConfig;
    
    @Inject
    private Environment environment;
    
    @Bean(name="customerXmlReader")
    public ItemReader<Customer> reader() {
            StaxEventItemReader<Customer> reader = new StaxEventItemReader<Customer>();
            String resourcePath = environment.getRequiredProperty("resource.input.customer");		
            Resource resource = JobUtil.getResource(resourcePath);	
            reader.setResource(resource); 		
            reader.setFragmentRootElementName("customer");
            reader.setUnmarshaller(itemMarshaller());
            return reader;
    }

    @Bean(name="customerMarshaller")
    public Unmarshaller itemMarshaller() {
            Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
            marshaller.setClassesToBeBound(Customer.class);
            return marshaller;
    }



    @Bean(name="customerXmlToDbJob")
    public Job job() {
            return jobBuilders.get("customerXmlToDbJob")		
                    .listener(batchConfig.customJobExecutionListener())
                    .start(step())
                    .build();
    }

    @Bean(name="customerXmlToDbStep")
    public Step step() {
            return stepBuilders.get("customerXmlToDbStep").<Customer, Customer> chunk(420)
                    .reader(reader())		
                    .processor(new CustomerJobProcessor())
                    .writer(writer())	
                    .build();
    }

    @Bean(name="customerDbWriter")
    public ItemWriter<Customer> writer() {
            JpaItemWriter<Customer> writer = new JpaItemWriter<Customer>();
            writer.setEntityManagerFactory(emf);
            return writer;
    }
}
