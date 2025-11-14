package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.SquaresInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SquaresInfoRepository extends JpaRepository<SquaresInfo, String> {

    // ID'ye göre ada getir
    Optional<SquaresInfo> findById(String id);

    // Tüm adaları getir
    List<SquaresInfo> findAll();

    // Site ID'sine göre adaları getir
    List<SquaresInfo> findBySiteIdOrderBySquareNameAsc(String siteId);

    // Ada adının varlığını kontrol et (case-insensitive)
    boolean existsBySquareNameIgnoreCase(String squareName);

    // Site ID ve ada adı kombinasyonunun varlığını kontrol et
    boolean existsBySiteIdAndSquareNameIgnoreCase(String siteId, String squareName);

    // Ada adına göre arama (contains)
    List<SquaresInfo> findBySquareNameContainingIgnoreCaseOrderBySquareNameAsc(String squareName);

    // Site adına göre adaları getir
    List<SquaresInfo> findBySiteNameIgnoreCaseOrderBySquareNameAsc(String siteName);

    // Random ada getir
    @Query(value = "SELECT * FROM squares_info ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<SquaresInfo> findRandomSquare();

    // Belirli site için random ada getir
    @Query(value = "SELECT * FROM squares_info WHERE site_id = :siteId ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<SquaresInfo> findRandomSquareBySiteId(@Param("siteId") String siteId);
}
