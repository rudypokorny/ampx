package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.dto.StatisticsDto;

import java.util.List;

public interface StatisticsService {

    /**
     * Returns list of 15 minutes averages of time serie from first datapoint to current time. Matching device key"
     *
     * @param deviceId the device whose devices should be aggregated
     * @return resulted statistics
     */
    List<StatisticsDto> getAveragesForDevice(final Long deviceId);

    /**
     * Return list of 15 minutes averages of time serie from first datapoint to current time. Matching user key
     *
     * @param userId the user whose devices should be aggregated
     * @return resulted statistics
     */
    List<StatisticsDto> getAveragesForUser(final Long userId);

    /**
     * return list of moving averages of 15 minutes average buckets. I.E. moving averages of result {@link StatisticsService#getAveragesForUser(Long)}
     * for given user
     *
     * @param userId
     * @param windowSize
     * @return resulted statistics
     */
    List<StatisticsDto> getMovingAveragesForUser(final Long userId, final Integer windowSize);

    /**
     * return list of moving averages of 15 minutes average buckets. I.E. moving averages of result {@link StatisticsService#getAveragesForUser(Long)}
     * for give device
     *
     * @param deviceId
     * @param windowSize
     * @return resulted statistics
     */
    List<StatisticsDto> getMovingAveragesForDevice(final Long deviceId, final Integer windowSize);
}
