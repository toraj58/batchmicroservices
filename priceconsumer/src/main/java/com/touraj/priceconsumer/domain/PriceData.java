package com.touraj.priceconsumer.domain;

import java.util.Date;

/**
 * Created by toraj on 08/25/2018.
 */
public class PriceData {

    String id;
    Date asOf;
    String payload;

    public PriceData(String id, Date asOf, String payload) {
        this.id = id;
        this.asOf = asOf;
        this.payload = payload;
    }

    public PriceData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getAsOf() {
        return asOf;
    }

    public void setAsOf(Date asOf) {
        this.asOf = asOf;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PriceData priceData = (PriceData) o;

        if (!id.equals(priceData.id)) return false;
        if (!asOf.equals(priceData.asOf)) return false;
        return payload.equals(priceData.payload);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + asOf.hashCode();
        result = 31 * result + payload.hashCode();
        return result;
    }
}
