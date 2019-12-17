package com.touraj.priceconsumer.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by toraj on 08/26/2018.
 */
public class TempCache {

    private static Map<UUID, List<PriceData>> tempData = new ConcurrentHashMap<>();

    public static Map<UUID, List<PriceData>> getTempData() {
        return tempData;
    }

    public static void setTempData(Map<UUID, List<PriceData>> tempData) {
        TempCache.tempData = tempData;
    }

    public synchronized static void updateTempCache(Command command)
    {
        // Critical Section
        try {
            tempData.computeIfPresent(command.getBatchID(), (k,v) -> {
                v.addAll(command.getChunkOfData().getPriceDataList());
                return v;
            });

            tempData.computeIfAbsent(command.getBatchID(),
                    (k) -> command.getChunkOfData().getPriceDataList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Following Lines Can be Uncommented for Debugging
//        System.out.println(">TempCache Size = " + tempData.size());
//        System.out.println(">TempCache List Size = " + tempData.get(command.getBatchID()).size() + " :: " + command.getBatchID());
    }

    public static void discardBatchData(UUID uuid) {
        tempData.remove(uuid);
    }
}
