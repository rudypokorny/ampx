package cz.rudypokorny.ampx.exceptions;

import org.springframework.http.HttpStatus;

/**
 * marker interface for having common ancestor to better manage exception handling
 */
public abstract class AbstractInvalidInputException extends RuntimeException{

    public static final String NOT_FOUND_MESSAGE_TEMPLATE = "%s with id %s has not been found";
    private HttpStatus status;

    public AbstractInvalidInputException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
