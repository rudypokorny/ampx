package cz.rudypokorny.ampx.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Device {

    @Id
    @GeneratedValue(generator = "seq_device", strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    private List<Datapoint> datapoints;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Datapoint> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(List<Datapoint> datapoints) {
        this.datapoints = datapoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(id, device.id) && Objects.equals(datapoints, device.datapoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datapoints);
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", datapoints=" + datapoints +
                '}';
    }
}
