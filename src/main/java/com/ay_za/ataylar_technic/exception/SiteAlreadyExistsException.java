package com.ay_za.ataylar_technic.exception;

public class SiteAlreadyExistsException extends BaseException {
    public SiteAlreadyExistsException(String message) {
        super(message, "SITE_002");
    }
}
