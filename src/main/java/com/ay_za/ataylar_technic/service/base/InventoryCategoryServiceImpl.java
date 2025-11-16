package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;

import java.util.List;

public interface InventoryCategoryServiceImpl {

    // CRUD işlemleri
    InventoryCategoryDto createCategory(InventoryCategoryDto dto);

    InventoryCategoryDto updateCategory(String id, InventoryCategoryDto dto);

    void deleteCategory(String id);

    InventoryCategoryDto getCategoryById(String id);

    List<InventoryCategoryDto> getAllCategories();

    // Hiyerarşik sorgular
    List<InventoryCategoryDto> getMainCategories();

    List<InventoryCategoryDto> getSubCategoriesByMainCategoryId(String mainCategoryId);

    // Aktiflik bazlı sorgular
    List<InventoryCategoryDto> getActiveCategories();

    List<InventoryCategoryDto> getActiveMainCategories();

    List<InventoryCategoryDto> getActiveSubCategoriesByMainCategoryId(String mainCategoryId);

    List<InventoryCategoryDto> searchCategoriesByName(String categoryName);

    List<InventoryCategoryDto> getCategoriesByMarketCode(String marketCode);

}

