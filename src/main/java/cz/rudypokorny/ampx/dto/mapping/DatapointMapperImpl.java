package cz.rudypokorny.ampx.dto.mapping;

import cz.rudypokorny.ampx.domain.Datapoint;
import cz.rudypokorny.ampx.domain.Device;
import cz.rudypokorny.ampx.domain.User;
import cz.rudypokorny.ampx.dto.DatapointDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class DatapointMapperImpl implements DatapointMapper {

    @Override
    public Datapoint fromDto(final DatapointDto datapointDto) {
        Objects.requireNonNull(datapointDto, "datapointDto cannot be null");

        var datapoint = new Datapoint();
        datapoint.setValue(datapointDto.getValue());
        datapoint.setTimestamp(datapointDto.getTimestamp());
        datapoint.setUser(convertUser(datapointDto));
        datapoint.setDevice(convertDevice(datapointDto));
        datapoint.setId(datapointDto.getId());
        return datapoint;
    }

    @Override
    public DatapointDto toDto(final Datapoint datapoint) {
        Objects.requireNonNull(datapoint, "datapoint cannot be null");

        var dto = new DatapointDto();
        dto.setTimestamp(datapoint.getTimestamp());
        if(datapoint.getDevice() != null){
            dto.setDevice(datapoint.getDevice().getId());
        }
        if(datapoint.getUser() != null){
            dto.setUser(datapoint.getUser().getId());
        }
        dto.setValue(datapoint.getValue());
        dto.setId(datapoint.getId());
        return dto;
    }

    private Device convertDevice(final DatapointDto datapointDto) {
        var device = new Device();
        device.setId(datapointDto.getDevice());
        return device;
    }

    private User convertUser(final DatapointDto datapointDto) {
        var user = new User();
        user.setId(datapointDto.getUser());
        return user;
    }
}
