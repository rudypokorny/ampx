package cz.rudypokorny.ampx.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue(generator = "seq_user", strategy = GenerationType.SEQUENCE)
    private  Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "device")
    private List<Datapoint> datapoints;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(datapoints, user.datapoints);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datapoints);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", datapoints=" + datapoints +
                '}';
    }
}
