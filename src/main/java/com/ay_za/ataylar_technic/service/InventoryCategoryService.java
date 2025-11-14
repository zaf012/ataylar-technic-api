package com.ay_za.ataylar_technic.service;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;

import java.util.List;

public interface InventoryCategoryService {

    // CRUD işlemleri
    InventoryCategoryDto createCategory(InventoryCategoryDto dto);

    InventoryCategoryDto updateCategory(String id, InventoryCategoryDto dto);

    void deleteCategory(String id);

    InventoryCategoryDto getCategoryById(String id);

    List<InventoryCategoryDto> getAllCategories();

    // Hiyerarşik sorgular
    List<InventoryCategoryDto> getMainCategories();

    List<InventoryCategoryDto> getSubCategoriesByMainCategoryId(String mainCategoryId);

    List<InventoryCategoryDto> getCategoryHierarchy();

    InventoryCategoryDto getCategoryWithSubCategories(String id);

    // Aktiflik bazlı sorgular
    List<InventoryCategoryDto> getActiveCategories();

    List<InventoryCategoryDto> getActiveMainCategories();

    List<InventoryCategoryDto> getActiveSubCategoriesByMainCategoryId(String mainCategoryId);

    // Arama işlemleri
    InventoryCategoryDto getCategoryByCategoryCode(String categoryCode);

    InventoryCategoryDto getCategoryByQrCode(String qrCode);

    List<InventoryCategoryDto> searchCategoriesByName(String categoryName);

    List<InventoryCategoryDto> getCategoriesByMarketCode(String marketCode);

    // Yardımcı metodlar
    boolean existsByCategoryCode(String categoryCode);

    boolean existsByQrCode(String qrCode);
}

