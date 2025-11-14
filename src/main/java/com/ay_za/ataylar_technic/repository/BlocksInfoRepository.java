package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.BlocksInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlocksInfoRepository extends JpaRepository<BlocksInfo, String> {

    // ID'ye göre blok getir
    Optional<BlocksInfo> findById(String id);

    // Tüm blokları getir
    List<BlocksInfo> findAll();

    // Ada ID'sine göre blokları getir
    List<BlocksInfo> findBySquareIdOrderByBlockNameAsc(String squareId);

    // Blok adının varlığını kontrol et (case-insensitive)
    boolean existsByBlockNameIgnoreCase(String blockName);

    // Ada ID ve blok adı kombinasyonunun varlığını kontrol et
    boolean existsBySquareIdAndBlockNameIgnoreCase(String squareId, String blockName);

    // Blok kodunun varlığını kontrol et
    boolean existsByBlockCodeIgnoreCase(String blockCode);

    // Blok adına göre arama (contains)
    List<BlocksInfo> findByBlockNameContainingIgnoreCaseOrderByBlockNameAsc(String blockName);

    // Blok koduna göre blok getir
    Optional<BlocksInfo> findByBlockCodeIgnoreCase(String blockCode);

    // Random blok getir
    @Query(value = "SELECT * FROM blocks_info ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<BlocksInfo> findRandomBlock();

    // Belirli ada için random blok getir
    @Query(value = "SELECT * FROM blocks_info WHERE square_id = :squareId ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<BlocksInfo> findRandomBlockBySquareId(@Param("squareId") String squareId);
}
