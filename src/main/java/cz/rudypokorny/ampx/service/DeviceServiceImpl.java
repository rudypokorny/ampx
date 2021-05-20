package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.Device;
import cz.rudypokorny.ampx.dto.DeviceDto;
import cz.rudypokorny.ampx.dto.mapping.DeviceMapper;
import cz.rudypokorny.ampx.exceptions.DeviceNotFoundException;
import cz.rudypokorny.ampx.repository.DatapointRepository;
import cz.rudypokorny.ampx.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeviceServiceImpl implements DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceServiceImpl.class);

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private DatapointRepository datapointRepository;

    @Autowired
    private DeviceMapper deviceMapper;

    @Override
    public Optional<Device> findDevice(Long deviceId) {
        Objects.requireNonNull(deviceId, "deviceId cannot be null");
        return deviceRepository.findById(deviceId);
    }

    @Override
    public DeviceDto findDeviceAsDto(final Long deviceId) {
        return findDevice(deviceId)
                .map(deviceMapper::toDto)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
    }

    @Override
    @Transactional
    public Integer deleteDatapointsForDevice(final Long deviceId) {
        Objects.requireNonNull(deviceId, "deviceId cannot be null");
        return deviceRepository.findById(deviceId)
                .map(this::deleteDatapoints)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
    }

    private Integer deleteDatapoints(final Device device){
        var count = device.getDatapoints().size();
        datapointRepository.deleteByDevice(device);
        logger.debug("Deleted all datapoints ({}) for device {}", count, device.getId());
        return count;
    }

}
