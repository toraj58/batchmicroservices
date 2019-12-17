package com.touraj.priceproducer.common;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by toraj on 08/25/2018.
 */
public class Util {
    private static final int MIN_SENSOR_VALUE = 1;
    private static final int MAX_SENSOR_VALUE = 200;

    public static int generateRandomIntInRange(int min, int max) {
        int randomSensorValue = ThreadLocalRandom.current().nextInt(min, max);
        return randomSensorValue;
    }

    public static Date generateRandomDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        int randInt= generateRandomIntInRange(MIN_SENSOR_VALUE, MAX_SENSOR_VALUE);
        c.add(Calendar.DATE, randInt*-1);
        dt = c.getTime();
        return dt;
    }

    public static String generateRandomID() {
        String idPrefix = "id";
        int randInt= generateRandomIntInRange(1, 100);
        return idPrefix+randInt;
    }
}