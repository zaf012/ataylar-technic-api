package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.InventoryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryCategoryRepository extends JpaRepository<InventoryCategory, String> {

    // Ana kategorileri getir (mainCategoryId null olanlar)
    // Sınırsız derinlik için sadece mainCategoryId null olanlar ana kategoridir
    @Query("SELECT ic FROM InventoryCategory ic WHERE ic.mainCategoryId IS NULL ORDER BY ic.categoryName")
    List<InventoryCategory> findMainCategories();

    // Belirli bir üst kategorinin doğrudan alt kategorilerini getir
    // Not: Bu metod sadece doğrudan alt kategorileri getirir, alt-alt kategorileri getirmez
    @Query("SELECT ic FROM InventoryCategory ic WHERE ic.mainCategoryId = :mainCategoryId ORDER BY ic.categoryName")
    List<InventoryCategory> findByMainCategoryId(@Param("mainCategoryId") String mainCategoryId);

    // Aktif kategorileri getir
    List<InventoryCategory> findByIsActiveTrue();

    // Aktif ana kategorileri getir (sadece en üst seviye)
    @Query("SELECT ic FROM InventoryCategory ic WHERE ic.isActive = true AND ic.mainCategoryId IS NULL ORDER BY ic.categoryName")
    List<InventoryCategory> findActiveMainCategories();

    // Belirli bir üst kategorinin aktif doğrudan alt kategorilerini getir
    @Query("SELECT ic FROM InventoryCategory ic WHERE ic.mainCategoryId = :mainCategoryId AND ic.isActive = true ORDER BY ic.categoryName")
    List<InventoryCategory> findActiveSubCategoriesByMainCategoryId(@Param("mainCategoryId") String mainCategoryId);

    // Category name ile arama (hiyerarşik ad değil, gerçek kategori adı)
    List<InventoryCategory> findByCategoryNameContainingIgnoreCase(String categoryName);

    // Market code ile arama
    List<InventoryCategory> findByMarketCode(String marketCode);

    // Hiyerarşik yapı için tüm kategorileri getir (ana kategoriler önce, sonra alt kategoriler)
    @Query("SELECT ic FROM InventoryCategory ic ORDER BY ic.mainCategoryId NULLS FIRST, ic.categoryName")
    List<InventoryCategory> findAllOrderedByHierarchy();
}

