package com.ay_za.ataylar_technic.exception;

public class AccountAlreadyExistsException extends BaseException {
    public AccountAlreadyExistsException(String message) {
        super(message, "ACCOUNT_002");
    }
}
