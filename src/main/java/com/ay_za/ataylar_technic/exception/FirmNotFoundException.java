package com.ay_za.ataylar_technic.exception;

public class FirmNotFoundException extends BaseException {
    public FirmNotFoundException(String message) {
        super(message, "FIRM_001");
    }
}
