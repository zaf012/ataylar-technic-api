package com.ay_za.ataylar_technic.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class QrCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int QR_CODE_LENGTH = 12;
    private static final SecureRandom random = new SecureRandom();

    /**
     * 12 haneli unique QR kod oluşturur (İngilizce harf ve rakam)
     * @return 12 karakterli QR kod
     */
    public String generateQrCode() {
        StringBuilder qrCode = new StringBuilder(QR_CODE_LENGTH);

        for (int i = 0; i < QR_CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            qrCode.append(CHARACTERS.charAt(randomIndex));
        }

        return qrCode.toString();
    }

    /**
     * Belirtilen prefix ile QR kod oluşturur
     * @param prefix QR kodun başına eklenecek prefix (max 4 karakter)
     * @return Prefix + random 8 karakter
     */
    public String generateQrCodeWithPrefix(String prefix) {
        if (prefix == null || prefix.length() > 4) {
            throw new IllegalArgumentException("Prefix 4 karakterden fazla olamaz");
        }

        int remainingLength = QR_CODE_LENGTH - prefix.length();
        StringBuilder qrCode = new StringBuilder();
        qrCode.append(prefix.toUpperCase());

        for (int i = 0; i < remainingLength; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            qrCode.append(CHARACTERS.charAt(randomIndex));
        }

        return qrCode.toString();
    }

    /**
     * QR kod format kontrolü
     * @param qrCode Kontrol edilecek QR kod
     * @return Geçerli ise true
     */
    public boolean isValidQrCodeFormat(String qrCode) {
        if (qrCode == null || qrCode.length() != QR_CODE_LENGTH) {
            return false;
        }

        return qrCode.matches("^[A-Z0-9]{12}$");
    }
}
