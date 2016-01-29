package com.example.jms.standard;

import javax.jms.Message;
import javax.jms.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobListener implements MessageListener {
    
    private static final Logger logger = LoggerFactory.getLogger(JobListener.class);
    
    public void onMessage(Message message) {
        try {
            int jobId = message.getIntProperty("Job_ID");
            logger.info("job {}",jobId);
    
        } catch (Exception e) {
            System.out.println("Worker caught an Exception");
        }
    }

	
}