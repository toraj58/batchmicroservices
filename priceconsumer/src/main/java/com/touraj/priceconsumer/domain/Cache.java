package com.touraj.priceconsumer.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by toraj on 08/26/2018.
 */
public class Cache {

    private static Map<String, PriceData> priceDataMap = new ConcurrentHashMap<>();

    public static Map<String, PriceData> getPriceDataMap() {
        return priceDataMap;
    }

    public static void setPriceDataMap(Map<String, PriceData> priceDataMap) {
        Cache.priceDataMap = priceDataMap;
    }

    public synchronized static void commitData(UUID uuid)
    {
        try {
            List<PriceData> priceDatas = TempCache.getTempData().get(uuid);

            priceDatas.stream().forEach((p) -> {
                priceDataMap.computeIfPresent(p.getId(), (k,v) ->
                {
                    if (p.getAsOf().after(v.getAsOf())) {
                        return p;
                    } else {
                        return v;
                    }
                });

                priceDataMap.computeIfAbsent(p.getId(), (k) -> p);

            });

            //Flush Commited Data
            TempCache.getTempData().remove(uuid);

            // Following Lines Can be uncommented for Debugging
//            System.out.println(">> Real Cache Size : " + priceDataMap.size());
//            priceDataMap.forEach((k,v) -> System.out.println("key: " + k + ":: val: " + v.getAsOf()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static PriceData getLastValuePriceByID(String id)
    {
        return priceDataMap.get(id);
    }
}