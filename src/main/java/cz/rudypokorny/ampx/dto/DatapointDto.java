package cz.rudypokorny.ampx.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

public class DatapointDto {

    private Long id;

    @NotNull
    private Long device;

    @NotNull
    private Long user;

    @NotNull
    private Date timestamp;

    @NotNull
    private Double value;

    public Long getDevice() {
        return device;
    }

    public void setDevice(Long device) {
        this.device = device;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatapointDto that = (DatapointDto) o;
        return Objects.equals(id, that.id) && Objects.equals(device, that.device) && Objects.equals(user, that.user) && Objects.equals(timestamp, that.timestamp) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, device, user, timestamp, value);
    }

    @Override
    public String toString() {
        return "DatapointDTO{" +
                "id='" + id + '\'' +
                ", device='" + device + '\'' +
                ", user='" + user + '\'' +
                ", timestamp=" + timestamp +
                ", value='" + value + '\'' +
                '}';
    }
}
