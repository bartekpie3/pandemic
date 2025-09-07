package com.example.pandemic.domain.exception;

public class InvalidActionRequest extends DomainException {
    public InvalidActionRequest(String message) {
        super(message);
    }
}
