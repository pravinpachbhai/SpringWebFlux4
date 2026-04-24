package com.pravin.demo.exception;

public class CustomerFoundException extends RuntimeException {

    public CustomerFoundException(String message) {
        super(message);
    }
}