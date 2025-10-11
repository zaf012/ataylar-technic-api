package com.ay_za.ataylar_technic.exception;

public class DataNotFoundException extends BaseException {
    public DataNotFoundException(String message) {
        super(message, "DATA_001");
    }
}
