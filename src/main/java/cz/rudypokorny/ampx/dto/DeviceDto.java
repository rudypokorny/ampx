package cz.rudypokorny.ampx.dto;

import java.util.Objects;

public class DeviceDto {

    private Long id;

    private int datapointCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDatapointCount() {
        return datapointCount;
    }

    public void setDatapointCount(int datapointCount) {
        this.datapointCount = datapointCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDto deviceDto = (DeviceDto) o;
        return datapointCount == deviceDto.datapointCount && Objects.equals(id, deviceDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datapointCount);
    }

    @Override
    public String toString() {
        return "DeviceDto{" +
                "id=" + id +
                ", datapointCount=" + datapointCount +
                '}';
    }
}
