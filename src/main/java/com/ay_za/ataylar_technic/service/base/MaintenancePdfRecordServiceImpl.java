package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.MaintenancePdfRecordDto;
import com.ay_za.ataylar_technic.service.model.MaintenanceChecklistModel;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface MaintenancePdfRecordServiceImpl {

    @Transactional
    void savePdfMetadata(MaintenanceChecklistModel report, String fileName,
                         String filePath, long fileSize);

    /**
     * Filtrelere göre PDF kayıtlarını listeler
     *
     * @param customerName Müşteri firma adı (opsiyonel)
     * @param systemName   Sistem adı (opsiyonel)
     * @param startDate    Başlangıç tarihi (opsiyonel)
     * @param endDate      Bitiş tarihi (opsiyonel)
     * @return PDF kayıtları listesi
     */
    List<MaintenancePdfRecordDto> listPdfRecords(String customerName, String systemName,
                                                   LocalDate startDate, LocalDate endDate);

    /**
     * En son oluşturulan 10 PDF kaydını getirir
     *
     * @return PDF kayıtları listesi
     */
    List<MaintenancePdfRecordDto> getRecentPdfs();

    /**
     * PDF dosyasını indirir
     *
     * @param id PDF kayıt ID'si
     * @return PDF dosyası resource'u
     * @throws Exception Dosya bulunamazsa veya okuma hatası
     */
    Resource downloadPdf(String id) throws Exception;

    /**
     * PDF dosya adını ID'ye göre getirir (download için)
     *
     * @param id PDF kayıt ID'si
     * @return Dosya adı
     */
    String getFileName(String id);

    /**
     * PDF dosya boyutunu ID'ye göre getirir (download için)
     *
     * @param id PDF kayıt ID'si
     * @return Dosya boyutu (bytes)
     */
    Long getFileSize(String id);

    /**
     * PDF kaydının detaylarını getirir
     *
     * @param id PDF kayıt ID'si
     * @return PDF kayıt detayları
     */
    MaintenancePdfRecordDto getPdfRecordById(String id);

    /**
     * PDF kaydını ve dosyasını siler
     *
     * @param id PDF kayıt ID'si
     * @return Silinen dosya adı
     * @throws Exception Dosya silme hatası
     */
    String deletePdfRecord(String id) throws Exception;

    /**
     * Müşteriye göre PDF kayıtlarını listeler
     *
     * @param customerName Müşteri firma adı
     * @return PDF kayıtları listesi
     */
    List<MaintenancePdfRecordDto> getPdfsByCustomer(String customerName);

    /**
     * Sisteme göre PDF kayıtlarını listeler
     *
     * @param systemName Sistem adı
     * @return PDF kayıtları listesi
     */
    List<MaintenancePdfRecordDto> getPdfsBySystem(String systemName);
}

