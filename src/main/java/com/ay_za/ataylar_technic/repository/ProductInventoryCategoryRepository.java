package com.ay_za.ataylar_technic.repository;

import com.ay_za.ataylar_technic.entity.ProductInventoryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Ürün Envanter Kategorileri Repository
 */
@Repository
public interface ProductInventoryCategoryRepository extends JpaRepository<ProductInventoryCategory, String> {


    /**
     * Üst kategori ID'sine göre alt kategorileri getir
     */
    List<ProductInventoryCategory> findByParentCategoryId(String parentCategoryId);

    /**
     * Ana kategorileri getir (parent_category_id null olanlar)
     */
    @Query("SELECT c FROM ProductInventoryCategory c WHERE c.parentCategoryId IS NULL ORDER BY c.displayOrder, c.categoryName")
    List<ProductInventoryCategory> findMainCategories();

    /**
     * Aktif kategorileri getir
     */
    List<ProductInventoryCategory> findByIsActiveTrue();

    /**
     * Aktif ana kategorileri getir
     */
    @Query("SELECT c FROM ProductInventoryCategory c WHERE c.parentCategoryId IS NULL AND c.isActive = true ORDER BY c.displayOrder, c.categoryName")
    List<ProductInventoryCategory> findActiveMainCategories();

    /**
     * Belirli bir seviyedeki kategorileri getir
     */
    List<ProductInventoryCategory> findByCategoryLevel(Integer level);

    /**
     * Üst kategoriye bağlı aktif alt kategorileri getir
     */
    @Query("SELECT c FROM ProductInventoryCategory c WHERE c.parentCategoryId = :parentId AND c.isActive = true ORDER BY c.displayOrder, c.categoryName")
    List<ProductInventoryCategory> findActiveSubCategories(@Param("parentId") String parentId);

    /**
     * Bir kategorinin alt kategorisi olup olmadığını kontrol et
     */
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM ProductInventoryCategory c WHERE c.parentCategoryId = :categoryId")
    boolean hasSubCategories(@Param("categoryId") String categoryId);

    /**
     * Kategori adına göre arama yap
     */
    @Query("SELECT c FROM ProductInventoryCategory c WHERE LOWER(c.categoryName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<ProductInventoryCategory> searchByCategoryName(@Param("name") String name);
}

