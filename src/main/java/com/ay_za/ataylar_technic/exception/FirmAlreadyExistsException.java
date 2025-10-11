package com.ay_za.ataylar_technic.exception;

public class FirmAlreadyExistsException extends BaseException {
    public FirmAlreadyExistsException(String message) {
        super(message, "FIRM_002");
    }
}
