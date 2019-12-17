package com.touraj.priceconsumer.common;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import com.touraj.priceconsumer.domain.Cache;
import com.touraj.priceconsumer.domain.Command;
import com.touraj.priceconsumer.domain.TempCache;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by toraj on 08/25/2018.
 */
public class AMQPConsumer {

    public void startConsumer() {

        try {
            final Channel channel = createConnectionAndChannel();
            boolean durable = false;
            channel.queueDeclare(LoadConfiguration.getQueueName(), durable, false, false, null);
            System.out.println("New Consumer Started to Receive price Data...");
            Gson gson = new Gson();

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                        throws IOException {
                    String message = new String(body, "UTF-8");

                    System.out.println("Thread name : " + Thread.currentThread().getName());
                    System.out.println("Received :: " + message);

                    //Touraj: I use Gson for Marshaling/ Unmarshaling data to DTO objects as Domain model
                    Command command = gson.fromJson(message, Command.class);

                    if (command.getCommandType() == CommandType.batchStart) {
                        System.out.println("Batch Started with id: " + command.getBatchID());
                    } else if (command.getCommandType() == CommandType.sendData) {
                        // Catch temporarily for future Commit
                        TempCache.updateTempCache(command);
                    } else if (command.getCommandType() == CommandType.batchComplete) {
                        //Commit Data for Compeleted Batch
                        Cache.commitData(command.getBatchID());
                    } else if (command.getCommandType() == CommandType.batchCanceled) {
                        //Discard data for canceled batch
                        TempCache.discardBatchData(command.getBatchID());
                    } else {
                        System.out.println("Unknown Command!");
                    }

                    System.out.println("Command is :: " + command.toString());
                }
            };

            boolean autoAck = true;
            channel.basicConsume(LoadConfiguration.getQueueName(), autoAck, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Channel createConnectionAndChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(LoadConfiguration.getRabbitmqIP());
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }
}
