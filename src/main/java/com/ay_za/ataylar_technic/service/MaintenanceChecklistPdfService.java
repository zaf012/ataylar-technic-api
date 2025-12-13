package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.service.base.MaintenanceChecklistPdfServiceImpl;
import com.ay_za.ataylar_technic.service.base.SystemInfoServiceImpl;
import com.ay_za.ataylar_technic.service.model.MaintenanceChecklistItemDto;
import com.ay_za.ataylar_technic.service.model.MaintenanceChecklistModel;
import com.ay_za.ataylar_technic.util.DateUtil;
import com.ay_za.ataylar_technic.util.SimpleReportExporter;
import com.ay_za.ataylar_technic.util.SimpleReportFiller;
import com.ay_za.ataylar_technic.util.TimeHelper;
import com.ay_za.ataylar_technic.vm.FileResponseVM;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MaintenanceChecklistPdfService implements MaintenanceChecklistPdfServiceImpl {

    private final SystemInfoServiceImpl systemInfoServiceImpl;

    public MaintenanceChecklistPdfService(SystemInfoServiceImpl systemInfoServiceImpl) {
        this.systemInfoServiceImpl = systemInfoServiceImpl;
    }


    @Override
    public FileResponseVM exportPdf(MaintenanceChecklistModel report) throws IOException {


        if (Objects.isNull(report)) {
            return null;
        }

        List<SystemInfoDto> checklistsBySystemName = systemInfoServiceImpl.getChecklistsBySystemName(report.getSystemName());

        report.setDescription(report.getDescription());
        report.setCustomerFirmName(report.getCustomerFirmName());
        report.setCustomerAddress(report.getCustomerAddress());
        report.setAuthorizedPersonnel(report.getAuthorizedPersonnel());
        report.setTelNo(report.getTelNo());
        report.setSystemName(report.getSystemName());
        report.setGsmNo(report.getGsmNo());
        report.setEmail(report.getEmail());
        report.setProductSerialNo(report.getProductSerialNo());
        report.setProductBrand(report.getProductBrand());
        report.setProductModel(report.getProductModel());
        report.setProductPurpose(report.getProductPurpose());
        report.setServiceCase(report.getServiceCase());
        report.setBlockName(report.getBlockName());
        report.setFloor(report.getFloor());
        report.setLocation(report.getLocation());
        report.setServiceDate(DateUtil.trDateFormat.format(LocalDate.parse(report.getServiceDate())));
        report.setEntryTime(TimeHelper.toHourMinuteSecondFormat(report.getEntryTime()));
        report.setExitTime(TimeHelper.toHourMinuteSecondFormat(report.getExitTime()));
        report.setServiceCarPlate(report.getServiceCarPlate());
        report.setServiceCarKm(report.getServiceCarKm());
        report.setServicePersonnel(report.getServicePersonnel());
        report.setImage1(report.getImage1());
        report.setImage2(report.getImage2());
        report.setImage3(report.getImage3());
        report.setSystemInfoDtoList(checklistsBySystemName);

        String dateInTrFormat = DateUtil.trDateFormat.format(LocalDateTime.now());

        ByteArrayOutputStream byteArrayOutputStream = this.buildPdfForMaintenanceChecklist(report);

        FileResponseVM fileResponseVM = new FileResponseVM();
        fileResponseVM.setExtension("pdf");
        fileResponseVM.setFilename(
                report.getCustomerFirmName()
                        + "_"
                        + dateInTrFormat
                        + "."
                        + fileResponseVM.getExtension());
        fileResponseVM.setFileContent(byteArrayOutputStream.toByteArray());

        return fileResponseVM;
    }

    public ByteArrayOutputStream buildPdfForMaintenanceChecklist(MaintenanceChecklistModel report)
            throws IOException {

        // List<SystemInfoDto> systemInfoList = systemInfoServiceImpl.getChecklistsBySystemName(report.getSystemName());
        //report.setSystemInfoDtoList(systemInfoList);


        // SystemInfoDto listesini MaintenanceChecklistItemDto listesine dönüştür
        List<MaintenanceChecklistItemDto> checklistItems = new ArrayList<>();
        for (SystemInfoDto systemInfo : report.getSystemInfoDtoList()) {
            MaintenanceChecklistItemDto item = MaintenanceChecklistItemDto.fromSystemInfo(systemInfo);

            // CHECKED/UNCHECKED ATAMASI
            if (report.getCheckedItems() != null && !report.getCheckedItems().isEmpty()) {
                Integer systemOrderNo = systemInfo.getSystemOrderNo();
                Boolean isChecked = report.getCheckedItems().get(systemOrderNo);

                if (Boolean.TRUE.equals(isChecked)) {
                    item.setChecked("X");
                    item.setUnchecked("");
                } else {
                    item.setChecked("");
                    item.setUnchecked("X");
                }
            } else {
                item.setChecked("X");
                item.setUnchecked("");
            }

            checklistItems.add(item);
        }

        // ItemDataSource için veri hazırlama
        List<Map<String, Object>> itemDataList = new ArrayList<>();
        for (MaintenanceChecklistItemDto item : checklistItems) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("item", item.getItem());
            itemMap.put("checked", item.getChecked());
            itemMap.put("unchecked", item.getUnchecked());
            itemDataList.add(itemMap);
        }

        Map<String, Object> parameters = new HashMap<>();
        if (!ObjectUtils.isEmpty(report)) {
            parameters.put("ItemDataSource", new JRBeanCollectionDataSource(itemDataList));
            // Null kontrolü ile parametreleri ekle - null ise boş string ata
            parameters.put("description", report.getDescription() != null ? report.getDescription() : "");
            parameters.put("customerFirmName", report.getCustomerFirmName() != null ? report.getCustomerFirmName() : "");
            parameters.put("customerAddress", report.getCustomerAddress() != null ? report.getCustomerAddress() : "");
            parameters.put("authorizedPersonnel", report.getAuthorizedPersonnel() != null ? report.getAuthorizedPersonnel() : "");
            parameters.put("telNo", report.getTelNo() != null ? report.getTelNo() : "");
            parameters.put("systemName", report.getSystemName() != null ? report.getSystemName() : "");
            parameters.put("gsmNo", report.getGsmNo() != null ? report.getGsmNo() : "");
            parameters.put("email", report.getEmail() != null ? report.getEmail() : "");
            parameters.put("productSerialNo", report.getProductSerialNo() != null ? report.getProductSerialNo() : "");
            parameters.put("productBrand", report.getProductBrand() != null ? report.getProductBrand() : "");
            parameters.put("productModel", report.getProductModel() != null ? report.getProductModel() : "");
            parameters.put("productPurpose", report.getProductPurpose() != null ? report.getProductPurpose() : "");
            parameters.put("serviceCase", report.getServiceCase() != null ? report.getServiceCase() : "");
            parameters.put("blockName", report.getBlockName() != null ? report.getBlockName() : "");
            parameters.put("floor", report.getFloor() != null ? report.getFloor() : "");
            parameters.put("location", report.getLocation() != null ? report.getLocation() : "");
            parameters.put("serviceDate", report.getServiceDate() != null ? report.getServiceDate() : "");
            parameters.put("entryTime", report.getEntryTime() != null ? TimeHelper.toHourMinuteSecondFormat(report.getEntryTime()) : "");
            parameters.put("exitTime", report.getExitTime() != null ? TimeHelper.toHourMinuteSecondFormat(report.getExitTime()) : "");
            parameters.put("serviceCarPlate", report.getServiceCarPlate() != null ? report.getServiceCarPlate() : "");
            parameters.put("serviceCarKm", report.getServiceCarKm() != null ? report.getServiceCarKm() : "");
            parameters.put("servicePersonnel", report.getServicePersonnel() != null ? report.getServicePersonnel() : "");
            parameters.put("image1", report.getImage1() != null ? report.getImage1() : "");
            parameters.put("image2", report.getImage2() != null ? report.getImage2() : "");
            parameters.put("image3", report.getImage3() != null ? report.getImage3() : "");
        }

        JasperPrint jasperPrint =
                this.createJasperPrint(
                        new JRBeanCollectionDataSource(checklistItems),
                        parameters
                );

        SimpleReportExporter simpleReportExporter = new SimpleReportExporter(jasperPrint);
        return simpleReportExporter.exportToPdf("Periyodik Bakım Çeklisti.pdf", "Ataylar Teknik");
    }

    private JasperPrint createJasperPrint(
            JRBeanCollectionDataSource dataSource,
            Map<String, Object> parameters) {
        SimpleReportFiller simpleReportFiller =
                new SimpleReportFiller("MaintenanceChecklist.jrxml", dataSource, parameters);
        simpleReportFiller.prepareReport();
        return simpleReportFiller.getJasperPrint();
    }

//    /**
//     * PDF çıktısını dosyaya kaydetme metodu
//     *
//     * @param byteArrayOutputStream PDF içeriği
//     * @param fileName              Dosya adı
//     * @return Kaydedilen dosyanın tam yolu
//     * @throws IOException Dosya yazma hatası
//     */
//    public String savePdfToFile(ByteArrayOutputStream byteArrayOutputStream, String fileName) throws IOException {
//        // pdf-files klasörünün yolunu belirle
//        Path pdfDirectory = Paths.get("pdf-files");
//
//        // Klasör yoksa oluştur
//        if (!Files.exists(pdfDirectory)) {
//            Files.createDirectories(pdfDirectory);
//        }
//
//        // Dosya adını temizle (Türkçe karakter ve özel karakterleri düzelt)
//        String cleanFileName = fileName
//                .replaceAll("[^a-zA-Z0-9.-]", "_")
//                .replaceAll("_{2,}", "_");
//
//        // Tam dosya yolunu oluştur
//        Path filePath = pdfDirectory.resolve(cleanFileName);
//
//        // PDF'i dosyaya yaz
//        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
//            byteArrayOutputStream.writeTo(fos);
//            fos.flush();
//        }
//
//        return filePath.toAbsolutePath().toString();
//    }

//    /**
//     * ByteArrayOutputStream'i PDF dosyası olarak kaydeder
//     *
//     * @param pdfStream    PDF içeriği
//     * @param baseFileName Dosya adı (örn: "bakim-listesi")
//     * @return Kaydedilen dosyanın tam yolu
//     * @throws IOException Dosya yazma hatası
//     */
//    public String savePdfStreamToFile(ByteArrayOutputStream pdfStream, String baseFileName) throws IOException {
//        if (pdfStream == null || pdfStream.size() == 0) {
//            throw new IllegalArgumentException("PDF stream boş olamaz!");
//        }
//
//        // Klasör yolunu belirle
//        Path pdfDirectory = Paths.get("pdf-files");
//
//        // Klasör yoksa oluştur
//        if (!Files.exists(pdfDirectory)) {
//            Files.createDirectories(pdfDirectory);
//            System.out.println("✓ pdf-files klasörü oluşturuldu: " + pdfDirectory.toAbsolutePath());
//        }
//
//        // Dosya adını oluştur (timestamp ile unique yap)
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
//        String fileName = String.format("%s_%s.pdf", baseFileName, timestamp);
//
//        // Tam dosya yolunu oluştur
//        Path filePath = pdfDirectory.resolve(fileName);
//
//        // PDF'i dosyaya yaz
//        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
//            pdfStream.writeTo(fos);
//            System.out.println("✓ PDF dosyası kaydedildi: " + filePath.toAbsolutePath());
//        }
//
//        return filePath.toAbsolutePath().toString();
//    }

//    @Override
//    public String testGenerateAndSavePdf() throws IOException {
//        System.out.println("=== PDF OLUŞTURMA TESTİ BAŞLIYOR ===");
//
//        // Test verisi oluştur
//        MaintenanceChecklistModel report = this.test();
//
//        System.out.println("\n✓ PDF oluşturuluyor...");
//        ByteArrayOutputStream pdfStream = buildPdfForMaintenanceChecklist(report);
//
//        System.out.println("✓ PDF boyutu: " + pdfStream.size() + " bytes");
//
//        // PDF'i dosyaya kaydet
//        String savedPath = savePdfStreamToFile(pdfStream, "periyodik-bakim-listesi");
//
//        System.out.println("✓ PDF kaydedildi: " + savedPath);
//        System.out.println("=== TEST TAMAMLANDI ===\n");
//
//        return savedPath;
//    }

}
