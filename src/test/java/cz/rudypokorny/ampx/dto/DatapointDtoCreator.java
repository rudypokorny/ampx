package cz.rudypokorny.ampx.dto;

import java.util.Date;
import java.util.Random;

/**
 * Test util class that helps to create dummy test object
 */
public class DatapointDtoCreator {

    public static DatapointDto createRandom() {
        var result = new DatapointDto();
        var random = new Random();
        result.setDevice(random.nextLong());
        result.setUser(random.nextLong());
        result.setValue(Math.random());
        result.setId(random.nextLong());
        result.setTimestamp(new Date());
        return result;
    }
}
