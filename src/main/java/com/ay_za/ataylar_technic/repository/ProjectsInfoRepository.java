package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.ProjectsInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectsInfoRepository extends JpaRepository<ProjectsInfo, String> {

    // Proje adına göre arama (case-insensitive)
    Optional<ProjectsInfo> findByProjectNameIgnoreCase(String projectName);

    // Proje adında arama (contains)
    List<ProjectsInfo> findByProjectNameContainingIgnoreCase(String projectName);

    // Proje adının var olup olmadığını kontrol et (case-insensitive)
    boolean existsByProjectNameIgnoreCase(String projectName);

    // ID'ye göre proje getir
    Optional<ProjectsInfo> findById(String id);

    // Firma ID'sine göre projeleri getir
    List<ProjectsInfo> findByFirmId(String firmId);

    // Firma ID'sine göre projeleri sıralı getir (alfabetik)
    List<ProjectsInfo> findByFirmIdOrderByProjectNameAsc(String firmId);

    // Proje adına göre sıralı liste (alfabetik)
    List<ProjectsInfo> findAllByOrderByProjectNameAsc();

    // Belirli firmada proje sayısını getir
    @Query("SELECT COUNT(p) FROM ProjectsInfo p WHERE p.firmId = ?1")
    Integer countProjectsByFirmId(String firmId);

    // Toplam proje sayısını getir
    @Query("SELECT COUNT(p) FROM ProjectsInfo p")
    Integer countProjects();

    // Tüm projeleri getir
    List<ProjectsInfo> findAll();

    // Firma ID ve proje adı kombinasyonunun varlığını kontrol et
    boolean existsByFirmIdAndProjectNameIgnoreCase(String firmId, String projectName);
}
