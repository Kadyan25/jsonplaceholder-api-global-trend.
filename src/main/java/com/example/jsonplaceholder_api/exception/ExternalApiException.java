package com.example.jsonplaceholder_api.exception;

public class ExternalApiException extends RuntimeException {

    public ExternalApiException(String message) {
        super(message);
    }
}
