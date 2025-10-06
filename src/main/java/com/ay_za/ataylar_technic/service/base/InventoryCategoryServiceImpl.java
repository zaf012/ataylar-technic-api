package com.ay_za.ataylar_technic.service.base;

import com.ay_za.ataylar_technic.dto.InventoryCategoryDto;

import java.util.List;

public interface InventoryCategoryServiceImpl {

    // Temel CRUD işlemleri
    List<InventoryCategoryDto> getAllCategories();
    List<InventoryCategoryDto> getActiveCategories();
    InventoryCategoryDto getCategoryById(String id);
    InventoryCategoryDto createCategory(InventoryCategoryDto categoryDto);
    InventoryCategoryDto updateCategory(String id, InventoryCategoryDto categoryDto);
    void deleteCategory(String id);

    // Hiyerarşik işlemler
    List<InventoryCategoryDto> getRootCategories();
    List<InventoryCategoryDto> getActiveRootCategories();
    List<InventoryCategoryDto> getSubCategories(String parentId);
    List<InventoryCategoryDto> getActiveSubCategories(String parentId);
    InventoryCategoryDto getCategoryWithSubCategories(String id);
    List<InventoryCategoryDto> getCategoryHierarchy();

    // Arama işlemleri
    InventoryCategoryDto getCategoryByQrCode(String qrCode);
    InventoryCategoryDto getCategoryByCategoryCode(String categoryCode);
    List<InventoryCategoryDto> searchCategoriesByName(String name);
    List<InventoryCategoryDto> getCategoriesByLevel(Integer level);

    // Yardımcı işlemler
    String generateUniqueQrCode();
    boolean isQrCodeUnique(String qrCode);
    boolean isCategoryCodeUnique(String categoryCode);
    Long getSubCategoryCount(String parentId);

    // Validation işlemleri
    boolean canDeleteCategory(String id);
    boolean canMoveCategory(String categoryId, String newParentId);
}
