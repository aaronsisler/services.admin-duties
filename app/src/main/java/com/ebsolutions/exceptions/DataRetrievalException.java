package com.ebsolutions.exceptions;

public class DataRetrievalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataRetrievalException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
