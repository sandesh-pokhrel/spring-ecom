package com.kavka.apiservices.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidUserCredentialException extends RuntimeException{

    public InvalidUserCredentialException(String message) {
        super(message);
    }
}
