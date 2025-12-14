package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.entity.MaintenancePdfRecord;
import com.ay_za.ataylar_technic.repository.MaintenancePdfRecordRepository;
import com.ay_za.ataylar_technic.service.base.PdfMergeServiceImpl;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class PdfMergeService implements PdfMergeServiceImpl {

    private static final String FIRST_PAGE_TEMPLATE = "pdf-templates/ataylar-first-page.pdf";
    private static final String LAST_PAGES_TEMPLATE = "pdf-templates/ataylar-last-pages.pdf";

    private final MaintenancePdfRecordRepository pdfRecordRepository;

    public PdfMergeService(MaintenancePdfRecordRepository pdfRecordRepository) {
        this.pdfRecordRepository = pdfRecordRepository;
    }

    @Override
    public byte[] mergePdfsWithTemplate(List<String> pdfRecordIds) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();
        ByteArrayOutputStream mergedOutput = new ByteArrayOutputStream();

        // 1️⃣ İlk sayfa ekle (ataylar-first-page.pdf)
        addTemplateToMerger(merger, FIRST_PAGE_TEMPLATE);

        // 2️⃣ Seçilen PDF'leri sırayla ekle
        for (String pdfRecordId : pdfRecordIds) {
            MaintenancePdfRecord record = pdfRecordRepository.findById(pdfRecordId)
                    .orElseThrow(() -> new RuntimeException("PDF kaydı bulunamadı: ID=" + pdfRecordId));

            Path pdfPath = Paths.get(record.getFilePath());

            if (!Files.exists(pdfPath)) {
                throw new IOException("PDF dosyası bulunamadı: " + record.getFilePath());
            }

            merger.addSource(Files.newInputStream(pdfPath));
        }

        // 3️⃣ Son sayfaları ekle (ataylar-last-pages.pdf)
        addTemplateToMerger(merger, LAST_PAGES_TEMPLATE);

        // 4️⃣ Birleştirme işlemini yap
        merger.setDestinationStream(mergedOutput);
        merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

        return mergedOutput.toByteArray();
    }

    /**
     * Template PDF'i merger'a ekler
     *
     * @param merger       PDFMergerUtility instance
     * @param templatePath Template dosya yolu (resources klasöründe)
     * @throws IOException Dosya bulunamaz veya okunamazsa
     */
    private void addTemplateToMerger(PDFMergerUtility merger, String templatePath) throws IOException {
        ClassPathResource resource = new ClassPathResource(templatePath);

        if (!resource.exists()) {
            throw new IOException("Template PDF bulunamadı: " + templatePath);
        }

        InputStream templateStream = resource.getInputStream();
        merger.addSource(templateStream);
    }
}

