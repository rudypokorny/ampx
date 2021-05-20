package cz.rudypokorny.ampx.exceptions;

import org.springframework.http.HttpStatus;

public class DeviceNotFoundException extends AbstractInvalidInputException {

    public DeviceNotFoundException(Long id) {
        super(String.format(NOT_FOUND_MESSAGE_TEMPLATE, "Device", id), HttpStatus.NOT_FOUND);
    }

}
