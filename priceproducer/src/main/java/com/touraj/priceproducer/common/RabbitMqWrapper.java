package com.touraj.priceproducer.common;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.touraj.priceproducer.domain.ChunkOfData;
import com.touraj.priceproducer.domain.Command;
import com.touraj.priceproducer.domain.PriceData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by toraj on 08/25/2018.
 */
public class RabbitMqWrapper {

    private Channel channel;
    private Connection connection;

    public RabbitMqWrapper() {
        init();
    }

    public void send() {
        try {
            boolean durable = false;
            channel.queueDeclare(LoadConfiguration.getQueueName(), durable, false, false, null);
            UUID uuid = UUID.randomUUID();

            //Start batch with unique Batch ID
            sendBatchCommand(uuid, CommandType.batchStart);

            //Sending Batch of Data Divided to Chunks
            sendBatchData(uuid);

            //Batch Send Finished using Chunks
            //Send Batch Complete Command
            sendBatchCommand(uuid, CommandType.batchComplete);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Clean up");
            cleanUp(channel, connection);
        }
    }

    private void sendBatchData(UUID uuid) throws IOException {
        int numOfChunksInBatch = Integer.parseInt(LoadConfiguration.getNumOfChunksInBatch());
        for (int i = 1; i <= numOfChunksInBatch; i++) {
            Command commandData = new Command();
            commandData.setCommandType(CommandType.sendData);
            commandData.setBatchID(uuid);
            ChunkOfData chunkOfData = new ChunkOfData();
            createChunkOfData(chunkOfData);
            commandData.setChunkOfData(chunkOfData);
            publish(commandData);
        }
    }

    private void sendBatchCommand(UUID uuid, CommandType commandType) throws IOException {
        Command command = new Command();
        command.setCommandType(commandType);
        command.setBatchID(uuid);
        command.setChunkOfData(null);
        publish(command);
    }

    private void publish(Command command) throws IOException {
        String message = new Gson().toJson(command);
        channel.basicPublish("", LoadConfiguration.getQueueName(), null, message.getBytes("UTF-8"));
        System.out.println("Message Sent :: " + message);
    }

    private void createChunkOfData(ChunkOfData chunkOfData) {
        List<PriceData> priceDatas = new ArrayList<>();
        int chunkSize = Integer.parseInt(LoadConfiguration.getChunkSize());
        for (int i = 1; i <= chunkSize; i++) {
            PriceData priceData = new PriceData(Util.generateRandomID(), Util.generateRandomDate(), "payload" + i);
            priceDatas.add(priceData);
        }
        chunkOfData.setPriceDataList(priceDatas);
    }

    private static void cleanUp(Channel channel, Connection connection) {
        try {
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(LoadConfiguration.getRabbitmqIP());
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
