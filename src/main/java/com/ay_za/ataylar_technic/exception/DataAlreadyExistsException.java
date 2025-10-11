package com.ay_za.ataylar_technic.exception;

public class DataAlreadyExistsException extends BaseException {
    public DataAlreadyExistsException(String message) {
        super(message, "DATA_002");
    }
}
