package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.dto.StatisticsDto;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Simple implementation for SMA - simple moving average for double values. Not managed by Spring
 */
public class MovingAverageProcessor {

    private final Queue<Double> averages = new LinkedList<>();
    private final List<StatisticsDto> statistics = new LinkedList<>();
    private final int windowSize;
    private Double sum = 0d;

    /**
     * Instantiate the processor with fixed windows size, for which the SMA is calculated
     *
     * @param windowSize
     */
    public MovingAverageProcessor(int windowSize) {
        this.windowSize = windowSize;
    }

    /**
     * Appending data to the processor. Data that are no longer fitting into window are dropped
     *
     * @param value
     * @return
     */
    public MovingAverageProcessor addData(Double value) {
        sum += value;
        averages.add(value);
        if (averages.size() > windowSize) {
            sum -= averages.remove();
        }
        return this;
    }

    public Double getResult() {
        return sum / windowSize;
    }
}
