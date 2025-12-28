package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.MaintenancePdfRecordDto;
import com.ay_za.ataylar_technic.entity.MaintenancePdfRecord;
import com.ay_za.ataylar_technic.mapper.MaintenancePdfRecordMapper;
import com.ay_za.ataylar_technic.repository.MaintenancePdfRecordRepository;
import com.ay_za.ataylar_technic.service.base.MaintenancePdfRecordServiceImpl;
import com.ay_za.ataylar_technic.service.model.MaintenanceChecklistModel;
import com.ay_za.ataylar_technic.util.DateUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MaintenancePdfRecordService implements MaintenancePdfRecordServiceImpl {

    private final MaintenancePdfRecordRepository pdfRecordRepository;
    private final MaintenancePdfRecordMapper pdfRecordMapper;

    public MaintenancePdfRecordService(
            MaintenancePdfRecordRepository pdfRecordRepository,
            MaintenancePdfRecordMapper pdfRecordMapper) {
        this.pdfRecordRepository = pdfRecordRepository;
        this.pdfRecordMapper = pdfRecordMapper;
    }

    /**
     * PDF metadata'sını veritabanına kaydeder
     *
     * @param report   Bakım raporu modeli
     * @param fileName Dosya adı
     * @param filePath Dosya yolu
     * @param fileSize Dosya boyutu (bytes)
     */
    @Transactional
    @Override
    public void savePdfMetadata(MaintenanceChecklistModel report, String fileName,
                                String filePath, long fileSize) {

        Integer lastReportNo = pdfRecordRepository.findMaxReportNo();

        MaintenancePdfRecord record = new MaintenancePdfRecord();
        record.setCustomerFirmName(report.getCustomerFirmName());
        record.setCustomerAddress(report.getCustomerAddress());
        record.setAuthorizedPersonnel(report.getAuthorizedPersonnel());
        record.setTelNo(report.getTelNo());
        record.setGsmNo(report.getGsmNo());
        record.setEmail(report.getEmail());
        record.setSystemName(report.getSystemName());
        record.setProductSerialNo(report.getProductSerialNo());
        record.setServiceDate(DateUtil.stringToLocalDate(report.getServiceDate()));
        record.setDescription(report.getDescription());
        record.setFileName(fileName);
        record.setFilePath(filePath);
        record.setFileSizeBytes(fileSize);
        record.setCreatedAt(LocalDateTime.now());
        record.setReportNo(lastReportNo != null ? lastReportNo + 1 : 1);
        record.setCreatedBy("SYSTEM"); // İleride kullanıcı bilgisi eklenebilir


        pdfRecordRepository.save(record);
    }

    @Override
    public List<MaintenancePdfRecordDto> listPdfRecords(String customerName, String systemName,
                                                        LocalDate startDate, LocalDate endDate) {
        List<MaintenancePdfRecord> records = pdfRecordRepository.findByFilters(
                customerName, systemName, startDate, endDate
        );
        return pdfRecordMapper.toDtoList(records);
    }

    @Override
    public List<MaintenancePdfRecordDto> getRecentPdfs() {
        List<MaintenancePdfRecord> records = pdfRecordRepository.findTop10ByOrderByCreatedAtDesc();
        return pdfRecordMapper.toDtoList(records);
    }

    @Override
    public Resource downloadPdf(String id) throws Exception {
        MaintenancePdfRecord record = pdfRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PDF kaydı bulunamadı: ID=" + id));

        Path filePath = Paths.get(record.getFilePath());

        // Dosya var mı kontrol et
        if (!Files.exists(filePath)) {
            throw new RuntimeException("PDF dosyası bulunamadı: " + record.getFilePath());
        }

        return new FileSystemResource(filePath);
    }

    @Override
    public String getFileName(String id) {
        MaintenancePdfRecord record = pdfRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PDF kaydı bulunamadı: ID=" + id));
        return record.getFileName();
    }

    @Override
    public Long getFileSize(String id) {
        MaintenancePdfRecord record = pdfRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PDF kaydı bulunamadı: ID=" + id));
        return record.getFileSizeBytes();
    }

    @Override
    public MaintenancePdfRecordDto getPdfRecordById(String id) {
        MaintenancePdfRecord record = pdfRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PDF kaydı bulunamadı: ID=" + id));
        return pdfRecordMapper.toDto(record);
    }

    @Override
    @Transactional
    public String deletePdfRecord(String id) throws Exception {
        MaintenancePdfRecord record = pdfRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PDF kaydı bulunamadı: ID=" + id));

        Path filePath = Paths.get(record.getFilePath());

        // Dosyayı sil
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        // Veritabanı kaydını sil
        pdfRecordRepository.deleteById(id);

        return record.getFileName();
    }

    @Override
    public List<MaintenancePdfRecordDto> getPdfsByCustomer(String customerName) {
        List<MaintenancePdfRecord> records = pdfRecordRepository
                .findByCustomerFirmNameContainingIgnoreCase(customerName);
        return pdfRecordMapper.toDtoList(records);
    }

    @Override
    public List<MaintenancePdfRecordDto> getPdfsBySystem(String systemName) {
        List<MaintenancePdfRecord> records = pdfRecordRepository
                .findBySystemNameContainingIgnoreCase(systemName);
        return pdfRecordMapper.toDtoList(records);
    }

    @Override
    public Integer getLastReportNo() {
        return pdfRecordRepository.findMaxReportNo();
    }

}

