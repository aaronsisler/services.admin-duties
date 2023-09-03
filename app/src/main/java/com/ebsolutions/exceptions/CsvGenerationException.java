package com.ebsolutions.exceptions;

public class CsvGenerationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CsvGenerationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public CsvGenerationException(String errorMessage) {
        super(errorMessage);
    }
}
