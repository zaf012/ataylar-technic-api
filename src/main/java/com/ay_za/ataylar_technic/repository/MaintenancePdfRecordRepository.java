package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.MaintenancePdfRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MaintenancePdfRecordRepository extends JpaRepository<MaintenancePdfRecord, String> {

    /**
     * Müşteri firma adına göre kayıtları listeler
     */
    List<MaintenancePdfRecord> findByCustomerFirmNameContainingIgnoreCase(String customerFirmName);

    /**
     * Sistem adına göre kayıtları listeler
     */
    List<MaintenancePdfRecord> findBySystemNameContainingIgnoreCase(String systemName);

    /**
     * Tarih aralığına göre kayıtları listeler
     */
    List<MaintenancePdfRecord> findByServiceDateBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Çoklu filtre desteği
     */
    @Query("SELECT m FROM MaintenancePdfRecord m WHERE " +
           "(:customerName IS NULL OR LOWER(m.customerFirmName) LIKE LOWER(CONCAT('%', :customerName, '%'))) AND " +
           "(:systemName IS NULL OR LOWER(m.systemName) LIKE LOWER(CONCAT('%', :systemName, '%'))) AND " +
           "(:startDate IS NULL OR m.serviceDate >= :startDate) AND " +
           "(:endDate IS NULL OR m.serviceDate <= :endDate) " +
           "ORDER BY m.createdAt DESC")
    List<MaintenancePdfRecord> findByFilters(
        @Param("customerName") String customerName,
        @Param("systemName") String systemName,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    /**
     * En son oluşturulan kayıtları listeler
     */
    List<MaintenancePdfRecord> findTop10ByOrderByCreatedAtDesc();

    /**
     * En son oluşturulan kaydın rapor no'sunu getirir
     */
    @Query("SELECT COALESCE(MAX(m.reportNo), 0) FROM MaintenancePdfRecord m")
    Integer findMaxReportNo();

}

