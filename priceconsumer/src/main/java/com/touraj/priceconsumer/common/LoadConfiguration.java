package com.touraj.priceconsumer.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by toraj on 08/25/2018.
 */
public class LoadConfiguration {

    private static String queueName;
    private static String rabbitmqIP;
    private static String numOfConsumers;
    private static String threadPoolSize;
    private static final String configFileName = "config.properties";

    public LoadConfiguration() {
        readAndLoadConfigs();
    }

    private void readAndLoadConfigs() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = getClass().getClassLoader().getResourceAsStream(configFileName);
            prop.load(input);
            setQueueName(prop.getProperty("queuename"));
            setRabbitmqIP(prop.getProperty("rabbitmqip"));
            setNumOfConsumers(prop.getProperty("numofconsumers"));
            setThreadPoolSize(prop.getProperty("threadpoolsize"));

            System.out.println("cons :: " + prop.getProperty("numofconsumers"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getQueueName() {
        return queueName;
    }

    public static void setQueueName(String queueName) {
        LoadConfiguration.queueName = queueName;
    }

    public static String getRabbitmqIP() {
        return rabbitmqIP;
    }

    public static void setRabbitmqIP(String rabbitmqIP) {
        LoadConfiguration.rabbitmqIP = rabbitmqIP;
    }

    public static String getConfigFileName() {
        return configFileName;
    }

    public static String getThreadPoolSize() {
        return threadPoolSize;
    }

    public static void setThreadPoolSize(String threadPoolSize) {
        LoadConfiguration.threadPoolSize = threadPoolSize;
    }

    public static String getNumOfConsumers() {
        return numOfConsumers;
    }

    public static void setNumOfConsumers(String numOfConsumers) {
        LoadConfiguration.numOfConsumers = numOfConsumers;
    }
}
