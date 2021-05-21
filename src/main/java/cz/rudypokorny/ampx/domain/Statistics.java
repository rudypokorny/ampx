package cz.rudypokorny.ampx.domain;

import java.util.Objects;

/**
 * POJO for wrapping JPQL query result used in {@link cz.rudypokorny.ampx.repository.DatapointRepository}
 */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistics that = (Statistics) o;
        return Objects.equals(average, that.average) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(average, count);
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "average=" + average +
                ", count=" + count +
                '}';
    }
}
