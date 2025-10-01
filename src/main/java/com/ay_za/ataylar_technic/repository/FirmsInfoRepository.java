package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.FirmsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FirmsInfoRepository extends JpaRepository<FirmsInfo, String> {

    // Firma adına göre arama (case-insensitive)
    Optional<FirmsInfo> findByFirmNameIgnoreCase(String firmName);

    // Firma adında arama (contains)
    List<FirmsInfo> findByFirmNameContainingIgnoreCase(String firmName);

    // Firma adının var olup olmadığını kontrol et (case-insensitive)
    boolean existsByFirmNameIgnoreCase(String firmName);

    // ID'ye göre firma getir
    Optional<FirmsInfo> findById(String id);

    // Firma adına göre sıralı liste (alfabetik)
    List<FirmsInfo> findAllByOrderByFirmNameAsc();

    // Firma sayısını getir
    @Query("SELECT COUNT(f) FROM FirmsInfo f")
    Integer countFirms();

    // Tüm firmaları getir
    List<FirmsInfo> findAll();
}
