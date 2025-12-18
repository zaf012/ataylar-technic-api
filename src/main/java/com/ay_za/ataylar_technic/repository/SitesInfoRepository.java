package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.SitesInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SitesInfoRepository extends JpaRepository<SitesInfo, String> {

    // ID'ye göre site getir
    Optional<SitesInfo> findById(String id);

    // Tüm siteleri getir
    List<SitesInfo> findAll();

    // Site adının var olup olmadığını kontrol et (case-insensitive)
    boolean existsBySiteNameIgnoreCase(String siteName);

    // Site adına göre sıralı liste (alfabetik)
    List<SitesInfo> findAllByOrderBySiteNameAsc();

    // Site adına göre tüm blokları getir (aynı site adına sahip birden fazla blok olabilir)
    List<SitesInfo> findBySiteNameIgnoreCase(String siteName);

    // Site adına göre arama (contains)
    List<SitesInfo> findBySiteNameContainingIgnoreCaseOrderBySiteNameAsc(String siteName);
}