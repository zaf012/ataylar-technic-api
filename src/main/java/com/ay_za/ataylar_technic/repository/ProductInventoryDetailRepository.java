package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.ProductInventoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Ürün Envanter Detayı Repository
 */
@Repository
public interface ProductInventoryDetailRepository extends JpaRepository<ProductInventoryDetail, String> {

    /**
     * Market koduna göre ürün detayı getir
     */
    Optional<ProductInventoryDetail> findByMarketCode(String marketCode);

    /**
     * Market kodunun daha önce kullanılıp kullanılmadığını kontrol et
     */
    boolean existsByMarketCode(String marketCode);

    /**
     * Market kodunun başka bir kayıt tarafından kullanılıp kullanılmadığını kontrol et (update için)
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM ProductInventoryDetail p WHERE p.marketCode = :marketCode AND p.id != :id")
    boolean existsByMarketCodeAndIdNot(@Param("marketCode") String marketCode, @Param("id") String id);

    /**
     * Kategoriye göre ürün detaylarını getir
     */
    List<ProductInventoryDetail> findByCategoryId(String categoryId);

    /**
     * Aktif ürün detaylarını getir
     */
    List<ProductInventoryDetail> findByActiveTrue();

    /**
     * Kategoriye göre aktif ürün detaylarını getir
     */
    @Query("SELECT p FROM ProductInventoryDetail p WHERE p.categoryId = :categoryId AND p.active = true")
    List<ProductInventoryDetail> findActiveByCategoryId(@Param("categoryId") String categoryId);

    /**
     * Markaya göre ürün detaylarını getir
     */
    List<ProductInventoryDetail> findByBrandName(String brandName);

    /**
     * Ürün adına göre arama yap (like)
     */
    @Query("SELECT p FROM ProductInventoryDetail p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :productName, '%'))")
    List<ProductInventoryDetail> searchByProductName(@Param("productName") String productName);

    /**
     * Market kodu veya ürün adına göre arama yap
     */
    @Query("SELECT p FROM ProductInventoryDetail p WHERE LOWER(p.marketCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.brandName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<ProductInventoryDetail> searchByKeyword(@Param("keyword") String keyword);
}

