package com.sellics.interview.exception;

import java.util.Optional;

/**
 * The Exception for the whole application.
 * This helps in the uniformity of the exception and error handling.
 */
public class ApplicationException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private final String errorCode;
    private final String errorMessage;
    private final Throwable originalException;

    public ApplicationException(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.originalException = null;
    }

    public ApplicationException(String errorCode, String errorMessage, Throwable originalException) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.originalException = originalException;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Optional<Throwable> getOriginalException() {
        return Optional.ofNullable(this.originalException);
    }
}
