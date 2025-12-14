package com.ay_za.ataylar_technic.controller;

import com.ay_za.ataylar_technic.dto.MaintenancePdfRecordDto;
import com.ay_za.ataylar_technic.service.base.MaintenancePdfRecordServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance-pdf")
@Tag(name = "Periyodik Bakım PDF Yönetimi", description = "PDF kayıtlarını listeleme ve indirme işlemleri")
public class MaintenancePdfRecordController {

    private final MaintenancePdfRecordServiceImpl pdfRecordService;

    public MaintenancePdfRecordController(MaintenancePdfRecordServiceImpl pdfRecordService) {
        this.pdfRecordService = pdfRecordService;
    }

    @GetMapping("/list")
    @Operation(summary = "PDF Kayıtlarını Listele", description = "Filtrelere göre PDF kayıtlarını listeler")
    public ResponseEntity<List<MaintenancePdfRecordDto>> listPdfRecords(
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String systemName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<MaintenancePdfRecordDto> dtos = pdfRecordService.listPdfRecords(
                customerName, systemName, startDate, endDate
        );


        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/recent")
    @Operation(summary = "Son Oluşturulan PDF'leri Getir", description = "En son oluşturulan 10 PDF kaydını getirir")
    public ResponseEntity<List<MaintenancePdfRecordDto>> getRecentPdfs() {
        List<MaintenancePdfRecordDto> dtos = pdfRecordService.getRecentPdfs();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "PDF İndir", description = "ID'ye göre PDF dosyasını indirir")
    public ResponseEntity<Resource> downloadPdf(@PathVariable String id) throws Exception {
        Resource resource = pdfRecordService.downloadPdf(id);
        String fileName = pdfRecordService.getFileName(id);
        Long fileSize = pdfRecordService.getFileSize(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(fileSize)
                .body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "PDF Kaydı Detay", description = "ID'ye göre PDF kaydının detaylarını getirir")
    public ResponseEntity<MaintenancePdfRecordDto> getPdfRecordById(@PathVariable String id) {
        MaintenancePdfRecordDto dto = pdfRecordService.getPdfRecordById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "PDF Kaydını Sil", description = "ID'ye göre PDF kaydını ve dosyasını siler")
    public ResponseEntity<String> deletePdfRecord(@PathVariable String id) throws Exception {
        String deletedFileName = pdfRecordService.deletePdfRecord(id);
        return ResponseEntity.ok("PDF kaydı ve dosyası başarıyla silindi: " + deletedFileName);
    }

    @GetMapping("/customer/{customerName}")
    @Operation(summary = "Müşteriye Göre PDF Listele", description = "Belirtilen müşteriye ait PDF'leri listeler")
    public ResponseEntity<List<MaintenancePdfRecordDto>> getPdfsByCustomer(
            @PathVariable String customerName) {

        List<MaintenancePdfRecordDto> dtos = pdfRecordService.getPdfsByCustomer(customerName);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/system/{systemName}")
    @Operation(summary = "Sisteme Göre PDF Listele", description = "Belirtilen sisteme ait PDF'leri listeler")
    public ResponseEntity<List<MaintenancePdfRecordDto>> getPdfsBySystem(
            @PathVariable String systemName) {

        List<MaintenancePdfRecordDto> dtos = pdfRecordService.getPdfsBySystem(systemName);
        return ResponseEntity.ok(dtos);
    }
}

