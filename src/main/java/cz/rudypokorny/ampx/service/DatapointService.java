package cz.rudypokorny.ampx.service;

import cz.rudypokorny.ampx.dto.DatapointDto;
import cz.rudypokorny.ampx.exceptions.DatapointNotUniqueException;

public interface DatapointService {
    String DATAPOINT_ALREADY_EXISTS_MESSAGE = "Datapoint for user %s, device %s and timestamp %s already exists";

    /**
     * Creates new datapoint for given device at given time for given user. Both device and user has to exists.
     * Datapoint for given user, device and timestamp must be unique
     *
     * @param datapoint to be saved
     * @return saved datapoint
     * @throws DatapointNotUniqueException in case when datapoint for given time, user and device is alredy persisted
     * @throws NullPointerException        if the given object is null
     */
    DatapointDto createDatapointFromDto(final DatapointDto datapoint);
}
