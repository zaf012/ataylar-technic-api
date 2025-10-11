package com.ay_za.ataylar_technic.error;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, String> {

    // Severity'ye göre filtreleme
    List<ErrorLog> findBySeverity(String severity);

    // Çözüm durumuna göre filtreleme
    List<ErrorLog> findByResolved(Boolean resolved);

    // Tarih aralığına göre filtreleme
    List<ErrorLog> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Exception tipine göre filtreleme
    List<ErrorLog> findByExceptionType(String exceptionType);

    // Error code'a göre filtreleme
    List<ErrorLog> findByErrorCode(String errorCode);

    // User ID'ye göre filtreleme
    List<ErrorLog> findByUserId(String userId);

    // Pagination ile tüm logları getir (en yeni önce)
    Page<ErrorLog> findAllByOrderByCreatedDateDesc(Pageable pageable);

    // Çözülmemiş hatalar (en yeni önce)
    Page<ErrorLog> findByResolvedFalseOrderByCreatedDateDesc(Pageable pageable);

    // Belirli severity ve çözüm durumuna göre sayfalama
    Page<ErrorLog> findBySeverityAndResolvedOrderByCreatedDateDesc(String severity, Boolean resolved, Pageable pageable);

    // Son 24 saatteki hatalar
    @Query("SELECT e FROM ErrorLog e WHERE e.createdDate >= :since ORDER BY e.createdDate DESC")
    List<ErrorLog> findRecentErrors(@Param("since") LocalDateTime since);

    // Error statistics
    @Query("SELECT e.severity, COUNT(e) FROM ErrorLog e GROUP BY e.severity")
    List<Object[]> getErrorStatsBySeverity();

    @Query("SELECT e.exceptionType, COUNT(e) FROM ErrorLog e GROUP BY e.exceptionType ORDER BY COUNT(e) DESC")
    List<Object[]> getErrorStatsByExceptionType();

    // Belirli URL'deki hatalar
    List<ErrorLog> findByRequestUrlContaining(String url);
}
