package com.touraj.priceconsumer;

import com.touraj.priceconsumer.common.CommandType;
import com.touraj.priceconsumer.domain.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by toraj on 08/26/2018.
 */
public class TestPriceConsumer {

    @Test
    public void testTempCacheUpdate() {
        UUID uuid = UUID.randomUUID();
        Command command = generateCommandWithData(uuid);
        TempCache.updateTempCache(command);
        Assert.assertEquals(3, TempCache.getTempData().get(uuid).size());
        TempCache.getTempData().remove(uuid);
    }

    @Test
    public void testTempCacheUpdate_SimulateMultipleBatch() {
        UUID uuid = UUID.randomUUID();
        Command command = generateCommandWithData(uuid);
        TempCache.updateTempCache(command);
        UUID uuid2 = UUID.randomUUID();
        Command command2 = generateCommandWithData(uuid2);
        TempCache.updateTempCache(command2);
        Assert.assertEquals(2, TempCache.getTempData().size());
        TempCache.getTempData().remove(uuid);
        TempCache.getTempData().remove(uuid2);
    }

    @Test
    public void testCacheCommit() {
        UUID uuid = UUID.randomUUID();
        Command command = generateCommandWithData(uuid);
        TempCache.updateTempCache(command);
        Cache.commitData(uuid);
        Assert.assertEquals(3, Cache.getPriceDataMap().size());
        TempCache.getTempData().remove(uuid);
    }

    @Test
    public void testCacheFindDataByID() {
        UUID uuid = UUID.randomUUID();
        Command command = generateCommandWithData(uuid);
        TempCache.updateTempCache(command);
        Cache.commitData(uuid);
        PriceData priceData =  Cache.getLastValuePriceByID("id1");
        Assert.assertNotNull(priceData);
        TempCache.getTempData().remove(uuid);
    }

    @Test
    public void testCacheFindDataByIdNotFound() {
        UUID uuid = UUID.randomUUID();
        Command command = generateCommandWithData(uuid);
        TempCache.updateTempCache(command);
        Cache.commitData(uuid);
        PriceData priceData =  Cache.getLastValuePriceByID("dafdsd");
        Assert.assertNull(priceData);
        TempCache.getTempData().remove(uuid);
    }

    @Test
    public void testTempCacheDiscardData() {
        UUID uuid = UUID.randomUUID();
        Command command = generateCommandWithData(uuid);
        TempCache.updateTempCache(command);
        TempCache.discardBatchData(uuid);
        List<PriceData> priceDatas = TempCache.getTempData().get(uuid);
        Assert.assertNull(priceDatas);
    }

    private Command generateCommandWithData(UUID uuid) {
        Command command = new Command();
        CommandType commandType = CommandType.sendData;
        ChunkOfData chunkOfData = new ChunkOfData();
        List<PriceData> priceDataList = new ArrayList<>();

        PriceData priceData1 = new PriceData();
        PriceData priceData2 = new PriceData();
        PriceData priceData3 = new PriceData();

        priceData1.setId("id1");
        priceData1.setAsOf(new Date());
        priceData1.setPayload("payload1");

        priceData2.setId("id2");
        priceData2.setAsOf(new Date());
        priceData2.setPayload("payload2");

        priceData3.setId("id3");
        priceData3.setAsOf(new Date());
        priceData3.setPayload("payload3");

        priceDataList.add(priceData1);
        priceDataList.add(priceData2);
        priceDataList.add(priceData3);

        chunkOfData.setPriceDataList(priceDataList);

        command.setBatchID(uuid);
        command.setChunkOfData(chunkOfData);
        command.setCommandType(commandType);
        return command;
    }
}
