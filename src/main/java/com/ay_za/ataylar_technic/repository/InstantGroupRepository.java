package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.InstantGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstantGroupRepository extends JpaRepository<InstantGroup, String> {

    // Grup adına göre arama (case-insensitive)
    Optional<InstantGroup> findByGroupNameIgnoreCase(String groupName);

    // Grup adında arama (contains)
    List<InstantGroup> findByGroupNameContainingIgnoreCase(String groupName);

    // Grup adının var olup olmadığını kontrol et (case-insensitive)
    boolean existsByGroupNameIgnoreCase(String groupName);

    // ID'ye göre aktif grup getir
    Optional<InstantGroup> findById(String id);

    // Grup adına göre sıralı liste (alfabetik)
    List<InstantGroup> findByOrderByGroupNameAsc();

    // Grup sayısını getir
    @Query("SELECT COUNT(g) FROM InstantGroup g")
    Integer countGroups();

    // Tüm grupları getir (veritabanından bağımsız)
    @Query("SELECT g FROM InstantGroup g")
    List<InstantGroup> findAllGroups();
}
