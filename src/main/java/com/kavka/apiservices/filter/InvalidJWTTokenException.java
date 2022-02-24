package com.kavka.apiservices.filter;

public class InvalidJWTTokenException extends RuntimeException{

    public InvalidJWTTokenException(String message) {
        super(message);
    }
}
