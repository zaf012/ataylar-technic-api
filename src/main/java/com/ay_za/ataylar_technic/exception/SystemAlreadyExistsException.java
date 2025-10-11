package com.ay_za.ataylar_technic.exception;

public class SystemAlreadyExistsException extends BaseException {
    public SystemAlreadyExistsException(String message) {
        super(message, "SYSTEM_002");
    }
}
