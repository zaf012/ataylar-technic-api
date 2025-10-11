package com.ay_za.ataylar_technic.exception;

public class AccountNotFoundException extends BaseException {
    public AccountNotFoundException(String message) {
        super(message, "ACCOUNT_001");
    }
}
