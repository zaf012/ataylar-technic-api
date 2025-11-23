package com.ay_za.ataylar_technic.exception;

/**
 * Kaynak bulunamadığında fırlatılan exception
 */
public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}

