package cz.rudypokorny.ampx.exceptions;


import org.springframework.http.HttpStatus;

public class DatapointNotUniqueException extends AbstractInvalidInputException {

    public DatapointNotUniqueException(String s) {
        super(s, HttpStatus.BAD_REQUEST);
    }
}
