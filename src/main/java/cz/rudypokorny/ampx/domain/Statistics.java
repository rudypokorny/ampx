package cz.rudypokorny.ampx.domain;

public class Statistics {
    private final Double average;
    private final Long count;

    public Statistics(Double average, Long count) {
        this.average = average;
        this.count = count;
    }

    public Double getAverage() {
        return average;
    }

    public Long getCount() {
        return count;
    }
}
