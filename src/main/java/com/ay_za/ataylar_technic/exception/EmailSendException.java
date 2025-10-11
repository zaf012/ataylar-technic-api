package com.ay_za.ataylar_technic.exception;

public class EmailSendException extends BaseException {
    public EmailSendException(String message) {
        super(message, "EMAIL_001");
    }

    public EmailSendException(String message, Throwable cause) {
        super(message, "EMAIL_001", cause);
    }
}
