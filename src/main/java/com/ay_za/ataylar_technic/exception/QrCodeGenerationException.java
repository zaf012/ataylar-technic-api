package com.ay_za.ataylar_technic.exception;

public class QrCodeGenerationException extends InventoryCategoryException {

    public QrCodeGenerationException() {
        super("Unique QR kod oluşturulamadı. Lütfen tekrar deneyin", "QR_CODE_GENERATION_FAILED");
    }

    public QrCodeGenerationException(int attempts) {
        super("Unique QR kod oluşturulamadı. " + attempts + " deneme sonucu başarısız", "QR_CODE_GENERATION_FAILED");
    }
}
