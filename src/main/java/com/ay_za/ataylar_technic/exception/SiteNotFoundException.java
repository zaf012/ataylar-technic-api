package com.ay_za.ataylar_technic.exception;

public class SiteNotFoundException extends BaseException {
    public SiteNotFoundException(String message) {
        super(message, "SITE_001");
    }
}
