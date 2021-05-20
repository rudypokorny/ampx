package cz.rudypokorny.ampx.domain;

import java.util.ArrayList;
import java.util.Random;

/**
 * Test util class that helps to create dummy test object
 */
public class UserCreator {

    public static User createRandom(int datapointsCount) {
        var user = createRandom();
        for (int i = 0; i < datapointsCount; i++) {
            user.getDatapoints().add(DatapointCreator.createRandom());
        }
        return user;
    }

    public static User createRandom() {
        return createWithId(new Random().nextLong());
    }

    public static User createWithId(final Long id){
        var user = new User();
        user.setDatapoints(new ArrayList<>());
        user.setId(id);
        return user;
    }
}
