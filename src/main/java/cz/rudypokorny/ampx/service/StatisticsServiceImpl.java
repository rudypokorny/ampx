package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.Statistics;
import cz.rudypokorny.ampx.dto.StatisticsDto;
import cz.rudypokorny.ampx.exceptions.DeviceNotFoundException;
import cz.rudypokorny.ampx.exceptions.UserNotFoundException;
import cz.rudypokorny.ampx.repository.DatapointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Value("${ampx.statistics.defaultAverageTimeFrame:15}")
    private int defaultAverageFrame;

    @Autowired
    private DatapointRepository datapointRepository;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public List<StatisticsDto> getAveragesForDevice(final Long deviceId) {
        Objects.requireNonNull(deviceId, "deviceId cannot be null");

        logger.info("Computing Datapoint averages for device id {}, time frame {} minutes", deviceId, defaultAverageFrame);
        var device = deviceService.findDevice(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));

        var minDate = datapointRepository.findMinDateForDevice(device);
        return computeStatistics("device", device.getId(), minDate,
                dateRange -> datapointRepository.findAverageForDevice(device.getId(), Date.from(dateRange.from), Date.from(dateRange.to)));
    }

    @Override
    public List<StatisticsDto> getAveragesForUser(final Long userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        logger.info("Computing Datapoint averages for user id {}, time frame {} minutes", userId, defaultAverageFrame);
        var user = userService.findUser(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        var minDate = datapointRepository.findMinDateForUser(user);
        return computeStatistics("user", user.getId(), minDate,
                dateRange -> datapointRepository.findAverageForUser(user.getId(), Date.from(dateRange.from), Date.from(dateRange.to)));
    }

    private List<StatisticsDto> computeStatistics(String entityName, Long entityId, Date minDate, Function<DateRange, Statistics> statisticsFunction) {
        //min date null = no data for that entity at all
        if (minDate == null) {
            return Collections.emptyList();
        }
        Instant to, from = Instant.now();
        var statistics = new ArrayList<StatisticsDto>();

        //loop for such long, until we are past the last date for given entity
        while (true) {
            to = from;
            from = to.minus(defaultAverageFrame, ChronoUnit.MINUTES);
            Statistics stats = statisticsFunction.apply(new DateRange(from, to));
            statistics.add(StatisticsDto.builder()
                    .withName(entityName)
                    .withId(entityId)
                    .withFrom(from)
                    .withTo(to)
                    .withAverage(stats.getAverage())
                    .withCount(stats.getCount())
                    .build());
            if (minDate.toInstant().isAfter(from)) {
                break;
            }
        }
        logger.info("Returned {} Datapoint averages for {} id {}, for total time frame {} - {}", entityName, statistics.size(), entityId, from, to);
        return statistics;
    }

    private class DateRange {
        Instant from;
        Instant to;

        public DateRange(Instant from, Instant to) {
            this.from = from;
            this.to = to;
        }
    }
}
