package com.example.jms;


import org.springframework.jms.annotation.JmsListener;

import com.example.configuration.ReceiveApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Receiver {	
        private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
        
	@JmsListener(destination=ReceiveApplication.DESC,concurrency="2")
	public void receiveMessage(String message) {
		logger.info("++++      Receiver : {}",message);
	}

}