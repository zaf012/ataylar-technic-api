package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.InventoryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryCategoryRepository extends JpaRepository<InventoryCategory, String> {

    // Ana kategorileri getir (parent'ı null olanlar)
    @Query("SELECT c FROM InventoryCategory c WHERE c.parentCategory IS NULL ORDER BY c.sortOrder")
    List<InventoryCategory> findRootCategories();

    // Aktif ana kategorileri getir
    @Query("SELECT c FROM InventoryCategory c WHERE c.parentCategory IS NULL AND c.isActive = true ORDER BY c.sortOrder")
    List<InventoryCategory> findActiveRootCategories();

    // Belirli bir kategorinin alt kategorilerini getir
    @Query("SELECT c FROM InventoryCategory c WHERE c.parentCategory.id = :parentId ORDER BY c.sortOrder")
    List<InventoryCategory> findByParentCategoryId(@Param("parentId") String parentId);

    // Belirli bir kategorinin aktif alt kategorilerini getir
    @Query("SELECT c FROM InventoryCategory c WHERE c.parentCategory.id = :parentId AND c.isActive = true ORDER BY c.sortOrder")
    List<InventoryCategory> findActiveByParentCategoryId(@Param("parentId") String parentId);

    // QR kod ile kategori bul
    Optional<InventoryCategory> findByQrCode(String qrCode);

    // Kategori kodu ile kategori bul
    Optional<InventoryCategory> findByCategoryCode(String categoryCode);

    // Belirli level'daki kategorileri getir
    @Query("SELECT c FROM InventoryCategory c WHERE c.level = :level ORDER BY c.sortOrder")
    List<InventoryCategory> findByLevel(@Param("level") Integer level);

    // Kategori adına göre arama (LIKE)
    @Query("SELECT c FROM InventoryCategory c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY c.sortOrder")
    List<InventoryCategory> findByCategoryNameContaining(@Param("name") String name);

    // Belirli bir kategorinin tüm alt kategorilerini recursive olarak getir
    @Query("SELECT c FROM InventoryCategory c WHERE c.parentCategory.id = :parentId " +
           "UNION ALL " +
           "SELECT sc FROM InventoryCategory sc WHERE sc.parentCategory.id IN " +
           "(SELECT c2.id FROM InventoryCategory c2 WHERE c2.parentCategory.id = :parentId)")
    List<InventoryCategory> findAllSubCategoriesRecursive(@Param("parentId") String parentId);

    // QR kod unique kontrolü
    boolean existsByQrCode(String qrCode);

    // Kategori kodu unique kontrolü
    boolean existsByCategoryCode(String categoryCode);

    // Belirli parent category'de max sort order'ı bul
    @Query("SELECT MAX(c.sortOrder) FROM InventoryCategory c WHERE c.parentCategory.id = :parentId")
    Integer findMaxSortOrderByParentId(@Param("parentId") String parentId);

    // Root kategorilerde max sort order'ı bul
    @Query("SELECT MAX(c.sortOrder) FROM InventoryCategory c WHERE c.parentCategory IS NULL")
    Integer findMaxSortOrderForRootCategories();

    // Kategorinin child sayısını getir
    @Query("SELECT COUNT(c) FROM InventoryCategory c WHERE c.parentCategory.id = :parentId")
    Long countByParentCategoryId(@Param("parentId") String parentId);

    // Aktif kategorileri getir
    List<InventoryCategory> findByIsActiveTrueOrderBySortOrder();

    // Parent category ve isim ile arama (aynı parent altında aynı isim olmasın)
    @Query("SELECT c FROM InventoryCategory c WHERE c.parentCategory.id = :parentId AND LOWER(c.categoryName) = LOWER(:categoryName)")
    Optional<InventoryCategory> findByParentCategoryIdAndCategoryNameIgnoreCase(@Param("parentId") String parentId, @Param("categoryName") String categoryName);

    // Root level'da isim ile arama
    @Query("SELECT c FROM InventoryCategory c WHERE c.parentCategory IS NULL AND LOWER(c.categoryName) = LOWER(:categoryName)")
    Optional<InventoryCategory> findRootByCategoryNameIgnoreCase(@Param("categoryName") String categoryName);
}
