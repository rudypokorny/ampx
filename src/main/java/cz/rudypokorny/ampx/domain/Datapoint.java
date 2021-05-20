package cz.rudypokorny.ampx.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"device_id", "user_id", "timestamp"})
})
public class Datapoint {
    @Id
    @GeneratedValue(generator = "seq_point", strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id")
    @NotNull
    private Device device;

    @NotNull
    @ManyToOne
    private User user;

    @NotNull
    private Date timestamp;

    @NotNull
    private Double value;

    public Datapoint() {
    }

    public Long getId() {
        return id;
    }

    public Device getDevice() {
        return device;
    }

    public User getUser() {
        return user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Datapoint datapoint = (Datapoint) o;
        return Objects.equals(id, datapoint.id) && Objects.equals(device, datapoint.device) && Objects.equals(user, datapoint.user) && Objects.equals(timestamp, datapoint.timestamp) && Objects.equals(value, datapoint.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, device, user, timestamp, value);
    }

    @Override
    public String toString() {
        return "Datapoint{" +
                "id=" + id +
                ", device=" + device +
                ", user=" + user +
                ", timestamp=" + timestamp +
                ", value='" + value + '\'' +
                '}';
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Device device;
        private User user;
        private Date timestamp;
        private Double value;

        public Builder withDevice(final Device device) {
            this.device = device;
            return this;
        }

        public Builder withUser(final User user) {
            this.user = user;
            return this;
        }

        public Builder withTimestamo(final Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withValue(final Double value) {
            this.value = value;
            return this;
        }

        public Datapoint build() {
            var datapoint = new Datapoint();
            datapoint.setDevice(device);
            datapoint.setUser(user);
            datapoint.setTimestamp(timestamp);
            datapoint.setValue(value);
            return datapoint;
        }
    }

}
