package com.touraj.priceproducer.main;

import com.touraj.priceproducer.common.LoadConfiguration;
import com.touraj.priceproducer.common.RabbitMqWrapper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by toraj on 08/25/2018.
 */
public class Send {


    public static void main(String[] args) {

        System.out.println("Sender Started ... ");

        new LoadConfiguration();
        ExecutorService executorService = Executors.newFixedThreadPool(Integer.parseInt(LoadConfiguration.getThreadPoolSize()));
        int numberOfProducers = Integer.parseInt(LoadConfiguration.getNumOfProducers());

        for (int i = 1; i <= numberOfProducers; i++) {
            executorService.execute(() -> {
                RabbitMqWrapper rabbitMqWrapper = new RabbitMqWrapper();
                rabbitMqWrapper.send();
            });
        }

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("All Producer Threads Executed!");
    }
}
