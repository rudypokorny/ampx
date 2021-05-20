package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.Datapoint;
import cz.rudypokorny.ampx.domain.Device;
import cz.rudypokorny.ampx.domain.User;
import cz.rudypokorny.ampx.dto.DatapointDto;
import cz.rudypokorny.ampx.dto.mapping.DatapointMapper;
import cz.rudypokorny.ampx.exceptions.DatapointNotUniqueException;
import cz.rudypokorny.ampx.exceptions.DeviceNotFoundException;
import cz.rudypokorny.ampx.exceptions.UserNotFoundException;
import cz.rudypokorny.ampx.repository.DatapointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

import static java.lang.String.format;

@Service
public class DatapointServiceImpl implements DatapointService {

    @Autowired
    private DatapointRepository datapointRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DatapointMapper datapointMapper;


    @Override
    public DatapointDto createDatapointFromDto(final DatapointDto datapointDto) {
        Objects.requireNonNull(datapointDto, "datapointDto cannot be null");

        var convertedDatapoint = datapointMapper.fromDto(datapointDto);
        var savedDatapoint = createDatapoint(convertedDatapoint.getUser(), convertedDatapoint.getDevice(),
                convertedDatapoint.getTimestamp(), convertedDatapoint.getValue());

        return datapointMapper.toDto(savedDatapoint);
    }

    public Datapoint createDatapoint(final User user, final Device device, final Date timestamp, final Double value) {
        Objects.requireNonNull(user, "user cannot be null");
        Objects.requireNonNull(device, "device cannot be null");
        Objects.requireNonNull(timestamp, "timestamp cannot be null");

        var foundUser = userService.findUser(user.getId())
                .orElseThrow(() -> new UserNotFoundException(user.getId()));

        var foundDevice = deviceService.findDevice(device.getId())
                .orElseThrow(() -> new DeviceNotFoundException(device.getId()));

        if (!datapointRepository.findDatapointByUserAndDeviceAndTimestamp(user, device, timestamp).isEmpty()) {
            throw new DatapointNotUniqueException(format(DATAPOINT_ALREADY_EXISTS_MESSAGE,
                    user.getId(), device.getId(), timestamp));
        }
        var dataToSave = Datapoint.builder()
                .withDevice(foundDevice)
                .withUser(foundUser)
                .withValue(value)
                .withTimestamo(timestamp)
                .build();
        return datapointRepository.save(dataToSave);
    }

}
