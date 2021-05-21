package cz.rudypokorny.ampx;

import cz.rudypokorny.ampx.domain.Datapoint;
import cz.rudypokorny.ampx.domain.Device;
import cz.rudypokorny.ampx.domain.User;
import cz.rudypokorny.ampx.dto.DatapointDto;
import cz.rudypokorny.ampx.dto.mapping.DatapointMapper;
import cz.rudypokorny.ampx.repository.DatapointRepository;
import cz.rudypokorny.ampx.repository.DeviceRepository;
import cz.rudypokorny.ampx.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Development utility to generate 100 users and Devices. Sequences are starting at 1, so first user/device  will be having id 0, last 100.
 */
@Component
@Profile(value = "dev")
public class DummyDataInitializr implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DummyDataInitializr.class);
    private static final int DATAPOINT_MULTIPLIER = 10;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DatapointRepository datapointRepository;
    @Autowired
    private DatapointMapper datapointMapper;
    @Value("${ampx.development.testData.count:0}")
    private int dummyDataCount;
    @Value("${server.port}")
    private int serverPort;
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) {
        logger.info("Start generating test data.");

        //create N users & devices
        for (int i = 1; i <= dummyDataCount; i++) {
            var user = userRepository.save(new User());
            var device = deviceRepository.save(new Device());

            //for each device create j datapoints, eech with timestamp-j minutes, and randomly chose from the users already created
            for (int j = 1; j <= computeDatapointCount(); j++) {
                var time = Instant.now().minus(j, ChronoUnit.MINUTES);

                var datapoint = Datapoint.builder()
                        .withDevice(device)
                        .withUser(user)
                        .withValue(Math.random())
                        .withTimestamo(Date.from(time))
                        .build();

                //call its own POST api
                sentToRest(datapoint);
                //OR inject directly to database
                //injectToDb(datapoint);
            }
        }
        logger.info("Finished generating test data. User and Device count {}. Datapoints for each device-user pair: {}", dummyDataCount, computeDatapointCount());
    }

    private int computeDatapointCount() {
        return dummyDataCount * DATAPOINT_MULTIPLIER;
    }

    private void injectToDb(Datapoint datapoint) {
        logger.debug("Injecting to DB: {}", datapoint);
        datapointRepository.save(datapoint);
    }

    private void sentToRest(Datapoint datapoint) {
        logger.debug("Sending to REST api: {}", datapoint);
        restTemplate.postForEntity(constructUri(), datapointMapper.toDto(datapoint), DatapointDto.class);
    }

    private String constructUri() {
        return new StringBuilder().append("http://localhost:").append(serverPort).append("/datapoints").toString();
    }
}
