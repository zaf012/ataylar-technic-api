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

    // Ana kategorileri getir (mainCategoryId null olanlar veya isMainCategory true olanlar)
    @Query("SELECT ic FROM InventoryCategory ic WHERE ic.isMainCategory = true OR ic.mainCategoryId IS NULL ORDER BY ic.sortOrder")
    List<InventoryCategory> findMainCategories();

    // Belirli bir ana kategorinin alt kategorilerini getir
    @Query("SELECT ic FROM InventoryCategory ic WHERE ic.mainCategoryId = :mainCategoryId ORDER BY ic.sortOrder")
    List<InventoryCategory> findByMainCategoryId(@Param("mainCategoryId") String mainCategoryId);

    // Aktif kategorileri getir
    List<InventoryCategory> findByIsActiveTrue();

    // Aktif ana kategorileri getir
    @Query("SELECT ic FROM InventoryCategory ic WHERE ic.isActive = true AND (ic.isMainCategory = true OR ic.mainCategoryId IS NULL) ORDER BY ic.sortOrder")
    List<InventoryCategory> findActiveMainCategories();

    // Belirli bir ana kategorinin aktif alt kategorilerini getir
    @Query("SELECT ic FROM InventoryCategory ic WHERE ic.mainCategoryId = :mainCategoryId AND ic.isActive = true ORDER BY ic.sortOrder")
    List<InventoryCategory> findActiveSubCategoriesByMainCategoryId(@Param("mainCategoryId") String mainCategoryId);

    // Category code ile arama
    Optional<InventoryCategory> findByCategoryCode(String categoryCode);

    // QR code ile arama
    Optional<InventoryCategory> findByQrCode(String qrCode);

    // Category name ile arama
    List<InventoryCategory> findByCategoryNameContainingIgnoreCase(String categoryName);

    // Market code ile arama
    List<InventoryCategory> findByMarketCode(String marketCode);

    // Hiyerarşik yapı için tüm kategorileri getir (ana kategoriler ve alt kategoriler)
    @Query("SELECT ic FROM InventoryCategory ic ORDER BY ic.mainCategoryId NULLS FIRST, ic.sortOrder")
    List<InventoryCategory> findAllOrderedByHierarchy();

    // Kategori kodu varlık kontrolü
    boolean existsByCategoryCode(String categoryCode);

    // QR kodu varlık kontrolü
    boolean existsByQrCode(String qrCode);
}

