package com.ay_za.ataylar_technic.exception;

public class SystemNotFoundException extends BaseException {
    public SystemNotFoundException(String message) {
        super(message, "SYSTEM_001");
    }
}
