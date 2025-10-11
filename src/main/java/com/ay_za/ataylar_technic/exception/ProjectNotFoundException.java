package com.ay_za.ataylar_technic.exception;

public class ProjectNotFoundException extends BaseException {
    public ProjectNotFoundException(String message) {
        super(message, "PROJECT_001");
    }
}
