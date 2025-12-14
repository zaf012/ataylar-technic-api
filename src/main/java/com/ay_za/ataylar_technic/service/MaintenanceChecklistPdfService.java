package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.SystemInfoDto;
import com.ay_za.ataylar_technic.service.base.MaintenanceChecklistPdfServiceImpl;
import com.ay_za.ataylar_technic.service.base.MaintenancePdfRecordServiceImpl;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class MaintenanceChecklistPdfService implements MaintenanceChecklistPdfServiceImpl {

    private final SystemInfoServiceImpl systemInfoServiceImpl;
    private final MaintenancePdfRecordServiceImpl maintenancePdfRecordServiceImpl;


    @Value("${pdf.storage.path:pdf-files}")
    private String pdfStoragePath;

    public MaintenanceChecklistPdfService(
            SystemInfoServiceImpl systemInfoServiceImpl, MaintenancePdfRecordServiceImpl maintenancePdfRecordServiceImpl) {
        this.systemInfoServiceImpl = systemInfoServiceImpl;
        this.maintenancePdfRecordServiceImpl = maintenancePdfRecordServiceImpl;
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

        // Dosya adını oluştur
        String fileName = report.getCustomerFirmName() + "_" + dateInTrFormat + ".pdf";

        // PDF'i dosya sistemine kaydet
        String savedPath = savePdfToFileSystem(byteArrayOutputStream, fileName);

        // Metadata'yı veritabanına kaydet
        maintenancePdfRecordServiceImpl.savePdfMetadata(report, fileName, savedPath, byteArrayOutputStream.size());

        FileResponseVM fileResponseVM = new FileResponseVM();
        fileResponseVM.setExtension("pdf");
        fileResponseVM.setFilename(fileName);
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

    /**
     * PDF dosyasını dosya sistemine kaydeder
     *
     * @param pdfStream PDF içeriği
     * @param fileName  Dosya adı
     * @return Kaydedilen dosyanın tam yolu
     * @throws IOException Dosya yazma hatası
     */
    private String savePdfToFileSystem(ByteArrayOutputStream pdfStream, String fileName) throws IOException {
        Path directory = Paths.get(pdfStoragePath);

        // Klasör yoksa oluştur
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        // Dosya adını temizle (Türkçe karakter ve özel karakterler için)
        String cleanFileName = fileName
                .replaceAll("[^a-zA-Z0-9.-]", "_")
                .replaceAll("_{2,}", "_");

        // Timestamp ekle (dosya adı unique olsun)
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uniqueFileName = cleanFileName.replace(".pdf", "_" + timestamp + ".pdf");

        Path filePath = directory.resolve(uniqueFileName);

        // PDF'i dosyaya yaz
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            pdfStream.writeTo(fos);
        }

        return filePath.toAbsolutePath().toString();
    }
}
