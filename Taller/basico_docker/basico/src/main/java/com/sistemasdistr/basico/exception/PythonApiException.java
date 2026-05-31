package com.sistemasdistr.basico.exception;

public class PythonApiException extends RuntimeException {
    private final String errorCode;

    public PythonApiException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}