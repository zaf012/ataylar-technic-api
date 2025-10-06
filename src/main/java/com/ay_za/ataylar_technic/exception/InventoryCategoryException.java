package com.ay_za.ataylar_technic.exception;

public class InventoryCategoryException extends RuntimeException {

    private final String errorCode;

    public InventoryCategoryException(String message) {
        super(message);
        this.errorCode = "INVENTORY_CATEGORY_ERROR";
    }

    public InventoryCategoryException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InventoryCategoryException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "INVENTORY_CATEGORY_ERROR";
    }

    public InventoryCategoryException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
