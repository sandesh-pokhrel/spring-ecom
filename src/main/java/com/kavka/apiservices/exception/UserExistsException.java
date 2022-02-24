package com.kavka.apiservices.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserExistsException extends RuntimeException {
    public UserExistsException(String message) {
        super(message);
    }
}
