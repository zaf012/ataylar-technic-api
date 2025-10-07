package com.ay_za.ataylar_technic.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class QrCodeGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int QR_CODE_LENGTH = 12;
    private final SecureRandom random = new SecureRandom();

    /**
     * 12 haneli benzersiz QR kod üretir
     * Format: İngilizce büyük harfler ve rakamlar
     * Örnek: TK0000119, L4AKKEUZNV
     */
    public String generateQRCode() {
        StringBuilder qrCode = new StringBuilder(QR_CODE_LENGTH);

        for (int i = 0; i < QR_CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            qrCode.append(CHARACTERS.charAt(randomIndex));
        }

        return qrCode.toString();
    }

    /**
     * Özel prefix ile QR kod üretir
     * @param prefix QR kodun başına eklenecek prefix (max 4 karakter)
     */
    public String generateQRCodeWithPrefix(String prefix) {
        if (prefix == null || prefix.length() > 4) {
            throw new IllegalArgumentException("Prefix maksimum 4 karakter olabilir");
        }

        StringBuilder qrCode = new StringBuilder();
        qrCode.append(prefix.toUpperCase());

        int remainingLength = QR_CODE_LENGTH - prefix.length();
        for (int i = 0; i < remainingLength; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            qrCode.append(CHARACTERS.charAt(randomIndex));
        }

        return qrCode.toString();
    }

    /**
     * Sayısal QR kod üretir
     * @param length QR kod uzunluğu
     */
    public String generateNumericQRCode(int length) {
        StringBuilder qrCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            qrCode.append(random.nextInt(10));
        }

        return qrCode.toString();
    }

    /**
     * QR kod formatını doğrular
     * @param qrCode kontrol edilecek QR kod
     * @return geçerli ise true
     */
    public boolean isValidQRCode(String qrCode) {
        if (qrCode == null || qrCode.length() != QR_CODE_LENGTH) {
            return false;
        }

        return qrCode.matches("[A-Z0-9]{" + QR_CODE_LENGTH + "}");
    }
}
