package com.touraj.priceconsumer.main;

import com.touraj.priceconsumer.common.AMQPConsumer;
import com.touraj.priceconsumer.common.LoadConfiguration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by toraj on 08/26/2018.
 */
@WebListener
public class Main implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        System.out.println("Starting LastValuePrice Service...");

        new LoadConfiguration();
        ExecutorService executorService = Executors.newFixedThreadPool(Integer.parseInt(LoadConfiguration.getThreadPoolSize()));
        int numberOfConsumers = Integer.parseInt(LoadConfiguration.getNumOfConsumers());

        for (int i = 1; i <= numberOfConsumers; i++) {
            executorService.execute(() -> {
                AMQPConsumer amqpConsumer = new AMQPConsumer();
                amqpConsumer.startConsumer();
            });
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Consumer Listeners started and Waiting for batch data...");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Application Stopped!");
    }
}
