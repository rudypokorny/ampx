package cz.rudypokorny.ampx.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AbstractInvalidInputException {

    public UserNotFoundException(Long id) {
        super(String.format(NOT_FOUND_MESSAGE_TEMPLATE, "User", id), HttpStatus.NOT_FOUND);
    }

}
