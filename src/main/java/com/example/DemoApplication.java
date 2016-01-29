package com.example;

import com.example.configuration.JmsConfig;
import com.example.configuration.ReceiveApplication;
import com.example.jms.QueueListener;
import com.example.jms.QueueSender;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class DemoApplication {
    
     private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
     
     static Random rand = new Random();
     static AtomicInteger count = new AtomicInteger();

	public static void main(String[] args) throws InterruptedException {
                if (args.length == 0) {
			args =   new String[]{ "date=" + getCurrentDate()};
		}
		SpringApplication.run(DemoApplication.class, args);
                
                ApplicationContext context = new AnnotationConfigApplicationContext(JmsConfig.class);
                ExecutorService threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 5);
                QueueSender queueSender = context.getBean(QueueSender.class);
                QueueListener ql = context.getBean(QueueListener.class);

                DefaultMessageListenerContainer listener = (DefaultMessageListenerContainer) context.getBean(DefaultMessageListenerContainer.class);

                    //fillQueue(queueSender, threadPool, 1000 * 1);
                    logger.info("fill messages :  {}", count);
                    logger.info("ConcurrentConsumers : {}", listener.getConcurrentConsumers());
                    logger.info("ActiveConsumerCount : {}", listener.getActiveConsumerCount());
                    logger.info("MaxConcurrentConsumers : {}", listener.getMaxConcurrentConsumers());
                    System.err.println(ql.getCount());
                    mailBoxSender(context);
                    Thread.sleep(1000);
                System.exit(0);
	}
        
        static void mailBoxSender(ApplicationContext context){
            MessageCreator messageCreator = new MessageCreator() {
                @Override
                public Message createMessage(Session session) throws JMSException {
                   return session.createTextMessage("przodownik_test");
                }
             };
             JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

             jmsTemplate.send(ReceiveApplication.DESC, messageCreator);

        }

        private static void fillQueue(final QueueSender queueSender, ExecutorService threadPool, int millis) {
            long time = System.currentTimeMillis() + millis;

            while (System.currentTimeMillis() < time) {
                threadPool.execute(new Runnable() {
                    public void run() {
                        queueSender.send("sample demo " + rand.nextInt());
                        count.incrementAndGet();
                    }
                });
            }

        }
        
        private static String getCurrentDate() {
		TimeZone zone = TimeZone.getTimeZone("Asia/jakarta");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		format.setTimeZone(zone);
		
		Calendar calendar = Calendar.getInstance();
		return format.format(calendar.getTime());
	}
}
