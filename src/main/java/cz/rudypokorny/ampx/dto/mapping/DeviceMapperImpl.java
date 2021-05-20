package cz.rudypokorny.ampx.dto.mapping;

import cz.rudypokorny.ampx.domain.Device;
import cz.rudypokorny.ampx.dto.DeviceDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DeviceMapperImpl implements DeviceMapper {

    @Override
    public Device fromDto(final DeviceDto deviceDto) {
        Objects.requireNonNull(deviceDto, "deviceDto cannot be null");

        var device = new Device();
        device.setId(deviceDto.getId());
        return device;
    }

    @Override
    public DeviceDto toDto(final Device device) {
        Objects.requireNonNull(device, "device cannot be null");

        var dto = new DeviceDto();
        dto.setId(device.getId());
        dto.setDatapointCount(device.getDatapoints().size());
        return dto;
    }
}
