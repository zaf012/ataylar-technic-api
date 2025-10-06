package com.ay_za.ataylar_technic.exception;

public class CategoryValidationException extends InventoryCategoryException {

    public CategoryValidationException(String message) {
        super(message, "CATEGORY_VALIDATION_ERROR");
    }

    public static CategoryValidationException categoryCodeAlreadyExists(String categoryCode) {
        return new CategoryValidationException("Kategori kodu zaten kullanımda: " + categoryCode);
    }

    public static CategoryValidationException qrCodeAlreadyExists(String qrCode) {
        return new CategoryValidationException("QR kod zaten kullanımda: " + qrCode);
    }

    public static CategoryValidationException invalidCategoryName(String categoryName) {
        return new CategoryValidationException("Geçersiz kategori adı: " + categoryName);
    }

    public static CategoryValidationException invalidQrCodeFormat(String qrCode) {
        return new CategoryValidationException("Geçersiz QR kod formatı: " + qrCode + ". QR kod 12 karakter olmalı ve sadece büyük harf ile rakam içermelidir");
    }

    public static CategoryValidationException circularReferenceDetected(String categoryId, String parentId) {
        return new CategoryValidationException("Döngüsel referans tespit edildi. Kategori kendi alt kategorisine parent olamaz");
    }
}
