package cz.rudypokorny.ampx.dto;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class StatisticsDto {

    private final String name;
    private final Object id;
    private final Date from;
    private final Date to;
    private final Long count;
    private final Double average;

    public static Builder builder() {
        return new Builder();
    }

    private StatisticsDto(String entity, Object id, Date from, Date to, Long count, Double average) {
        this.name = entity;
        this.id = id;
        this.from = from;
        this.to = to;
        this.count = count;
        this.average = average;
    }

    public String getName() {
        return name;
    }

    public Object getId() {
        return id;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public Long getCount() {
        return count;
    }

    public Double getAverage() {
        return average;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticsDto that = (StatisticsDto) o;
        return Objects.equals(name, that.name) && Objects.equals(id, that.id) && Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(count, that.count) && Objects.equals(average, that.average);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, from, to, count, average);
    }

    @Override
    public String toString() {
        return "StatisticsDto{" +
                "entity='" + name + '\'' +
                ", id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", count=" + count +
                ", average=" + average +
                '}';
    }

    public static class Builder {
        private String name;
        private Object id;
        private Date from;
        private Date  to;
        private Long count;
        private Double average;

        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withId(Object id) {
            this.id = id;
            return this;
        }

        public Builder withFrom(Instant from) {
            this.from = Date.from(from);
            return this;
        }

        public Builder withTo(Instant to) {
            this.to = Date.from(to);
            return this;
        }

        public Builder withCount(Long count) {
            this.count = count;
            return this;
        }

        public Builder withAverage(Double average) {
            this.average = average;
            return this;
        }

        public StatisticsDto build(){
            return new StatisticsDto(name, id, from, to, count, average);
        }
    }
}
