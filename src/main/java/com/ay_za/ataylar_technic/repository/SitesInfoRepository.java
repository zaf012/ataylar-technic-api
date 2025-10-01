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

    // Site adı ve blok adı kombinasyonunun varlığını kontrol et
    boolean existsBySiteNameAndBlockNameIgnoreCase(String siteName, String blockName);

    // Belirli bir site adına sahip tüm blokları getir
    List<SitesInfo> findBySiteNameIgnoreCaseOrderByBlockNameAsc(String siteName);

    // Site adına göre arama (contains)
    List<SitesInfo> findBySiteNameContainingIgnoreCaseOrderBySiteNameAsc(String siteName);

    // Proje ID'sine göre siteleri getir
    List<SitesInfo> findByProjectIdOrderBySiteNameAsc(String projectId);

    // Site adı ve proje ID kombinasyonu ile arama
    List<SitesInfo> findBySiteNameIgnoreCaseAndProjectId(String siteName, String projectId);
}
