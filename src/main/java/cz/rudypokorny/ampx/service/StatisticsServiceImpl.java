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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<StatisticsDto> getMovingAveragesForDevice(final Long deviceId, final Integer windowSize) {
        Objects.requireNonNull(deviceId, "deviceId cannot be null");
        Objects.requireNonNull(windowSize, "windowSize cannot be null");

        var averages = getAveragesForDevice(deviceId);
        logger.info("Computing Datapoint moving averages for device id {}, window size = {}", deviceId, windowSize);

        return computeSimpleMovingAverages(windowSize, averages);
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

    @Override
    public List<StatisticsDto> getMovingAveragesForUser(final Long userId, final Integer windowSize) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(windowSize, "windowSize cannot be null");

        var averages = getAveragesForUser(userId);
        logger.info("Computing Datapoint moving averages for user id {}, window size = {}", userId, windowSize);

        return computeSimpleMovingAverages(windowSize, averages);
    }

    private List<StatisticsDto> computeSimpleMovingAverages(Integer windowSize, List<StatisticsDto> averages) {
        final AtomicInteger counter = new AtomicInteger();
        //group standaard statistics result to the buckets by the window size
        final Collection<List<StatisticsDto>> result = averages.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / windowSize))
                .values();

        //convert the list of statistics per window to once item, with oldest FROM, youngest TO and average
        return result.stream().map(list -> {
            return StatisticsDto.builder()
                    //compute tha average from the list
                    .withAverage(list.stream().map(StatisticsDto::getAverage).reduce(0d, (sum, curr) -> sum += curr) / list.size())
                    //its all the same - takes from the first item
                    .withName(list.get(0).getName())
                    //reduce to the oldest
                    .withFrom(list.stream().map(s -> s.getFrom().toInstant()).reduce(Instant.now(), (a, b) -> a.isBefore(b) ? a : b))
                    //reduce to the youngest
                    .withTo(list.stream().map(s -> s.getTo().toInstant()).reduce(Instant.now(), (a, b) -> a.isAfter(b) ? a : b))
                    .build();
        }).collect(Collectors.toList());
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
        logger.info("Returned {} Datapoint averages for {} id {}", entityName, statistics.size(), entityId);
        return statistics;
    }

    /**
     * Helper POJO just for allowing use of functional interface with for more than one input parameter
     */
    private class DateRange {
        Instant from;
        Instant to;

        public DateRange(Instant from, Instant to) {
            this.from = from;
            this.to = to;
        }
    }

    private class AverageAgregator {
        Instant from;
        Instant to;
        Double averageSum;
        Integer count;
    }
}
