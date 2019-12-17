package com.touraj.priceproducer.domain;

import java.util.List;

/**
 * Created by toraj on 08/25/2018.
 */
public class ChunkOfData {

    List<PriceData> priceDataList;

    public List<PriceData> getPriceDataList() {
        return priceDataList;
    }

    public void setPriceDataList(List<PriceData> priceDataList) {
        this.priceDataList = priceDataList;
    }
}
