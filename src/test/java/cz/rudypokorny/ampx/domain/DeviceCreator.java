package cz.rudypokorny.ampx.domain;

import java.util.ArrayList;
import java.util.Random;

/**
 * Test util class that helps to create dummy test object
 */
public class DeviceCreator {

    public static Device createRandom() {
        return createWithId(new Random().nextLong());
    }

    public static Device createRandom(int datapointsCount) {
        var device = createRandom();
        for (int i = 0; i < datapointsCount; i++) {
            device.getDatapoints().add(DatapointCreator.createRandom());
        }
        return device;
    }

    public static Device createWithId(final Long id) {
        var device = new Device();
        device.setId(id);
        device.setDatapoints(new ArrayList<>());
        return device;
    }
}
