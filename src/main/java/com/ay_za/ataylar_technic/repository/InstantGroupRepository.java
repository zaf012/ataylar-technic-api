package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.InstantGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstantGroupRepository extends JpaRepository<InstantGroup, String> {

    // Aktif grupları getir
    List<InstantGroup> findByIsActiveTrue();

    // Grup adına göre arama (exact match)
    Optional<InstantGroup> findByGroupNameAndIsActiveTrue(String groupName);

    // Grup adına göre arama (case-insensitive)
    Optional<InstantGroup> findByGroupNameIgnoreCaseAndIsActiveTrue(String groupName);

    // Grup adında arama (contains)
    List<InstantGroup> findByGroupNameContainingIgnoreCaseAndIsActiveTrue(String groupName);

    // Grup adının var olup olmadığını kontrol et
    boolean existsByGroupNameAndIsActiveTrue(String groupName);

    // Grup adının var olup olmadığını kontrol et (case-insensitive)
    boolean existsByGroupNameIgnoreCaseAndIsActiveTrue(String groupName);

    // Belirli bir kullanıcı tarafından oluşturulan gruplar
    List<InstantGroup> findByCreatedByAndIsActiveTrue(String createdBy);

    // Belirli bir kullanıcı tarafından güncellenen gruplar
    List<InstantGroup> findByUpdatedByAndIsActiveTrue(String updatedBy);

    // ID'ye göre aktif grup getir
    Optional<InstantGroup> findByIdAndIsActiveTrue(String id);

    // Grup adına göre sıralı liste (alfabetik)
    List<InstantGroup> findByIsActiveTrueOrderByGroupNameAsc();

    // Oluşturma tarihine göre sıralı liste (en yeni önce)
    List<InstantGroup> findByIsActiveTrueOrderByCreatedDateDesc();

    // Güncelleme tarihine göre sıralı liste (en son güncellenen önce)
    List<InstantGroup> findByIsActiveTrueOrderByUpdatedDateDesc();

    // Belirli bir kullanıcının oluşturduğu grupları tarih sırasına göre getir
    List<InstantGroup> findByCreatedByAndIsActiveTrueOrderByCreatedDateDesc(String createdBy);

    // Grup sayısını getir
    @Query("SELECT COUNT(g) FROM InstantGroup g WHERE g.isActive = true")
    Integer countActiveGroups();

    // Grup adı ile arama yaparken case-insensitive ve trim
    @Query("SELECT g FROM InstantGroup g WHERE UPPER(TRIM(g.groupName)) = UPPER(TRIM(:groupName)) AND g.isActive = true")
    Optional<InstantGroup> findByGroupNameTrimmed(@Param("groupName") String groupName);

    // Kullanıcı aktivitesi bazında grupları getir
    @Query("SELECT g FROM InstantGroup g WHERE (g.createdBy = :user OR g.updatedBy = :user) AND g.isActive = true ORDER BY g.updatedDate DESC")
    List<InstantGroup> findGroupsByUserActivity(@Param("user") String user);

    // En çok kullanılan grupları getir (InstantAccount tablosu ile join)
    @Query("SELECT g FROM InstantGroup g WHERE g.isActive = true AND " +
            "g.id IN (SELECT DISTINCT a.accountGroupId FROM InstantAccount a WHERE a.isActive = true) " +
            "ORDER BY g.groupName ASC")
    List<InstantGroup> findActiveGroupsWithAccounts();

    // Hiç hesabı olmayan grupları getir
    @Query("SELECT g FROM InstantGroup g WHERE g.isActive = true AND " +
            "g.id NOT IN (SELECT DISTINCT a.accountGroupId FROM InstantAccount a WHERE a.isActive = true AND a.accountGroupId IS NOT NULL) " +
            "ORDER BY g.groupName ASC")
    List<InstantGroup> findGroupsWithoutAccounts();
}
