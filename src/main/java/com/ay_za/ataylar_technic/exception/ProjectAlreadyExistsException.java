package com.ay_za.ataylar_technic.exception;

public class ProjectAlreadyExistsException extends BaseException {
    public ProjectAlreadyExistsException(String message) {
        super(message, "PROJECT_002");
    }
}
