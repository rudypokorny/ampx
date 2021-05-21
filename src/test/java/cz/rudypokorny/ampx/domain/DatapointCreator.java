package cz.rudypokorny.ampx.domain;

import java.util.Date;
import java.util.Random;

/**
 * Test util class that helps to create dummy test object
 */
public class DatapointCreator {

    public static Datapoint createRandom() {
        var result = new Datapoint();
        var random = new Random();
        result.setDevice(DeviceCreator.createRandom());
        result.setUser(UserCreator.createRandom());
        result.setValue(Math.random());
        result.setId(random.nextLong());
        result.setTimestamp(new Date());
        return result;
    }
}
