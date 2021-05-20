package cz.rudypokorny.ampx.dto;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ErrorDto {

    private final String message;
    private final Map<String, String> errors;
    private final String details;

    private ErrorDto(String message, String details, Map<String, String> errors) {
        this.message = message;
        this.errors = errors;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public String getDetails() {
        return details;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String message;
        private String details;
        private Map<String, String> errors = new HashMap<>();

        public Builder withMessage(final String message) {
            this.message = message;
            return this;
        }
        public Builder withDetails(final String details) {
            this.details = details;
            return this;
        }

        public Builder withValidationError(String field, String error) {
            this.errors.put(field, error);
            return this;
        }

        public ErrorDto build() {
            return new ErrorDto(message, details, errors);
        }
    }
}
