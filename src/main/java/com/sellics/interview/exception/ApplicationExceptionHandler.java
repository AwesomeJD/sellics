package com.sellics.interview.exception;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sellics.interview.constants.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    @ExceptionHandler({ApplicationException.class})
    public final ResponseEntity<Object> handleServiceException(ApplicationException ex) {
        LOGGER.error("Application exception occurred.", (Throwable) ex.getOriginalException().orElse(ex));
        Map<String, Object> responseError =
                this.getResponseBodyMap(ex.getErrorCode(), ex.getErrorMessage(), "Business Error");
        LOGGER.trace("Error response :{}", responseError);
        return new ResponseEntity(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public final ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        LOGGER.error("ConstraintViolationException exception occurred due to bad user input.", ex);
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        Map<String, Object> responseError =
                this.getResponseBodyMap(
                        ErrorConstants.BAD_USER_INPUT_ERROR_CODE, errors.toString(), "BAD USER INPUT");
        LOGGER.trace("Error response :{}", responseError);
        return new ResponseEntity(responseError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class})
    public final ResponseEntity<Object> handleException(Exception ex) {
        LOGGER.error("General exception occurred. {}", ex);
        Map<String, Object> responseError =
                this.getResponseBodyMap(
                        ErrorConstants.GENERIC_ERROR_CODE,
                        ErrorConstants.GENERIC_ERROR_MESSAGE,
                        "Technical Error");
        LOGGER.trace("Error response :{}", responseError);
        return new ResponseEntity(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, Object> getResponseBodyMap(
            String errorCode, String errorMessage, String errorType) {
        return ImmutableMap.of(
                "errors",
                ImmutableList.of(
                        ImmutableMap.of(
                                "code", errorCode, "message", errorMessage, "type", errorType)));
    }
}

