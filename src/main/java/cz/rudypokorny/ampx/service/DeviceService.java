package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.domain.Device;
import cz.rudypokorny.ampx.dto.DeviceDto;

import java.util.Optional;

/**
 * Service managing {@link cz.rudypokorny.ampx.domain.Device} entity
 */
public interface DeviceService {


    /**
     * Searches for device with given id
     *
     * @param deviceId
     * @return found device wrapped in Optional
     */
    Optional<Device> findDevice(final Long deviceId);

    /**
     * Searches for device in database, returns will number of datapoints
     *
     * @param deviceId id of the device
     * @return found device dto
     * @throws cz.rudypokorny.ampx.exceptions.DeviceNotFoundException if device with given id does not exists
     */
    DeviceDto findDeviceAsDto(final Long deviceId);

    /**
     * Deletes all the {@link cz.rudypokorny.ampx.domain.Datapoint} for given deviceId
     *
     * @param deviceId id of the device whose datapoints hould be deleted
     * @return count of deleted datapoints
     * @throws cz.rudypokorny.ampx.exceptions.DeviceNotFoundException if device with given id does not exists
     */
    Integer deleteDatapointsForDevice(final Long deviceId);
}
