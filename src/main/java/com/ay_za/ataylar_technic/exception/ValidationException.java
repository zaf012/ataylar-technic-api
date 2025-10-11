package com.ay_za.ataylar_technic.exception;

public class ValidationException extends BaseException {
    public ValidationException(String message) {
        super(message, "VALIDATION_001");
    }
}
