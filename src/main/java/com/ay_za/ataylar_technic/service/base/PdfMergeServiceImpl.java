package com.ay_za.ataylar_technic.service.base;

import java.io.IOException;
import java.util.List;

public interface PdfMergeServiceImpl {

    /**
     * Seçilen PDF'leri template sayfalarıyla birleştirir
     * Sıralama: ataylar-first-page.pdf → Seçilen PDF'ler → ataylar-last-pages.pdf
     *
     * @param pdfRecordIds Birleştirilecek PDF kayıtlarının ID listesi
     * @return Birleştirilmiş PDF (byte array)
     * @throws IOException PDF işleme hatası
     */
    byte[] mergePdfsWithTemplate(List<String> pdfRecordIds) throws IOException;
}

