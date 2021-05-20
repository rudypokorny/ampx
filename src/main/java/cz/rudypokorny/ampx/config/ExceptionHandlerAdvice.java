package cz.rudypokorny.ampx.config;

import cz.rudypokorny.ampx.dto.ErrorDto;
import cz.rudypokorny.ampx.exceptions.AbstractInvalidInputException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        var errorBuilder = ErrorDto.builder().withMessage("Validation error");
        for (final ObjectError error : ex.getBindingResult().getAllErrors()) {
            errorBuilder.withValidationError(((FieldError) error).getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorBuilder.build());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AbstractInvalidInputException.class)
    public ResponseEntity<Object> handleBadRequest(HttpServletRequest req, AbstractInvalidInputException e) {
        return ResponseEntity
                .status(e.getStatus())
                .body(ErrorDto.builder()
                .withMessage(e.getMessage())
                .build());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        return invalidRequestError(ResponseEntity.badRequest(), "Request body is not valid");
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return invalidRequestError(ResponseEntity.badRequest(), "Request parameters are not valid");
    }

    private ResponseEntity<Object> invalidRequestError(ResponseEntity.BodyBuilder bodyBuilder, String s) {
        return bodyBuilder
                .body(ErrorDto.builder()
                        .withMessage(s)
                        .build());
    }
}
